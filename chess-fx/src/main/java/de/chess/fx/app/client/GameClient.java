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
    private GameDTO gameDTO;
    private IClientProperties clientProperties;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private NioClient client = null;
    private static GameClient instance = null;

    private GameClient() {
        try {
            this.clientProperties = LocalDevPClientProperties.instance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameClient getInstance() {
        if (instance == null) {
            instance = new GameClient();
            instance.start();
        }
        return instance;
    }

    public void start() {
        try {
            client = new NioClient(this.clientProperties.getLocalHostAddress(), this.clientProperties.getServerPort());
            Thread t = new Thread(client);
            t.setDaemon(true);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openGameRequest(){
        sendRequest(new OpenGameRequest(gameDTO));
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

    public GameDTO getGameDTO() {
        return gameDTO;
    }

    public void setGameDTO(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
    }

    private byte[] getRequestAsByteData(Request request) {
        return gson.toJson(request).getBytes();
    }

    private String getRequestAsJson(Request request) {
        return gson.toJson(request);
    }
}
