package de.chess.app.server.nio;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.chess.app.manager.GameManager;
import de.chess.dto.GameDTO;
import de.chess.dto.RequestType;
import de.chess.dto.request.OpenGameRequest;
import de.chess.dto.request.ReceiveGameRequest;
import de.chess.dto.request.Request;
import de.chess.dto.response.OpenGameResponse;
import de.chess.dto.response.ReceiveGameListResponse;
import de.chess.dto.response.Response;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class EchoWorker implements Runnable {
    private List queue = new LinkedList();
    private Gson gson = new Gson();
    public static final Logger LOGGER = Logger.getGlobal();
    public void processData(NioServer server, SocketChannel socket, byte[] data, int count) {
        byte[] dataCopy = new byte[count];
        System.arraycopy(data, 0, dataCopy, 0, count);
        synchronized (queue) {
            queue.add(new ServerDataEvent(server, socket, dataCopy));
            queue.notify();
        }
    }

    @Override
    public void run() {
        ServerDataEvent dataEvent;

        while (true) {
            // Wait for data to become available
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                    }
                }
                dataEvent = (ServerDataEvent) queue.remove(0);
            }

            Response response = prepareResponse(new String(dataEvent.data));
            dataEvent.data = gson.toJson(response).getBytes();
            dataEvent.server.send(dataEvent.socket, dataEvent.data);
        }
    }

    private Response prepareResponse(String request) {
        JsonObject jsonObjectb = gson.fromJson(request, JsonObject.class);

        String requestType = jsonObjectb.get("requestType").getAsString();
        Response response = null;
        if (requestType.equals(RequestType.NEW_GAME.name())) {
            GameDTO gameDTO = gson.fromJson(request, OpenGameRequest.class).getGameDTO();
            gameDTO = GameManager.instance().requestGame(gameDTO);
            response = new OpenGameResponse(gameDTO.getUuid(), RequestType.NEW_GAME, true);
            response.setGameDTO(gameDTO);
            LOGGER.info("Server Response: "+RequestType.NEW_GAME+ " "+ " "+response.getGameDTO().getUuid()+ " "+response.toString());
        } else if (requestType.equals(RequestType.REQUEST_GAME_LIST.name())) {
            ReceiveGameListResponse responseReceiveGameList = new ReceiveGameListResponse(RequestType.REQUEST_GAME_LIST);
            List<GameDTO> gameList = GameManager.instance().getActiveGameList();
            responseReceiveGameList.setGameList(gameList);
            response = responseReceiveGameList;
            LOGGER.info("Server Response: "+RequestType.REQUEST_GAME_LIST+ " "+response.toString()+ " Games: "+responseReceiveGameList.getGameList().size());
        }

        return response;
    }


}