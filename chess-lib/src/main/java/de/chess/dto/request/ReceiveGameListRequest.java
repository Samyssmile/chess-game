package de.chess.dto.request;

import de.chess.dto.RequestType;

import java.util.UUID;

public class ReceiveGameListRequest extends Request {
    public ReceiveGameListRequest(UUID playerUUID) {
        super(RequestType.REQUEST_GAME_LIST, playerUUID);
    }
}
