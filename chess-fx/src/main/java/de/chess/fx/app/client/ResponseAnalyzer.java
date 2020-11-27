package de.chess.fx.app.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.chess.dto.ChessGame;
import de.chess.dto.response.ReceiveGameListResponse;
import de.chess.dto.response.Response;
import de.chess.fx.app.provider.GameListProvider;
import de.chess.io.client.IResponseAnalyzer;

import java.util.List;

public class ResponseAnalyzer implements IResponseAnalyzer {


    @Override
    public void analyze(Response response) {
        switch (response.getRequestType()){
            case REQUEST_GAME_LIST:
                ReceiveGameListResponse receiveGameListResponse = (ReceiveGameListResponse) response;
                List<ChessGame> gameList = receiveGameListResponse.getGameList();
                GameListProvider.getInstance().setGameList(gameList);
            case JOIN: ;
            case MOVE:;
            case MESSAGE:;
            case SURRENDER:;
            case NEW_GAME:;
            case REMIS:;

        }
    }

    @Override
    public Response analyze(String jsonResponse) {
        return null;
    }
}
