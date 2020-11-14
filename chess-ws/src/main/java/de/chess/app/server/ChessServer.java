package de.chess.app.server;

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
    private SubmissionPublisher<SocketChannel> publisher;
    private boolean running;
    private ServerSocketChannel serverSocket;
    private Selector selector;

    public ChessServer(ServerProperties serverProperties) throws IOException {
        this.serverProperties = serverProperties;
        this.port = serverProperties.getServerPort();
        this.address = serverProperties.serverAddress();
        this.isBlockingMode = serverProperties.isBlockingMode();

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

                    if (key.isAcceptable()) {
                        register(selector, serverSocket);
                    }

                    if (key.isReadable()) {
                        answerWithEcho(buffer, key);
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        publisher.submit(client);
    }

    private void answerWithEcho(ByteBuffer buffer, SelectionKey key)
            throws IOException {

        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        String receivedMessage = new String(buffer.array()).trim();
        LOGGER.info("Received Message from Client: "+receivedMessage);
        if (new String(buffer.array()).trim().equals(POISON_PILL)) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        } else {
            buffer.flip();
            client.write(buffer);
            buffer.clear();
        }
    }


}
