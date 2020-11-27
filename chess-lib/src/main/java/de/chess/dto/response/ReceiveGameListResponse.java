package de.chess.dto.response;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;

import java.util.ArrayList;
import java.util.List;

public class ReceiveGameListResponse extends Response {
    private final List<ChessGame> gameList;

    public ReceiveGameListResponse(List<ChessGame> gameList) {
        super(RequestType.REQUEST_GAME_LIST);
        System.out.println("Response Game Lsit created: "+gameList.size());
        this.gameList = gameList;
    }

    public List<ChessGame> getGameList() {
        return gameList;
    }

}
