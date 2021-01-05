package de.chess.dto.request;

import de.chess.dto.RequestType;

import java.util.UUID;

public class SelfIntroducingRequest extends Request {

    public SelfIntroducingRequest(RequestType requestType, UUID playerUUID) {
        super(requestType, playerUUID);
    }



}
