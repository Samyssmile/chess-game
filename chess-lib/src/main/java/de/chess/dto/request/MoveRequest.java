package de.chess.dto.request;

import de.chess.dto.RequestType;

import java.util.UUID;

public class MoveRequest extends Request {
    private String move;

    public MoveRequest(UUID gameUUID, UUID playerUUID, RequestType requestType, String move) {
        super(gameUUID, playerUUID, requestType);
        this.move = move;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}
