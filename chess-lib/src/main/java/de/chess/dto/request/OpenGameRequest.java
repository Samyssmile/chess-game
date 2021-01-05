package de.chess.dto.request;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;

import java.util.UUID;

public class OpenGameRequest extends Request {

    private final ChessGame gameDTO;

    public OpenGameRequest(ChessGame gameDTO, UUID playerUUID) {
        super(RequestType.NEW_GAME, playerUUID);
        this.gameDTO = gameDTO;
    }

    public ChessGame getGameDTO() {
        return gameDTO;
    }
}
