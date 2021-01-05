package de.chess.dto.response;

import de.chess.dto.RequestType;

import java.util.UUID;

public class OpenGameResponse extends Response {
    private final boolean granted;
    private final UUID hostPlayerUUID;

    public OpenGameResponse(UUID gameUUID, UUID hostPlayerUUID, RequestType requestType, boolean granted) {
        super(gameUUID, requestType);
        this.granted = granted;
        this.hostPlayerUUID = hostPlayerUUID;
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
