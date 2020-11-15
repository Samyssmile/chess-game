package de.chess.app.server;

import com.google.gson.Gson;
import de.chess.dto.Declaration;
import de.chess.dto.Signal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessServer extends Thread implements IChessServer {
    private static final Logger LOGGER = Logger.getGlobal();
    private static final int MAX_BUFFER_CAPACITY = 100;
    private static final String POISON_PILL = "POISON_PILL";

    private final int port;
    private final boolean isBlockingMode;
    private final ServerProperties serverProperties;

    private final GameClientReceiver gameClientReceiver = new GameClientReceiver();
    private final String address;
    private SubmissionPublisher<ServerGameClient> publisher;
    private boolean running;
    private ServerSocketChannel serverSocket;
    private Selector selector;
    private Gson gson;

    public ChessServer(ServerProperties serverProperties) throws IOException {
        this.serverProperties = serverProperties;
        this.port = serverProperties.getServerPort();
        this.address = serverProperties.serverAddress();
        this.isBlockingMode = serverProperties.isBlockingMode();
        this.gson = new Gson();
        init();
        subscribeReceiver();
    }

    private void init() throws IOException {
        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(isBlockingMode);
        serverSocket.socket().bind(new InetSocketAddress(address, port));
        selector = Selector.open();
        SelectionKey acceptKey = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }


    private void subscribeReceiver() {
        publisher =
                new SubmissionPublisher<>(ForkJoinPool.commonPool(), MAX_BUFFER_CAPACITY);
        publisher.subscribe(gameClientReceiver);
    }


    @Override
    public void run() {
        try {
            running = true;
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(256);


            while (running) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {

                    SelectionKey key = iter.next();



                    if (key.isReadable() && key.isAcceptable()) {
                        Declaration filledDeclaration = answerWithEcho(buffer, key);
                        register(selector, key,  filledDeclaration);
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(Selector selector, SelectionKey key, Declaration filledDeclaration)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        ServerGameClient serverGameClient = new ServerGameClient(selector, client, key, filledDeclaration);
        publisher.submit(serverGameClient);
    }

    private Declaration answerWithEcho(ByteBuffer buffer, SelectionKey key)
            throws IOException {

        SocketChannel client = (SocketChannel) key.channel();

        client.read(buffer);
        String declarationAsJson = new String(buffer.array()).trim();
        Declaration declaration = gson.fromJson(declarationAsJson, Declaration.class);
        LOGGER.info("Received Message from Client: " + declarationAsJson);
        if (new String(buffer.array()).trim().equals(POISON_PILL)) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        } else {
            buffer.flip();
            client.write(buffer);
            buffer.clear();
        }
        return declaration;
    }


}
