package de.chess.fx.app.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient implements Serializable {
    public static final Logger LOGGER = Logger.getLogger(GameClient.class.getSimpleName());


    public void connect() throws IOException {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            IClientProperties clientProperties = LocalDevPClientProperties.instance();
            boolean isConnected = socketChannel.connect(new InetSocketAddress(clientProperties.getServerAddress(), clientProperties.getServerPort()));
            if (isConnected) {

                ObjectOutputStream oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                oos.writeObject("Hello from Client Side");


                ObjectInputStream ois = new ObjectInputStream(socketChannel.socket().getInputStream());
                String serverSideMessage = null;
                try {
                    serverSideMessage = (String) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                LOGGER.log(Level.INFO, serverSideMessage);

            }
            LOGGER.log(Level.INFO, "Client connected...");
        }


    }
}
