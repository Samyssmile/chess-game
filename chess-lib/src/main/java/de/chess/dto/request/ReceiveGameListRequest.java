package de.chess.dto.request;

import de.chess.dto.RequestType;

public class ReceiveGameListRequest extends Request {
    public ReceiveGameListRequest() {
        super(RequestType.REQUEST_GAME_LIST);
    }
}
