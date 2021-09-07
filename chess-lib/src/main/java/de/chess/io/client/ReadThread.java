package de.chess.io.client;

import de.chess.dto.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Logger;


public class ReadThread extends Thread {
    private static final Logger LOGGER = Logger.getGlobal();
    private final IResponseAnalyzer responseAnalyzer;
    private ObjectInputStream reader;
    private final Socket socket;
    private final GameClient client;

    public ReadThread(Socket socket, GameClient client, IResponseAnalyzer responseAnalyzer) {
        this.socket = socket;
        this.client = client;
        this.responseAnalyzer = responseAnalyzer;

        try {
            InputStream input = socket.getInputStream();
            this.reader = new ObjectInputStream(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Response response = (Response) this.reader.readObject();
                responseAnalyzer.analyze(response);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
