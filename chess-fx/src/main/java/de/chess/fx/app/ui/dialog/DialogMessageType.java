package de.chess.fx.app.ui.dialog;

public enum DialogMessageType {

    CANT_CONNECT_TO_SERVER("cant_connect_to_server"), CANT_CONNECT_TO_SERVER_TITLE("cant_connect_to_server_title");

    private final String value;

    DialogMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
