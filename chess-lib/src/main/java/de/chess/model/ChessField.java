package de.chess.model;

import java.io.Serializable;

public class ChessField implements Serializable {

    private String position;
    private Piece piece;

    public ChessField(String position, Piece piece) {
        this.position = position;
        this.piece = piece;
    }


    public ChessField(String position) {
        this.position = position;
    }

    public ChessField(IndexField field) {
        this.position = coordinatesToNotation(field.getX(), field.getY());
    }


    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public IndexField getAsIndexField() {
        IndexField field = new IndexField();
        switch (position.charAt(0)) {
            case 'a':
                field.setX(0);
                break;
            case 'b':
                field.setX(1);
                break;
            case 'c':
                field.setX(2);
                break;
            case 'd':
                field.setX(3);
                break;
            case 'e':
                field.setX(4);
                break;
            case 'f':
                field.setX(5);
                break;
            case 'g':
                field.setX(6);
                break;
            case 'h':
                field.setX(7);
                break;
            default:
                break;
        }

        switch (position.charAt(1)) {
            case '1':
                field.setY(7);
                break;
            case '2':
                field.setY(6);
                break;
            case '3':
                field.setY(5);
                break;
            case '4':
                field.setY(4);
                break;
            case '5':
                field.setY(3);
                break;
            case '6':
                field.setY(2);
                break;
            case '7':
                field.setY(1);
                break;
            case '8':
                field.setY(0);
                break;
            default:
                break;
        }

        return field;
    }

    /**
     * Forms notation from x and y.
     *
     * @param x
     * @param y
     * @return string
     */
    public String coordinatesToNotation(int x, int y) {
        int number = 0;
        char letter = 'z';
        String notation;

        switch (x) {
            case 0:
                number = 8;
                break;
            case 1:
                number = 7;
                break;
            case 2:
                number = 6;
                break;
            case 3:
                number = 5;
                break;
            case 4:
                number = 4;
                break;
            case 5:
                number = 3;
                break;
            case 6:
                number = 2;
                break;
            case 7:
                number = 1;
                break;
            default:
                break;
        }

        switch (y) {
            case 0:
                letter = 'a';
                break;
            case 1:
                letter = 'b';
                break;
            case 2:
                letter = 'c';
                break;
            case 3:
                letter = 'd';
                break;
            case 4:
                letter = 'e';
                break;
            case 5:
                letter = 'f';
                break;
            case 6:
                letter = 'g';
                break;
            case 7:
                letter = 'h';
                break;
            default:
                break;
        }

        notation = letter + Integer.toString(number);
        return notation;
    }

    @Override
    public String toString() {
        return getFEN();
    }

    private String getFEN() {
        String fen = " ";
        if (piece != null){
            fen = piece.getFEN();
        }
        return fen;
    }
}
