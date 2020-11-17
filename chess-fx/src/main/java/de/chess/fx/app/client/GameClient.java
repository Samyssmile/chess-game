package de.chess.fx.app.client;

import com.google.gson.Gson;
import de.chess.dto.GameDTO;

import java.io.IOException;
import java.util.logging.Logger;

public class GameClient {
    public static final Logger LOGGER = Logger.getGlobal();
    private final GameDTO gameDTO;
    private IClientProperties clientProperties;
    private Gson gson = new Gson();
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
            sendDataToServer("GET / HTTP/1.0\r\n\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendDataToServer(String jsonData) {
        RspHandler handler = new RspHandler();
        try {
            client.send(jsonData.getBytes(), handler);
            handler.waitForResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
