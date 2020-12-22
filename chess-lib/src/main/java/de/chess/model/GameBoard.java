package de.chess.model;

import java.io.Serializable;

public class GameBoard implements Serializable {

    private final ChessField[][] board = new ChessField[8][8];

    public GameBoard() {
        initEmptyBoard();
    }

    private void initEmptyBoard() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y<board[x].length ; y++) {
                board[x][y] = new ChessField(new IndexField(x, y));
            }
        }
    }

    public void putPiece(String coordinate, Piece piece) {
        IndexField indexField = getAsIndexField(coordinate);
        int x = indexField.getX();
        int y = indexField.getY();

        board[x][y].setPiece(piece);
    }
    public ChessField[][] getBoard() {
        return board;
    }

    public void print() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y< board[x].length ; y++) {
                System.out.print("[" + board[x][y] + "]");
            }
            System.out.println("");
        }
    }

    public IndexField getAsIndexField(String chessNotation) {
        IndexField field = new IndexField();
        switch (chessNotation.charAt(0)) {
            case 'a':
                field.setY(0);
                break;
            case 'b':
                field.setY(1);
                break;
            case 'c':
                field.setY(2);
                break;
            case 'd':
                field.setY(3);
                break;
            case 'e':
                field.setY(4);
                break;
            case 'f':
                field.setY(5);
                break;
            case 'g':
                field.setY(6);
                break;
            case 'h':
                field.setY(7);
                break;
            default:
                break;
        }

        switch (chessNotation.charAt(1)) {
            case '1':
                field.setX(7);
                break;
            case '2':
                field.setX(6);
                break;
            case '3':
                field.setX(5);
                break;
            case '4':
                field.setX(4);
                break;
            case '5':
                field.setX(3);
                break;
            case '6':
                field.setX(2);
                break;
            case '7':
                field.setX(1);
                break;
            case '8':
                field.setX(0);
                break;
            default:
                break;
        }

        return field;
    }


    public void initStartState() {
        initEmptyBoard();
        putPiece("a2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("b2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("c2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("d2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("e2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("f2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("g2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("h2", new Piece(ChessColor.WHITE, PieceType.PAWN));

        putPiece("a7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("b7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("c7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("d7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("e7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("f7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("g7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("h7", new Piece(ChessColor.BLACK, PieceType.PAWN));
    }
}
