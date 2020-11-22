package de.chess.dto.response;

import de.chess.dto.RequestType;

public class DummyResponse extends Response {
    private String msg = "Hello Dummy Response Here!";

    public DummyResponse() {
        super(RequestType.DUMMY);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
