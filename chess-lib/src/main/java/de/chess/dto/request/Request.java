package de.chess.dto.request;

import de.chess.dto.RequestType;

import java.io.Serializable;
import java.util.UUID;

public class Request implements Serializable {

    private UUID gameUUID;
    private RequestType requestType;

    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    public Request(UUID gameUUID, RequestType requestType) {
        this.gameUUID = gameUUID;
        this.requestType = requestType;
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
}
