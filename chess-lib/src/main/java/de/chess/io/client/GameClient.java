package de.chess.io.client;

import de.chess.dto.RequestType;
import de.chess.dto.request.Request;
import de.chess.dto.request.SelfIntroducingRequest;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient {
    private static final Logger LOGGER = Logger.getGlobal();
    private final String hostname;
    private final int port;
    private final IResponseAnalyzer responseAnalyzer;
    private final UUID playersUUID;
    private String userName;
    private Socket socket;
    private ReadThread readingThread;
    private WriteThread writingThread;

    private static GameClient instance = null;


    public static Optional<GameClient> getInstance() {
        return Optional.of(instance);
    }

    public static GameClient getAndIniTInstance(String hostname, int port, IResponseAnalyzer responseAnalyzer, UUID playersUUID) {
        if (instance == null) {
            instance = new GameClient(hostname, port, responseAnalyzer, playersUUID);
        }
        return instance;
    }

    private GameClient(String hostname, int port, IResponseAnalyzer responseAnalyzer, UUID playersUUID) {
        this.hostname = hostname;
        this.port = port;
        this.responseAnalyzer = responseAnalyzer;
        this.playersUUID = playersUUID;
    }

    /**
     * Connect to Server
     */
    public void execute() {
        if (connect(this.hostname, this.port)) {
            startReadWriteThreads();
        }

    }

    private void startReadWriteThreads() {
        writingThread = new WriteThread(this.socket, this);
        readingThread = new ReadThread(this.socket, this, responseAnalyzer);
        readingThread.start();
        writingThread.start();
        writingThread.addRequest(new SelfIntroducingRequest(RequestType.SELF_INTRODUCING, playersUUID));
    }

    private boolean connect(String hostname, int port) {
        boolean conntected = false;
        try {
            this.socket = new Socket(hostname, port);
            conntected = this.socket.isConnected();
            LOGGER.log(Level.INFO, "Connected");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Connection Refused or Server not found.");
        }

        return conntected;

    }

    public void sendRequest(Request request) {
        writingThread.addRequest(request);
    }

}