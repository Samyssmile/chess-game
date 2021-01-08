package de.chess.io.client;

import de.chess.dto.request.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteThread extends Thread {
    private static final Logger LOGGER = Logger.getGlobal();
    private ObjectOutputStream writer;
    private final Socket socket;
    private final GameClient client;
    private LinkedBlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();

    public WriteThread(Socket socket, GameClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            this.writer = new ObjectOutputStream(output);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        do {
            synchronized (requestQueue) {
                try {
                    Request request = requestQueue.take();
                    writer.writeObject(request);
                    writer.flush();
                    LOGGER.log(Level.INFO, "Request was sent");
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                if (requestQueue.isEmpty())
                    requestQueue.notify(); // notify the producer
            }

        } while (socket.isConnected());

        closeConnection();
    }

    private void closeConnection() {
        try {
            this.socket.close();
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }

    /**
     * Add a Request to execution queue.
     *
     * @param request
     */
    public void addRequest(Request request) {
        LOGGER.log(Level.INFO, "New Request added to request queue");
        requestQueue.add(request);
    }
}