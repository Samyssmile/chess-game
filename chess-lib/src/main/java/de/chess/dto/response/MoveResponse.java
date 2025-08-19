package de.chess.dto.response;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;

import java.util.UUID;

public class MoveResponse extends Response {
    
    private boolean moveAccepted;
    private String move;
    private String message;

    public MoveResponse(UUID gameUUID, RequestType requestType, boolean moveAccepted, String move, ChessGame gameDTO) {
        super(gameUUID, requestType);
        this.moveAccepted = moveAccepted;
        this.move = move;
        this.setGameDTO(gameDTO);
    }

    public MoveResponse(UUID gameUUID, RequestType requestType, boolean moveAccepted, String move, String message) {
        super(gameUUID, requestType);
        this.moveAccepted = moveAccepted;
        this.move = move;
        this.message = message;
    }

    public boolean isMoveAccepted() {
        return moveAccepted;
    }

    public void setMoveAccepted(boolean moveAccepted) {
        this.moveAccepted = moveAccepted;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MoveResponse{" +
                "moveAccepted=" + moveAccepted +
                ", move='" + move + '\'' +
                ", message='" + message + '\'' +
                ", gameUUID=" + getGameUUID() +
                ", requestType=" + getRequestType() +
                '}';
    }
}