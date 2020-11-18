package de.chess.fx.app.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.chess.dto.GameDTO;
import de.chess.dto.request.OpenGameRequest;
import de.chess.dto.request.Request;

import java.io.IOException;
import java.util.logging.Logger;

public class GameClient {
    public static final Logger LOGGER = Logger.getGlobal();
    private final GameDTO gameDTO;
    private IClientProperties clientProperties;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private NioClient client = null;

    public GameClient(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
        try {
            this.clientProperties = LocalDevPClientProperties.instance();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        try {
            client = new NioClient(this.clientProperties.getLocalHostAddress(), this.clientProperties.getServerPort());
            Thread t = new Thread(client);
            t.setDaemon(true);
            t.start();
            sendRequest(new OpenGameRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(String jsonData) {
        RspHandler handler = new RspHandler();
        try {
            client.send(jsonData.getBytes(), handler);
            handler.waitForResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(Request request) {
        RspHandler handler = new RspHandler();
        try {
            byte[] byteData = getRequestAsByteData(request);
            client.send(byteData, handler);
            handler.waitForResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getRequestAsByteData(Request request) {
        return gson.toJson(request).getBytes();
    }

    private String getRequestAsJson(Request request) {
        return gson.toJson(request);
    }
}
