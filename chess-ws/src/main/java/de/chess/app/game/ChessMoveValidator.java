package de.chess.app.game;

import de.chess.model.GameBoard;
import de.chess.validation.IMoveValidator;
import de.chess.model.Move;

public class ChessMoveValidator implements IMoveValidator {

    @Override
    public boolean isMoveValid(GameBoard gameBoard, Move move) {
        return false;
    }

}
