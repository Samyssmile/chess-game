package de.chess.fx.app.client;

import com.google.gson.Gson;
import de.chess.dto.Declaration;
import de.chess.dto.GameDTO;
import de.chess.dto.Signal;
import de.chess.fx.app.handler.ExceptionHandler;
import org.ietf.jgss.GSSContext;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient implements Serializable {
    public static final Logger LOGGER = Logger.getLogger(GameClient.class.getSimpleName());
    private final GameDTO gameDTO;

    private SocketChannel client;
    Gson gson = new Gson();
    private static ByteBuffer buffer;

    public GameClient(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
        try {
            String paramterIP = System.getProperty("ip");
            client = SocketChannel.open(new InetSocketAddress((paramterIP != null) ? paramterIP : "localhost", 8085));
            buffer = ByteBuffer.allocate(256);
            if (client != null && client.isConnected()) {
                Declaration declarationDTO = new Declaration(UUID.randomUUID().toString(), gameDTO);
                String declarationAsJson = gson.toJson(declarationDTO);
                writeTextMessageToServer(declarationAsJson);
                LOGGER.log(Level.INFO, "Client connected...");

            }
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
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
