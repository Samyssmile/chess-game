package de.chess.io.client;

import de.chess.dto.response.Response;

import java.io.*;
import java.net.Socket;


public class ReadThread extends Thread {
    private ObjectInputStream reader;
    private final Socket socket;
    private final GameClient client;

    public ReadThread(Socket socket, GameClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            this.reader = new ObjectInputStream(input);
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Response response = (Response) this.reader.readObject();
                System.out.println(response.getRequestType());
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}