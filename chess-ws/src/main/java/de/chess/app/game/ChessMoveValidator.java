package de.chess.app.game;

import de.chess.model.*;
import de.chess.validation.IMoveValidator;

public class ChessMoveValidator implements IMoveValidator {

    @Override
    public boolean isMoveValid(GameBoard gameBoard, Move move) {
        // Basic validations
        if (move == null || move.getFromField() == null || move.getToField() == null) {
            return false;
        }
        
        String fromField = move.getFromField();
        String toField = move.getToField();
        
        // Can't move to same position
        if (fromField.equals(toField)) {
            return false;
        }
        
        // Get source and destination positions
        IndexField fromIndex = gameBoard.getAsIndexField(fromField);
        IndexField toIndex = gameBoard.getAsIndexField(toField);
        
        // Check if source field has a piece
        ChessField fromChessField = gameBoard.getBoard()[fromIndex.getX()][fromIndex.getY()];
        Piece piece = fromChessField.getPiece();
        
        if (piece == null) {
            return false; // No piece to move
        }
        
        // Check if destination has same color piece (can't capture own piece)
        ChessField toChessField = gameBoard.getBoard()[toIndex.getX()][toIndex.getY()];
        Piece destinationPiece = toChessField.getPiece();
        
        if (destinationPiece != null && destinationPiece.getColor() == piece.getColor()) {
            return false; // Can't capture own piece
        }
        
        // Validate move based on piece type
        return validateMoveByPieceType(gameBoard, piece, fromIndex, toIndex);
    }
    
    private boolean validateMoveByPieceType(GameBoard gameBoard, Piece piece, IndexField from, IndexField to) {
        switch (piece.getPieceType()) {
            case PAWN:
                return validatePawnMove(gameBoard, piece, from, to);
            case ROOK:
                return validateRookMove(gameBoard, piece, from, to);
            case KNIGHT:
                return validateKnightMove(gameBoard, piece, from, to);
            case BISHOP:
                return validateBishopMove(gameBoard, piece, from, to);
            case QUEEN:
                return validateQueenMove(gameBoard, piece, from, to);
            case KING:
                return validateKingMove(gameBoard, piece, from, to);
            default:
                return false;
        }
    }
    
    private boolean validatePawnMove(GameBoard gameBoard, Piece pawn, IndexField from, IndexField to) {
        int direction = pawn.getColor() == ChessColor.WHITE ? -1 : 1; // White moves up (decrease row), Black moves down
        int rowDiff = to.getX() - from.getX();
        int colDiff = Math.abs(to.getY() - from.getY());
        
        ChessField destinationField = gameBoard.getBoard()[to.getX()][to.getY()];
        boolean isCapture = destinationField.getPiece() != null;
        
        // Forward move (no capture)
        if (colDiff == 0 && !isCapture) {
            // One square forward
            if (rowDiff == direction) {
                return true;
            }
            // Two squares forward from starting position
            if (rowDiff == 2 * direction) {
                int startingRow = pawn.getColor() == ChessColor.WHITE ? 6 : 1;
                return from.getX() == startingRow;
            }
        }
        // Diagonal capture
        else if (colDiff == 1 && rowDiff == direction && isCapture) {
            return true;
        }
        
        return false;
    }
    
    private boolean validateRookMove(GameBoard gameBoard, Piece rook, IndexField from, IndexField to) {
        // Rook moves horizontally or vertically
        if (from.getX() != to.getX() && from.getY() != to.getY()) {
            return false;
        }
        
        // Check path is clear
        return isPathClear(gameBoard, from, to);
    }
    
    private boolean validateKnightMove(GameBoard gameBoard, Piece knight, IndexField from, IndexField to) {
        int rowDiff = Math.abs(to.getX() - from.getX());
        int colDiff = Math.abs(to.getY() - from.getY());
        
        // Knight moves in L-shape: 2+1 or 1+2
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
    
    private boolean validateBishopMove(GameBoard gameBoard, Piece bishop, IndexField from, IndexField to) {
        int rowDiff = Math.abs(to.getX() - from.getX());
        int colDiff = Math.abs(to.getY() - from.getY());
        
        // Bishop moves diagonally
        if (rowDiff != colDiff) {
            return false;
        }
        
        // Check path is clear
        return isPathClear(gameBoard, from, to);
    }
    
    private boolean validateQueenMove(GameBoard gameBoard, Piece queen, IndexField from, IndexField to) {
        // Queen combines rook and bishop moves
        return validateRookMove(gameBoard, queen, from, to) || validateBishopMove(gameBoard, queen, from, to);
    }
    
    private boolean validateKingMove(GameBoard gameBoard, Piece king, IndexField from, IndexField to) {
        int rowDiff = Math.abs(to.getX() - from.getX());
        int colDiff = Math.abs(to.getY() - from.getY());
        
        // King moves one square in any direction
        return rowDiff <= 1 && colDiff <= 1;
    }
    
    private boolean isPathClear(GameBoard gameBoard, IndexField from, IndexField to) {
        int rowDirection = Integer.compare(to.getX(), from.getX());
        int colDirection = Integer.compare(to.getY(), from.getY());
        
        int currentRow = from.getX() + rowDirection;
        int currentCol = from.getY() + colDirection;
        
        // Check all squares between from and to (exclusive)
        while (currentRow != to.getX() || currentCol != to.getY()) {
            if (gameBoard.getBoard()[currentRow][currentCol].getPiece() != null) {
                return false; // Path blocked
            }
            currentRow += rowDirection;
            currentCol += colDirection;
        }
        
        return true;
    }
}
