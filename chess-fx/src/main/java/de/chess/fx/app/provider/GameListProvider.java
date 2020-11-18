package de.chess.fx.app.provider;

import de.chess.dto.GameDTO;
import de.chess.dto.RequestType;
import de.chess.dto.request.ReceiveGameRequest;
import de.chess.fx.app.client.GameClient;
import de.chess.fx.app.model.GameRowData;

import java.util.ArrayList;
import java.util.List;

public class GameListProvider implements IGameListProvider {

    private List<GameRowData> gameRowList = new ArrayList<>();

    private static IGameListProvider instance = null;

    private GameListProvider() {
    }

    public static IGameListProvider getInstance() {
        if (instance == null) {
            instance = new GameListProvider();
        }
        return instance;
    }

    @Override
    public List<GameRowData> receiveGameList() {
        GameClient.getInstance().sendRequest(new ReceiveGameRequest(RequestType.REQUEST_GAME_LIST));

        return gameRowList;
    }

    @Override
    public void setGameList(List<GameDTO> gameList) {
        gameRowList.clear();
        for (GameDTO gameDTO : gameList) {
            GameRowData gameRowData = new GameRowData(gameDTO.getHostPlayerName(), gameDTO.getGameName(), gameDTO.getHostColor(), gameDTO.getTimeLimit());
            gameRowList.add(gameRowData);
        }

    }
}
