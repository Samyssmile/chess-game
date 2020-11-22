package de.chess.dto.response;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;

import java.util.ArrayList;
import java.util.List;

public class ReceiveGameListResponse extends Response {
    private List<ChessGame> gameList = new ArrayList<>();

    public ReceiveGameListResponse(RequestType requestType) {
        super(requestType);
    }

    public List<ChessGame> getGameList() {
        return gameList;
    }

    public void setGameList(List<ChessGame> gameList) {
        this.gameList = gameList;
    }
}
