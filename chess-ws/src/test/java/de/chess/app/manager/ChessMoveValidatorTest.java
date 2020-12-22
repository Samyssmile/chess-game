package de.chess.app.manager;

import de.chess.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessMoveValidatorTest {

    private ChessMoveValidator validator;

    @BeforeEach
    void setUp() {

        this.validator = new ChessMoveValidator();
    }

    @Test
    void isPawnE2_E4_Valid() {
        GameBoard gameBoard = new GameBoard();

        gameBoard.putPiece("a2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        gameBoard.putPiece("b2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        gameBoard.putPiece("c2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        gameBoard.putPiece("d2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        gameBoard.putPiece("e2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        gameBoard.putPiece("f2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        gameBoard.putPiece("g2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        gameBoard.putPiece("h2", new Piece(ChessColor.WHITE, PieceType.PAWN));

        gameBoard.putPiece("a7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        gameBoard.putPiece("b7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        gameBoard.putPiece("c7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        gameBoard.putPiece("d7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        gameBoard.putPiece("e7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        gameBoard.putPiece("f7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        gameBoard.putPiece("g7", new Piece(ChessColor.BLACK, PieceType.PAWN));
        gameBoard.putPiece("h7", new Piece(ChessColor.BLACK, PieceType.PAWN));


        Move move = new Move("e2", "e4");
        boolean isValid = validator.isMoveValid(gameBoard, move);

        assertTrue(isValid);
        gameBoard.print();

    }
}