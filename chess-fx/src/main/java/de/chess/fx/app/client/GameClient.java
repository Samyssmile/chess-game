package de.chess.fx.app.client;

import de.chess.fx.app.handler.ExceptionHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient implements Serializable {
    public static final Logger LOGGER = Logger.getLogger(GameClient.class.getSimpleName());

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private SocketChannel client;

    private static ByteBuffer buffer;

    public GameClient() {
        try {
            client = SocketChannel.open(new InetSocketAddress("localhost", 8085));
            buffer = ByteBuffer.allocate(256);
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    public void connect()   {

        try{
            IClientProperties clientProperties = LocalDevPClientProperties.instance();
            if (client != null && client.isConnected()) {

                objectOutputStream = new ObjectOutputStream(client.socket().getOutputStream());
                objectInputStream = new ObjectInputStream(client.socket().getInputStream());
                writeTextMessageToServer("Hello from Client ******* Hello from Client *******");

            }
        }catch (IOException e){
            ExceptionHandler.handle(e);
        }


        LOGGER.log(Level.INFO, "Client connected...");
    }


    public String writeTextMessageToServer(String msg) {
        buffer = ByteBuffer.wrap(msg.getBytes());
        String response = null;
        try {
            client.write(buffer);
            client.read(buffer);
            response = new String(buffer.array()).trim();

            LOGGER.log(Level.INFO, "Response: " + response);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }


}
