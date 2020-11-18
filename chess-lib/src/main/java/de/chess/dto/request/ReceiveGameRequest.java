package de.chess.dto.request;

import de.chess.dto.RequestType;

public class ReceiveGameRequest extends Request {
    public ReceiveGameRequest(RequestType requestType) {
        super(requestType);
    }
}
