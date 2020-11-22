package de.chess.dto.response;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;

import java.util.UUID;

public class Response {

    private UUID gameUUID;
    private RequestType requestType;
    private ChessGame gameDTO;

    public Response(RequestType requestType) {
        this.requestType = requestType;
    }

    public Response(UUID gameUUID, RequestType requestType) {
        this.gameUUID = gameUUID;
        this.requestType = requestType;
    }


    public ChessGame getGameDTO() {
        return gameDTO;
    }

    public void setGameDTO(ChessGame gameDTO) {
        this.gameDTO = gameDTO;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public void setGameUUID(UUID gameUUID) {
        this.gameUUID = gameUUID;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "Response{" +
                "gameUUID=" + gameUUID +
                ", requestType=" + requestType +
                ", gameDTO=" + gameDTO +
                '}';
    }

}
