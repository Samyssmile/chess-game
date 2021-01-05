package de.chess.dto.request;

import de.chess.dto.RequestType;

import java.io.Serializable;
import java.util.UUID;

public class Request implements Serializable {

    private  UUID playerUUID;
    private UUID gameUUID;
    private RequestType requestType;

    public Request(RequestType requestType, UUID playerUUID) {
        this.requestType = requestType;
        this.playerUUID = playerUUID;
    }

    public Request(UUID gameUUID, UUID playerUUID, RequestType requestType) {
        this.gameUUID = gameUUID;
        this.requestType = requestType;
        this.playerUUID = playerUUID;
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

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public String toString() {
        return "Request{" +
                "playerUUID=" + playerUUID +
                ", gameUUID=" + gameUUID +
                ", requestType=" + requestType +
                '}';
    }
}
