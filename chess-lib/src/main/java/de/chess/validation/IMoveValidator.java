package de.chess.validation;

import de.chess.dto.ChessGame;
import de.chess.model.GameBoard;
import de.chess.model.Move;

public interface IMoveValidator {

    /**
     * True if Move is valid, otherwise false
     * @param gameBoard game
     * @param move Move of interesst
     * @return true if valid
     */
   public boolean isMoveValid(GameBoard gameBoard, Move move);
}
