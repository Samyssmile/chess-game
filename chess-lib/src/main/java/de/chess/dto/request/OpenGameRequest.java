package de.chess.dto.request;

import de.chess.dto.GameDTO;
import de.chess.dto.RequestType;

public class OpenGameRequest extends Request {

    private final GameDTO gameDTO;

    public OpenGameRequest(GameDTO gameDTO) {
        super(RequestType.NEW_GAME);
        this.gameDTO = gameDTO;

    }

    public GameDTO getGameDTO() {
        return gameDTO;
    }
}
