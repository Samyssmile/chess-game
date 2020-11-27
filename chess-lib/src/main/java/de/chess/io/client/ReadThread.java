package de.chess.io.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.chess.dto.response.Response;
import de.chess.io.server.IRequestAnalyzer;

import java.io.*;
import java.net.Socket;


public class ReadThread extends Thread {
    private final IResponseAnalyzer responseAnalyzer;
    private ObjectInputStream reader;
    private final Socket socket;
    private final GameClient client;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ReadThread(Socket socket, GameClient client, IResponseAnalyzer responseAnalyzer) {
        this.socket = socket;
        this.client = client;
        this.responseAnalyzer = responseAnalyzer;

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
                System.out.println(gson.toJson(response));
                responseAnalyzer.analyze(response);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}