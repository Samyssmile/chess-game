package de.chess.app.game;

import de.chess.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessMoveValidatorTest {

    private ChessMoveValidator validator;
    private GameBoard gameBoard = new GameBoard();

    @BeforeEach
    void setUp() {
        this.validator = new ChessMoveValidator();
    }

    @Test
    void isPawnE2_E4_Valid() {
        System.out.println("Pawn E2 - E4");
        gameBoard.putPiece("e2", new Piece(ChessColor.WHITE, PieceType.PAWN));
        Move move = new Move("e2", "e4");
        boolean isValid = validator.isMoveValid(gameBoard, move);
        gameBoard.print();

        assertTrue(isValid);
    }


    /**
     * Validate one pawn beat another
     */
    @Test
    void isPawnBeatE2_E4_Valid() {
        System.out.println("\n Pawn E3 - F4");
        gameBoard.putPiece("e3", new Piece(ChessColor.WHITE, PieceType.PAWN));
        gameBoard.putPiece("f4", new Piece(ChessColor.WHITE, PieceType.PAWN));
        Move move = new Move("e2", "e4");
        boolean isValid = validator.isMoveValid(gameBoard, move);
        gameBoard.print();

        assertTrue(isValid);
    }
}
