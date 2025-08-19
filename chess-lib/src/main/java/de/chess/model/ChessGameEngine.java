package de.chess.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Chess game engine that handles check, checkmate, and stalemate detection
 */
public class ChessGameEngine {

    /**
     * Check if the specified color's king is in check
     */
    public boolean isInCheck(GameBoard gameBoard, ChessColor kingColor) {
        // Find the king position
        String kingPosition = findKingPosition(gameBoard, kingColor);
        if (kingPosition == null) {
            return false; // King not found (shouldn't happen in a valid game)
        }

        // Check if any opponent piece can attack the king
        ChessColor opponentColor = (kingColor == ChessColor.WHITE) ? ChessColor.BLACK : ChessColor.WHITE;
        return isSquareUnderAttack(gameBoard, kingPosition, opponentColor);
    }

    /**
     * Check if the specified color is in checkmate
     */
    public boolean isCheckmate(GameBoard gameBoard, ChessColor playerColor) {
        // Must be in check to be checkmate
        if (!isInCheck(gameBoard, playerColor)) {
            return false;
        }

        // Check if there are any legal moves that can get out of check
        return !hasLegalMoves(gameBoard, playerColor);
    }

    /**
     * Check if the specified color is in stalemate
     */
    public boolean isStalemate(GameBoard gameBoard, ChessColor playerColor) {
        // Must NOT be in check to be stalemate
        if (isInCheck(gameBoard, playerColor)) {
            return false;
        }

        // Check if there are no legal moves available
        return !hasLegalMoves(gameBoard, playerColor);
    }

    /**
     * Find the king position for the specified color
     */
    private String findKingPosition(GameBoard gameBoard, ChessColor kingColor) {
        ChessField[][] board = gameBoard.getBoard();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessField field = board[x][y];
                Piece piece = field.getPiece();
                if (piece != null && piece.getPieceType() == PieceType.KING && piece.getColor() == kingColor) {
                    return field.coordinatesToNotation(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Check if a square is under attack by the specified color
     */
    private boolean isSquareUnderAttack(GameBoard gameBoard, String targetSquare, ChessColor attackingColor) {
        ChessField[][] board = gameBoard.getBoard();
        
        // Check all pieces of the attacking color
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessField field = board[x][y];
                Piece piece = field.getPiece();
                
                if (piece != null && piece.getColor() == attackingColor) {
                    String fromSquare = field.coordinatesToNotation(x, y);
                    Move attackMove = new Move(fromSquare, targetSquare);
                    
                    // Check if this piece can attack the target square
                    if (canPieceAttackSquare(gameBoard, piece, new IndexField(x, y), gameBoard.getAsIndexField(targetSquare))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if a piece can attack a specific square (similar to move validation but for attack purposes)
     */
    private boolean canPieceAttackSquare(GameBoard gameBoard, Piece piece, IndexField from, IndexField to) {
        // For simplicity, we'll use the existing move validation but ignore the "can't capture own piece" rule
        // since we're checking for attacks, not actual moves
        
        int deltaX = to.getX() - from.getX();
        int deltaY = to.getY() - from.getY();
        int absX = Math.abs(deltaX);
        int absY = Math.abs(deltaY);

        switch (piece.getPieceType()) {
            case PAWN:
                return canPawnAttack(piece.getColor(), deltaX, deltaY);
            case ROOK:
                return (deltaX == 0 || deltaY == 0) && isPathClear(gameBoard, from, to);
            case BISHOP:
                return (absX == absY) && isPathClear(gameBoard, from, to);
            case KNIGHT:
                return (absX == 2 && absY == 1) || (absX == 1 && absY == 2);
            case QUEEN:
                return ((deltaX == 0 || deltaY == 0) || (absX == absY)) && isPathClear(gameBoard, from, to);
            case KING:
                return absX <= 1 && absY <= 1 && !(absX == 0 && absY == 0);
        }
        return false;
    }

    /**
     * Check if a pawn can attack a specific square
     */
    private boolean canPawnAttack(ChessColor pawnColor, int deltaX, int deltaY) {
        int direction = (pawnColor == ChessColor.WHITE) ? -1 : 1;
        return deltaX == direction && Math.abs(deltaY) == 1;
    }

    /**
     * Check if the path between two squares is clear
     */
    private boolean isPathClear(GameBoard gameBoard, IndexField from, IndexField to) {
        int deltaX = to.getX() - from.getX();
        int deltaY = to.getY() - from.getY();
        
        int stepX = Integer.compare(deltaX, 0);
        int stepY = Integer.compare(deltaY, 0);
        
        int currentX = from.getX() + stepX;
        int currentY = from.getY() + stepY;
        
        while (currentX != to.getX() || currentY != to.getY()) {
            if (gameBoard.getBoard()[currentX][currentY].getPiece() != null) {
                return false;
            }
            currentX += stepX;
            currentY += stepY;
        }
        return true;
    }

    /**
     * Check if the specified color has any legal moves
     */
    private boolean hasLegalMoves(GameBoard gameBoard, ChessColor playerColor) {
        ChessField[][] board = gameBoard.getBoard();
        
        // Check all pieces of the player's color
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessField field = board[x][y];
                Piece piece = field.getPiece();
                
                if (piece != null && piece.getColor() == playerColor) {
                    String fromSquare = field.coordinatesToNotation(x, y);
                    
                    // Try all possible destination squares
                    for (int toX = 0; toX < 8; toX++) {
                        for (int toY = 0; toY < 8; toY++) {
                            if (x == toX && y == toY) continue; // Can't move to same square
                            
                            String toSquare = board[toX][toY].coordinatesToNotation(toX, toY);
                            Move move = new Move(fromSquare, toSquare);
                            
                            // Check if this move is legal and doesn't leave king in check
                            if (isMoveLegalAndSafe(gameBoard, move, playerColor)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if a move is legal and doesn't leave the king in check
     */
    private boolean isMoveLegalAndSafe(GameBoard gameBoard, Move move, ChessColor playerColor) {
        // First check if the move is basically valid (without considering check)
        if (!isMoveBasicallyValid(gameBoard, move)) {
            return false;
        }

        // Simulate the move and check if it leaves the king in check
        GameBoard simulatedBoard = simulateMove(gameBoard, move);
        return !isInCheck(simulatedBoard, playerColor);
    }

    /**
     * Basic move validation (without considering check)
     */
    private boolean isMoveBasicallyValid(GameBoard gameBoard, Move move) {
        String fromField = move.getFromField();
        String toField = move.getToField();
        
        IndexField fromIndex = gameBoard.getAsIndexField(fromField);
        IndexField toIndex = gameBoard.getAsIndexField(toField);
        
        ChessField fromChessField = gameBoard.getBoard()[fromIndex.getX()][fromIndex.getY()];
        Piece piece = fromChessField.getPiece();
        
        if (piece == null) {
            return false;
        }
        
        ChessField toChessField = gameBoard.getBoard()[toIndex.getX()][toIndex.getY()];
        Piece destinationPiece = toChessField.getPiece();
        
        if (destinationPiece != null && destinationPiece.getColor() == piece.getColor()) {
            return false;
        }
        
        return canPieceAttackSquare(gameBoard, piece, fromIndex, toIndex);
    }

    /**
     * Create a copy of the game board with a move simulated
     */
    private GameBoard simulateMove(GameBoard originalBoard, Move move) {
        // Create a deep copy of the game board
        GameBoard simulatedBoard = createBoardCopy(originalBoard);
        
        // Execute the move on the simulated board
        String fromField = move.getFromField();
        String toField = move.getToField();
        
        Piece movingPiece = simulatedBoard.getPiece(fromField);
        simulatedBoard.removePiece(fromField);
        simulatedBoard.putPiece(toField, movingPiece);
        
        return simulatedBoard;
    }

    /**
     * Create a deep copy of the game board
     */
    private GameBoard createBoardCopy(GameBoard original) {
        GameBoard copy = new GameBoard();
        ChessField[][] originalFields = original.getBoard();
        
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece originalPiece = originalFields[x][y].getPiece();
                if (originalPiece != null) {
                    String coordinate = originalFields[x][y].coordinatesToNotation(x, y);
                    Piece copiedPiece = new Piece(originalPiece.getColor(), originalPiece.getPieceType());
                    copy.putPiece(coordinate, copiedPiece);
                }
            }
        }
        
        return copy;
    }
}