package de.chess.fx.app.ui.views.maingameview;

import de.chess.fx.app.ui.views.field.FieldView;
import de.chess.fx.app.ui.views.figure.ChessFigure;
import de.chess.model.Move;
import de.chess.model.ChessColor;
import de.chess.fx.app.audio.AudioEffectPlayer;
import de.chess.fx.app.audio.AudioEffectType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * Handles drag and drop functionality for chess pieces on the board
 */
public class DragAndDropHandler {
    
    private final FieldView[][] chessboardFields;
    private final GameViewModel viewModel;
    private final AudioEffectPlayer audioPlayer;
    
    // Visual feedback for drag operations
    private static final DropShadow DRAG_SHADOW = new DropShadow(15, Color.GOLD);
    private static final DropShadow DROP_TARGET_SHADOW = new DropShadow(10, Color.LIGHTGREEN);
    private static final DropShadow INVALID_DROP_SHADOW = new DropShadow(10, Color.RED);
    private static final DropShadow VALID_MOVE_HIGHLIGHT = new DropShadow(8, Color.CYAN);
    
    // Drag state
    private FieldView dragSourceField = null;
    private ChessFigure draggedFigure = null;
    private List<FieldView> highlightedFields = new ArrayList<>();

    public DragAndDropHandler(FieldView[][] chessboardFields, GameViewModel viewModel) {
        this.chessboardFields = chessboardFields;
        this.viewModel = viewModel;
        this.audioPlayer = new AudioEffectPlayer();
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
                } else {
                    // Play "not allowed" sound for invalid piece selection
                    audioPlayer.playSound(AudioEffectType.NOT_ALLOWED);
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
                    if (success) {
                        audioPlayer.playSound(AudioEffectType.MOVE);
                    } else {
                        audioPlayer.playSound(AudioEffectType.NOT_ALLOWED);
                    }
                } else {
                    audioPlayer.playSound(AudioEffectType.NOT_ALLOWED);
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
        
        // Highlight valid moves
        highlightValidMoves(sourceField);
    }

    private boolean canMovePiece(ChessFigure figure) {
        // Check if game is running
        if (viewModel.getGameDTO() == null || 
            viewModel.getGameDTO().getCurrentTurn() == null) {
            return false;
        }
        
        // Check if it's the piece owner's turn
        ChessColor figureColor = figure.getColor();
        ChessColor currentTurn = viewModel.getGameDTO().getCurrentTurn();
        
        if (figureColor != currentTurn) {
            return false; // Not this piece's turn
        }
        
        // Check if this player controls this color
        ChessColor playerColor = viewModel.getCurrentUserColor();
        if (playerColor != figureColor) {
            return false; // Player doesn't control this piece
        }
        
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
            boolean requestSent = viewModel.makeMove(moveString);
            
            // DO NOT update UI immediately - wait for server response
            // The UI will be updated when MOVE_DONE event is received
            // Return true if request was sent, regardless of move validity
            
            return requestSent;
            
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
        // IMPORTANT: FieldView constructor is called with (row, col) but stored as (x, y)
        // So x = row index (0-7), y = col index (0-7)
        // We need: file = col (y), rank = 8 - row (8 - x)
        char file = (char) ('a' + y);  // y is the column index -> file (a-h)
        int rank = 8 - x;  // x is the row index -> rank (8-1)
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

    private void highlightValidMoves(FieldView sourceField) {
        clearHighlightedMoves();
        
        if (viewModel.getGameDTO() == null) {
            return;
        }
        
        ChessFigure selectedFigure = sourceField.getFigure();
        if (selectedFigure == null) {
            return;
        }
        
        // Simple highlighting - mark empty fields and opponent pieces as potential targets
        // The actual move validation will happen server-side
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                FieldView targetField = chessboardFields[row][col];
                
                // Skip the source field
                if (targetField == sourceField) {
                    continue;
                }
                
                // Highlight empty fields or fields with opponent pieces
                if (!targetField.isFigurePresent()) {
                    targetField.setEffect(VALID_MOVE_HIGHLIGHT);
                    highlightedFields.add(targetField);
                } else {
                    ChessFigure targetFigure = targetField.getFigure();
                    if (targetFigure.getColor() != selectedFigure.getColor()) {
                        // Highlight opponent pieces (potential captures)
                        targetField.setEffect(VALID_MOVE_HIGHLIGHT);
                        highlightedFields.add(targetField);
                    }
                }
            }
        }
    }
    
    private void clearHighlightedMoves() {
        for (FieldView field : highlightedFields) {
            field.setEffect(null);
        }
        highlightedFields.clear();
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

    /**
     * Refresh game state to update turn logic after server updates
     */
    public void refreshGameState() {
        // Clear any highlighted moves since game state has changed
        clearHighlightedMoves();
        
        // Clear drag state to prevent issues with stale references
        clearDragState();
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