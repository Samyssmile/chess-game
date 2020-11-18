package de.chess.dto.request;

import de.chess.dto.RequestType;

import java.util.UUID;

public class OpenGameRequest extends Request {

    public OpenGameRequest() {
        super(RequestType.NEW_GAME);
    }


}
