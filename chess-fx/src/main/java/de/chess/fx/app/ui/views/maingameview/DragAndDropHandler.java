package de.chess.fx.app.ui.views.maingameview;

import de.chess.fx.app.ui.views.field.FieldView;
import de.chess.fx.app.ui.views.figure.ChessFigure;
import de.chess.model.Move;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

/**
 * Handles drag and drop functionality for chess pieces on the board
 */
public class DragAndDropHandler {
    
    private final FieldView[][] chessboardFields;
    private final GameViewModel viewModel;
    
    // Visual feedback for drag operations
    private static final DropShadow DRAG_SHADOW = new DropShadow(15, Color.GOLD);
    private static final DropShadow DROP_TARGET_SHADOW = new DropShadow(10, Color.LIGHTGREEN);
    private static final DropShadow INVALID_DROP_SHADOW = new DropShadow(10, Color.RED);
    
    // Drag state
    private FieldView dragSourceField = null;
    private ChessFigure draggedFigure = null;

    public DragAndDropHandler(FieldView[][] chessboardFields, GameViewModel viewModel) {
        this.chessboardFields = chessboardFields;
        this.viewModel = viewModel;
        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                FieldView field = chessboardFields[row][col];
                setupFieldForDragAndDrop(field);
            }
        }
    }

    private void setupFieldForDragAndDrop(FieldView field) {
        // Set up drag detection
        field.setOnDragDetected(event -> {
            if (field.isFigurePresent()) {
                ChessFigure figure = field.getFigure();
                
                // Check if it's the current player's piece and their turn
                if (canMovePiece(figure)) {
                    startDrag(field, figure, event);
                }
            }
            event.consume();
        });

        // Set up drag over (when dragging over a field)
        field.setOnDragOver(event -> {
            if (event.getGestureSource() != field && 
                event.getDragboard().hasString()) {
                
                // Check if this is a valid drop target
                if (isValidDropTarget(field)) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    field.setEffect(DROP_TARGET_SHADOW);
                } else {
                    field.setEffect(INVALID_DROP_SHADOW);
                }
            }
            event.consume();
        });

        // Set up drag entered (visual feedback when entering a field)
        field.setOnDragEntered(event -> {
            if (event.getGestureSource() != field && 
                event.getDragboard().hasString()) {
                
                if (isValidDropTarget(field)) {
                    field.setEffect(DROP_TARGET_SHADOW);
                } else {
                    field.setEffect(INVALID_DROP_SHADOW);
                }
            }
            event.consume();
        });

        // Set up drag exited (remove visual feedback when leaving a field)
        field.setOnDragExited(event -> {
            field.setEffect(null);
            event.consume();
        });

        // Set up drag dropped (handle the actual drop)
        field.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasString() && dragSourceField != null) {
                if (isValidDropTarget(field)) {
                    success = executeMoveIfValid(dragSourceField, field);
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // Set up drag done (cleanup after drag operation)
        field.setOnDragDone(event -> {
            // Remove visual effects
            if (draggedFigure != null) {
                draggedFigure.setEffect(null);
            }
            
            // Clear drag state
            clearDragState();
            event.consume();
        });
    }

    private void startDrag(FieldView sourceField, ChessFigure figure, MouseEvent event) {
        Dragboard dragboard = sourceField.startDragAndDrop(TransferMode.MOVE);
        
        // Store source field coordinates in dragboard
        ClipboardContent content = new ClipboardContent();
        content.putString(sourceField.getFieldX() + "," + sourceField.getFieldY());
        dragboard.setContent(content);

        // Set visual feedback
        figure.setEffect(DRAG_SHADOW);
        
        // Store drag state
        dragSourceField = sourceField;
        draggedFigure = figure;
    }

    private boolean canMovePiece(ChessFigure figure) {
        // TODO: Add logic to check if it's the current player's piece
        // and if it's their turn. For now, allow all moves.
        return true;
    }

    private boolean isValidDropTarget(FieldView targetField) {
        if (dragSourceField == null) {
            return false;
        }

        // Can't drop on the same field
        if (targetField == dragSourceField) {
            return false;
        }

        // Basic validation - more sophisticated validation will be done server-side
        return true;
    }

    private boolean executeMoveIfValid(FieldView sourceField, FieldView targetField) {
        try {
            // Convert coordinates to chess notation
            String fromNotation = convertCoordinatesToNotation(sourceField.getFieldX(), sourceField.getFieldY());
            String toNotation = convertCoordinatesToNotation(targetField.getFieldX(), targetField.getFieldY());
            
            // Create move string in format expected by server (e.g., "e2-e4")
            String moveString = fromNotation + "-" + toNotation;
            
            // Send move to server through view model
            boolean moveSuccessful = viewModel.makeMove(moveString);
            
            if (moveSuccessful) {
                // Update UI immediately for responsive feel
                updateBoardUI(sourceField, targetField);
            }
            
            return moveSuccessful;
            
        } catch (Exception e) {
            System.err.println("Error executing move: " + e.getMessage());
            return false;
        }
    }

    private void updateBoardUI(FieldView sourceField, FieldView targetField) {
        // Move the figure visually
        ChessFigure figure = sourceField.getFigure();
        if (figure != null) {
            sourceField.setFigure(null);  // Remove from source
            targetField.setFigure(figure);  // Add to target
        }
    }

    private String convertCoordinatesToNotation(int x, int y) {
        // Convert array coordinates to chess notation
        // x: 0-7 -> a-h, y: 0-7 -> 8-1 (board is flipped)
        char file = (char) ('a' + x);
        int rank = 8 - y;  // Flip y coordinate
        return file + String.valueOf(rank);
    }

    private void clearDragState() {
        dragSourceField = null;
        draggedFigure = null;
        
        // Clear effects from all fields
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                chessboardFields[row][col].setEffect(null);
            }
        }
    }

    /**
     * Update the board state from server response
     */
    public void updateBoardFromServer(de.chess.model.GameBoard gameBoard) {
        // Update the UI board to match the server state
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                FieldView field = chessboardFields[row][col];
                de.chess.model.ChessField serverField = gameBoard.getBoard()[row][col];
                
                if (serverField.getPiece() == null) {
                    field.setFigure(null);
                } else {
                    // Create figure from server piece data
                    ChessFigure figure = createFigureFromPiece(serverField.getPiece());
                    field.setFigure(figure);
                }
            }
        }
    }

    private ChessFigure createFigureFromPiece(de.chess.model.Piece piece) {
        de.chess.model.ChessColor color = piece.getColor();
        
        switch (piece.getPieceType()) {
            case PAWN:
                return new de.chess.fx.app.ui.views.figure.Pawn(color);
            case ROOK:
                return new de.chess.fx.app.ui.views.figure.Tower(color);
            case KNIGHT:
                return new de.chess.fx.app.ui.views.figure.Horse(color);
            case BISHOP:
                return new de.chess.fx.app.ui.views.figure.Bishop(color);
            case QUEEN:
                return new de.chess.fx.app.ui.views.figure.Queen(color);
            case KING:
                return new de.chess.fx.app.ui.views.figure.King(color);
            default:
                return null;
        }
    }
}