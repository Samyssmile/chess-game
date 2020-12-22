package de.chess.model;

public class Move {

    private String fromField;
    private String toField;

    public Move(String fromField, String toField) {
        this.fromField = fromField;
        this.toField = toField;
    }

    public String getFromField() {
        return fromField;
    }
}
