package de.chess.dto.response;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;

import java.util.UUID;

public class OpenGameResponse extends Response {
    private final boolean granted;
    private final UUID hostPlayerUUID;

    public OpenGameResponse(UUID gameUUID, UUID hostPlayerUUID, RequestType requestType, boolean granted, ChessGame chessGame) {
        super(gameUUID, requestType);
        this.granted = granted;
        this.hostPlayerUUID = hostPlayerUUID;
        setGameDTO(chessGame);
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
