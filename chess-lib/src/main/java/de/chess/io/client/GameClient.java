package de.chess.io.client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient {
    private static final Logger LOGGER = Logger.getGlobal();
    private final String hostname;
    private final int port;
    private String userName;
    private Socket socket;

    public GameClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        connect(this.hostname, this.port);

        new ReadThread(this.socket, this).start();
        new WriteThread(this.socket, this).start();
    }

    private void connect(String hostname, int port) {
        try {
            this.socket = new Socket(hostname, port);
            LOGGER.log(Level.INFO, "Connected");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Connection Refused or Server not found.");
            e.printStackTrace();
        }

    }

}