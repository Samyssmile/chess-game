package de.chess.io.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer extends Thread {
    private static final Logger LOGGER = Logger.getGlobal();
    private final Set<ClientThread> clientThreads = new HashSet<>();

    private final int port;
    private final int backlog;
    private final InetAddress address;
    private boolean serverRuning = false;

    public GameServer(int port, int backlog, InetAddress address) {
        this.port = port;
        this.backlog = backlog;
        this.address = address;
        setReady();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port, this.backlog, this.address)) {
            LOGGER.log(Level.INFO, "Chat Server is listening on port " + this.port);

            while (this.serverRuning) {
                Socket socket = serverSocket.accept();
                LOGGER.log(Level.INFO, "Incoming Client accepted");

                ClientThread client = new ClientThread(socket, this);
                this.clientThreads.add(client);
                client.start();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopServer() {
        this.serverRuning = false;
    }

    public void setReady() {
        this.serverRuning = true;
    }

}
