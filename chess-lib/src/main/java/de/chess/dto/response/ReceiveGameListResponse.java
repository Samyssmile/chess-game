package de.chess.dto.response;

import de.chess.dto.GameDTO;
import de.chess.dto.RequestType;

import java.util.ArrayList;
import java.util.List;

public class ReceiveGameListResponse extends Response {
    private List<GameDTO> gameList = new ArrayList<>();

    public ReceiveGameListResponse(RequestType requestType) {
        super(requestType);
    }

    public List<GameDTO> getGameList() {
        return gameList;
    }

    public void setGameList(List<GameDTO> gameList) {
        this.gameList = gameList;
    }
}
