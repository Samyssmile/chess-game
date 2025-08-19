package de.chess.model;

import java.io.IOException;
import java.io.Serializable;

public class GameBoard implements Serializable {

    private final ChessField[][] board = new ChessField[8][8];

    public GameBoard() {
        initEmptyBoard();
    }

    /**
     * Custom deserialization method to prevent constructor from resetting board
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Use default deserialization for all fields
        in.defaultReadObject();
        // Do NOT call initEmptyBoard() here - the board state is already deserialized correctly
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

    public Piece removePiece(String coordinate) {
        IndexField indexField = getAsIndexField(coordinate);
        int x = indexField.getX();
        int y = indexField.getY();

        Piece piece = board[x][y].getPiece();
        board[x][y].setPiece(null);
        return piece;
    }

    public Piece getPiece(String coordinate) {
        IndexField indexField = getAsIndexField(coordinate);
        int x = indexField.getX();
        int y = indexField.getY();

        return board[x][y].getPiece();
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

    public void initialDistibutionOfChessPieces() {
        initEmptyBoard();
        //White
        putPiece("a2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("b2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("c2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("d2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("e2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("f2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("g2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        putPiece("h2", new Piece(ChessColor.WHITE, PieceType.PAWN));

        putPiece("a1", new Piece(ChessColor.WHITE, PieceType.ROOK));
        putPiece("b1", new Piece(ChessColor.WHITE, PieceType.KNIGHT));
        putPiece("c1", new Piece(ChessColor.WHITE, PieceType.BISHOP));
        putPiece("d1", new Piece(ChessColor.WHITE, PieceType.QUEEN));
        putPiece("e1", new Piece(ChessColor.WHITE, PieceType.KING));
        putPiece("f1", new Piece(ChessColor.WHITE, PieceType.BISHOP));
        putPiece("g1", new Piece(ChessColor.WHITE, PieceType.KNIGHT));
        putPiece("h1", new Piece(ChessColor.WHITE, PieceType.ROOK));

        //Black
        putPiece("a7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("b7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("c7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("d7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("e7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("f7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("g7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        putPiece("h7", new Piece(ChessColor.BLACK, PieceType.PAWN));

        putPiece("a8", new Piece(ChessColor.BLACK, PieceType.ROOK));
        putPiece("b8", new Piece(ChessColor.BLACK, PieceType.KNIGHT));
        putPiece("c8", new Piece(ChessColor.BLACK, PieceType.BISHOP));
        putPiece("d8", new Piece(ChessColor.BLACK, PieceType.QUEEN));
        putPiece("e8", new Piece(ChessColor.BLACK, PieceType.KING));
        putPiece("f8", new Piece(ChessColor.BLACK, PieceType.BISHOP));
        putPiece("g8", new Piece(ChessColor.BLACK, PieceType.KNIGHT));
        putPiece("h8", new Piece(ChessColor.BLACK, PieceType.ROOK));
    }
}
