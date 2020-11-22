package de.chess.dto.response;

import de.chess.dto.RequestType;

import java.util.UUID;

public class OpenGameResponse extends Response {
    private final boolean granted;

    public OpenGameResponse(UUID gameUUID, RequestType requestType, boolean granted) {
        super(gameUUID, requestType);
        this.granted = granted;
    }

    public boolean isGranted() {
        return granted;
    }


    @Override
    public String toString() {
        return "OpenGameResponse{" +
                "granted=" + granted +
                '}';
    }
}
