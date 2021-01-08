package de.chess.io.server;

import de.chess.dto.request.Request;
import de.chess.dto.response.Response;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {
    private static final Logger LOGGER = Logger.getGlobal();
    private final IRequestAnalyzer requestAnalyzer;
    private boolean isConnected = false;
    private final Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private LinkedBlockingQueue requestList = new LinkedBlockingQueue();

    public ClientThread(Socket socket,  IRequestAnalyzer requestAnalyzer) {
        this.socket = socket;
        this.requestAnalyzer = requestAnalyzer;
    }

    public void run() {
        try {
            InputStream input = this.socket.getInputStream();
            this.reader = new ObjectInputStream(input);

            OutputStream output = this.socket.getOutputStream();
            this.writer =  new ObjectOutputStream(output);


            while (socket.isConnected()) {
                System.out.println("Waiting for requests...");
                Request jsonRequest = (Request) this.reader.readObject();
                Response response = requestAnalyzer.analyze(jsonRequest, this);
                sendResponse(response);
            }

        } catch (SocketException se) {
            LOGGER.warning("Connection closed");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void sendResponse(Response response) {
        try {
            this.writer.writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
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