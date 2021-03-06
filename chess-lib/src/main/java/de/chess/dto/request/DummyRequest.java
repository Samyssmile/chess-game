package de.chess.dto.request;

import de.chess.dto.RequestType;

import java.util.UUID;

public class DummyRequest extends Request {

    private String msg = "Hello World";

    public DummyRequest() {
        this(RequestType.DUMMY);
    }

    public DummyRequest(RequestType requestType) {
        super(requestType, UUID.randomUUID());
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
