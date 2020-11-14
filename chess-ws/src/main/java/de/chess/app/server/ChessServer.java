package de.chess.app.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessServer implements IChessServer {
    private static final Logger LOGGER = Logger.getLogger(ChessServer.class.getName());
    private static final int MAX_BUFFER_CAPACITY = 100;

    private final int port;
    private final boolean isBlockingMode;
    private final ServerProperties serverProperties;

    private final GameClientReceiver gameClientReceiver = new GameClientReceiver();
    private SubmissionPublisher<SocketChannel> publisher;

    public ChessServer(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
        this.port = serverProperties.getServerPort();
        this.isBlockingMode = serverProperties.isBlockingMode();

        subscribeReceiver();
    }

    private void subscribeReceiver() {
        publisher =
                new SubmissionPublisher<>(ForkJoinPool.commonPool(), MAX_BUFFER_CAPACITY);
        publisher.subscribe(gameClientReceiver);
    }

    @Override
    public void start() throws IOException, ClassNotFoundException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(isBlockingMode);

        LOGGER.info("Chess Backend Started...");
        LOGGER.info("Port: " + serverProperties.getServerPort());
        LOGGER.info("Blocking Mode: " + serverProperties.isBlockingMode());

        System.out.println("Start listing");
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            ObjectInputStream ois = new ObjectInputStream(socketChannel.socket().getInputStream());
            String clientSideMessage = (String) ois.readObject();
            LOGGER.log(Level.INFO, "Message from Client: "+clientSideMessage);

            ObjectOutputStream oos = new
                    ObjectOutputStream(socketChannel.socket().getOutputStream());
            oos.writeObject("Hello from Server Side");


            oos.close();
            ois.close();

            publisher.submit(socketChannel);
            if (socketChannel != null) {
                System.out.println("Connected");
                publisher.submit(socketChannel);
            }
        }
    }


}
