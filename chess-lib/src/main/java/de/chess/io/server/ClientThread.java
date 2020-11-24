package de.chess.io.server;

import de.chess.dto.response.Response;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {
    private static final Logger LOGGER = Logger.getGlobal();
    private boolean isConnected = false;
    private final Socket socket;
    private final GameServer server;
    private PrintWriter writer;
    private BufferedReader reader;


    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
        this.isConnected = socket.isConnected();
    }

    public void run() {
        try {
            InputStream input = this.socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = this.socket.getOutputStream();
            this.writer = new PrintWriter(output, true);

            while (this.isConnected) {
                String jsonRequest = this.reader.readLine();
                analyzeRequest(jsonRequest);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void analyzeRequest(String jsonRequest) {
        LOGGER.log(Level.INFO, "Incoming Request: {0}", jsonRequest);
    }

    public void sendResponse(Response message) {
        this.writer.println(message);
    }


    public void onDissconnect() {
        try {
            LOGGER.log(Level.INFO, "Closing Client Socket...");
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}