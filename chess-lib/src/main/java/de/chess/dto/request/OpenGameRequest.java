package de.chess.dto.request;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;

public class OpenGameRequest extends Request {

    private final ChessGame gameDTO;

    public OpenGameRequest(ChessGame gameDTO) {
        super(RequestType.NEW_GAME);
        this.gameDTO = gameDTO;
    }

    public ChessGame getGameDTO() {
        return gameDTO;
    }
}
