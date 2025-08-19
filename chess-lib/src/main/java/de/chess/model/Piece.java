package de.chess.model;

import java.io.Serializable;

public class Piece implements Serializable {

    private final ChessColor color;
    private final PieceType pieceType;

    public Piece(ChessColor color, PieceType pieceType) {
        this.pieceType = pieceType;
        this.color = color;
    }

    public ChessColor getColor() {
        return color;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public String getFEN() {
        String fen = "";

        switch (pieceType) {
            case PAWN:
                fen = "P";
                break;
            case BISHOP:
                fen = "B";
                break;
            case KING:
                fen = "K";
                break;
            case KNIGHT:
                fen = "N";
                break;
            case QUEEN:
                fen = "Q";
                break;
            case ROOK:
                fen = "R";
                break;
            default:
                fen = " ";
                break;
        }
        return fen;
    }
}
