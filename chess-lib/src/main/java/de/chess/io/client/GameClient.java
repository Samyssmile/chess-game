package de.chess.io.client;

import de.chess.dto.request.Request;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient {
    private static final Logger LOGGER = Logger.getGlobal();
    private final String hostname;
    private final int port;
    private String userName;
    private Socket socket;
    private ReadThread readingThread;
    private WriteThread writingThread;

    private static GameClient instance = null;


    public static Optional<GameClient> getInstance() {
        return Optional.of(instance);
    }

    public static GameClient getAndIniTInstance(String hostname, int port) {
        if (instance == null) {
            instance = new GameClient(hostname, port);
        }
        return instance;
    }

    private GameClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Connect to Server
     */
    public void execute() {
        connect(this.hostname, this.port);
        startReadWriteThreads();
    }

    private void startReadWriteThreads() {
        writingThread = new WriteThread(this.socket, this);
        readingThread = new ReadThread(this.socket, this);
        readingThread.start();
        writingThread.start();
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

    public void sendRequest(Request request) {
        writingThread.addRequest(request);
    }

}