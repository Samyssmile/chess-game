package de.chess.fx.app.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.chess.dto.ChessGame;
import de.chess.dto.response.OpenGameResponse;
import de.chess.dto.response.ReceiveGameListResponse;
import de.chess.dto.response.Response;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.fx.app.provider.GameListProvider;
import de.chess.io.client.IResponseAnalyzer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponseAnalyzer implements IResponseAnalyzer {
    private static final Logger LOGGER = Logger.getGlobal();

    @Override
    public void analyze(Response response) {
        switch (response.getRequestType()) {
            case REQUEST_GAME_LIST:
                onGameListResponse(response);
                break;
            case JOIN:
                break;
            case MOVE:
                break;
            case MESSAGE:
                break;
            case SURRENDER:
                break;
            case NEW_GAME:
                onNewGameResponse(response);
                break;
            case REMIS:
                break;

        }
    }

    private void onNewGameResponse(Response response) {
        LOGGER.log(Level.INFO, "New Game Response");
        OpenGameResponse openGameResponse = (OpenGameResponse) response;
        if (openGameResponse.isGranted()) {
            EventHandler.getInstance().fireEvent(EventType.OPEN_NEW_GAME);
        }

    }

    private void onGameListResponse(Response response) {
        LOGGER.log(Level.INFO, "Game List Response");
        ReceiveGameListResponse receiveGameListResponse = (ReceiveGameListResponse) response;
        List<ChessGame> gameList = receiveGameListResponse.getGameList();
        GameListProvider.getInstance().setGameList(gameList);
    }

    @Override
    public Response analyze(String jsonResponse) {
        return null;
    }
}
