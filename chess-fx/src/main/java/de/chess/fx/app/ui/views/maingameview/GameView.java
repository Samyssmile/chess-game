package de.chess.fx.app.ui.views.maingameview;


import de.chess.dto.ChessGame;
import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.field.FieldView;
import de.chess.fx.app.ui.views.figure.*;
import de.chess.model.ChessColor;
import de.chess.model.GameBoard;
import de.chess.fx.app.handler.EventData;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.fx.app.handler.IChannel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GameView extends BorderPane implements UIView, IChannel {
    private static final double TITLE_FONT_SIZE = 55;
    private Label labelTitle;
    private Label labelPlayerHostName;
    private Label labelPlayerClientName;
    private Label labelPlayerHostElo;
    private Label labelPlayerVisitorElo;
    private Label labelCountDown;
    private Label labelTurnIndicator;
    private Label labelGameStatus;

    private Button buttonGiveUp;
    private Button buttonRemisRequest;

    private HBox topHBox;
    private HBox bottomHBox;
    private List<Node> nodeList;
    
    private GridPane chessboardGrid;
    private FieldView[][] chessboardFields;
    private GameBoard gameBoard;
    private DragAndDropHandler dragAndDropHandler;

    private GameViewModel viewModel;


    public GameView(ChessGame gameDTO, boolean isHost) {
        this.nodeList = new ArrayList<>();
        initNodes();
        initViewModel();
        viewModel.setChessGame(gameDTO);
        viewModel.setCurrentUserAsHost(isHost);
        initChessboard();
        initDragAndDrop();
        registerForEvents();

        this.topHBox = new HBox(labelPlayerHostName, labelTitle, labelPlayerClientName);
        this.bottomHBox = new HBox(buttonGiveUp, buttonRemisRequest, labelTurnIndicator, labelGameStatus);

        this.setTop(topHBox);
        this.setCenter(chessboardGrid);
        this.setBottom(this.bottomHBox);

        confugureView();
    }


    @Override
    public List<Node> initNodes() {
        labelTitle = new Label(" - vs - ");
        labelPlayerHostName = new Label();
        labelPlayerClientName = new Label();
        labelPlayerHostElo = new Label("1800");
        labelPlayerVisitorElo = new Label("1566");
        labelCountDown = new Label("3.2.1.GO");
        labelTurnIndicator = new Label("White to move");
        labelGameStatus = new Label("Game in progress");

        buttonGiveUp = new Button("I give up");
        buttonRemisRequest = new Button("Suggest Draw - Remis");

        nodeList.add(labelPlayerHostName);
        nodeList.add(labelPlayerClientName);
        nodeList.add(labelPlayerHostElo);
        nodeList.add(labelPlayerVisitorElo);
        nodeList.add(buttonGiveUp);
        nodeList.add(labelCountDown);
        nodeList.add(labelTitle);
        nodeList.add(labelTurnIndicator);
        nodeList.add(labelGameStatus);

        return nodeList;
    }

    @Override
    public void initActionsEvents() {

    }

    @Override
    public void initViewModel() {
        this.viewModel = new GameViewModel();

        labelPlayerHostName.textProperty().bindBidirectional(viewModel.hostPlayerNameProperty());
        labelPlayerClientName.textProperty().bindBidirectional(viewModel.clientPlayerNameProperty());
        labelTurnIndicator.textProperty().bindBidirectional(viewModel.turnIndicatorProperty());
        labelGameStatus.textProperty().bindBidirectional(viewModel.gameStatusProperty());
    }

    @Override
    public void confugureView() {
        this.bottomHBox.setAlignment(Pos.CENTER);
        this.bottomHBox.setSpacing(10);
        this.bottomHBox.setPadding(new Insets(10));
        this.topHBox.setAlignment(Pos.CENTER);
        this.labelTitle.setFont(new Font(TITLE_FONT_SIZE));
        
        // Center the chessboard and make it responsive
        this.chessboardGrid.setAlignment(Pos.CENTER);
        this.chessboardGrid.setPadding(new Insets(20));
        
        // Make the chessboard responsive to window resizing
        setupResponsiveChessboard();
    }
    
    private void initChessboard() {
        // Use the GameBoard from the ViewModel (server data) if available
        if (viewModel != null && viewModel.getGameDTO() != null && viewModel.getGameDTO().getGameBoard() != null) {
            this.gameBoard = viewModel.getGameDTO().getGameBoard();
        } else {
            // Fallback: create local board for initial display
            this.gameBoard = new GameBoard();
            this.gameBoard.initialDistibutionOfChessPieces();
        }
        
        this.chessboardGrid = new GridPane();
        this.chessboardFields = new FieldView[8][8];
        
        // Check if board should be rotated for black player
        boolean shouldRotate = viewModel != null && viewModel.shouldRotateBoard();
        
        // Create 8x8 grid of FieldViews
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Calculate display positions - rotate if playing as black
                int displayRow = shouldRotate ? (7 - row) : row;
                int displayCol = shouldRotate ? (7 - col) : col;
                
                // Create field with appropriate color (alternating pattern)
                ChessColor fieldColor = (row + col) % 2 == 0 ? ChessColor.WHITE : ChessColor.BLACK;
                
                // Get piece from game board if any
                ChessFigure figure = createFigureFromBoard(row, col);
                
                FieldView field = figure != null ? 
                    new FieldView(row, col, figure, fieldColor) : 
                    new FieldView(row, col);
                    
                chessboardFields[row][col] = field;
                // Add to grid using display coordinates for rotation
                chessboardGrid.add(field, displayCol, displayRow);
            }
        }
    }
    
    private ChessFigure createFigureFromBoard(int row, int col) {
        de.chess.model.ChessField chessField = gameBoard.getBoard()[row][col];
        if (chessField.getPiece() == null) {
            return null;
        }
        
        de.chess.model.Piece piece = chessField.getPiece();
        ChessColor color = piece.getColor();
        
        switch (piece.getPieceType()) {
            case PAWN:
                return new Pawn(color);
            case ROOK:
                return new Tower(color);
            case KNIGHT:
                return new Horse(color);
            case BISHOP:
                return new Bishop(color);
            case QUEEN:
                return new Queen(color);
            case KING:
                return new King(color);
            default:
                return null;
        }
    }
    
    private void setupResponsiveChessboard() {
        // Listen to window size changes and adjust board accordingly
        this.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        // Update chessboard size when window size changes
                        newWindow.widthProperty().addListener((w_obs, oldWidth, newWidth) -> updateChessboardSize());
                        newWindow.heightProperty().addListener((h_obs, oldHeight, newHeight) -> updateChessboardSize());
                    }
                });
            }
        });
    }
    
    private void updateChessboardSize() {
        // This method will be called when window is resized
        // For now, we keep the fixed size but this can be enhanced later
        // to dynamically calculate field size based on available space
    }

    private void initDragAndDrop() {
        if (chessboardFields != null && viewModel != null) {
            dragAndDropHandler = new DragAndDropHandler(chessboardFields, viewModel);
        }
    }

    /**
     * Get the drag and drop handler for external access if needed
     */
    public DragAndDropHandler getDragAndDropHandler() {
        return dragAndDropHandler;
    }

    private void registerForEvents() {
        EventHandler.getInstance().registerForEvent(this, EventType.GAME_STARTED);
        EventHandler.getInstance().registerForEvent(this, EventType.MOVE_DONE);
    }

    @Override
    public void update(EventType eventType, EventData eventData) {
        switch (eventType) {
            case GAME_STARTED:
                Platform.runLater(() -> {
                    // Update the game data and refresh the board
                    ChessGame updatedGame = (ChessGame) eventData.getData();
                    if (updatedGame != null) {
                        viewModel.setChessGame(updatedGame);
                        refreshChessboard();
                    }
                });
                break;
            case MOVE_DONE:
                Platform.runLater(() -> {
                    // Update the game data and refresh the board
                    ChessGame updatedGame = (ChessGame) eventData.getData();
                    if (updatedGame != null) {
                        viewModel.setChessGame(updatedGame);
                        refreshChessboard();
                    }
                });
                break;
        }
    }

    private void refreshChessboard() {
        if (viewModel.getGameDTO() != null && viewModel.getGameDTO().getGameBoard() != null) {
            this.gameBoard = viewModel.getGameDTO().getGameBoard();
            
            // Update all field views with current board state
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    FieldView field = chessboardFields[row][col];
                    ChessFigure figure = createFigureFromBoard(row, col);
                    field.setFigure(figure);
                }
            }
            
            // Refresh drag and drop handler to update turn logic
            if (dragAndDropHandler != null) {
                dragAndDropHandler.refreshGameState();
            }
        }
    }
}
