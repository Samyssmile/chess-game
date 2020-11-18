package de.chess.dto.request;

import de.chess.dto.RequestType;

import java.util.UUID;

public class MoveRequest extends Request {
    private String move;

    public MoveRequest(UUID gameUUID, RequestType requestType, String move) {
        super(gameUUID, requestType);
        this.move = move;
    }
}
