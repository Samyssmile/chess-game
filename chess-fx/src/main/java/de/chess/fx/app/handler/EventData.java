package de.chess.fx.app.handler;

public class EventData {

    private Object data;

    public EventData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
