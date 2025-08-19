package de.chess.fx.app.ui.views.maingameview;


import de.chess.dto.ChessGame;
import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.field.FieldView;
import de.chess.fx.app.ui.views.figure.*;
import de.chess.model.ChessColor;
import de.chess.model.GameBoard;
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

public class GameView extends BorderPane implements UIView {
    private static final double TITLE_FONT_SIZE = 55;
    private Label labelTitle;
    private Label labelPlayerHostName;
    private Label labelPlayerClientName;
    private Label labelPlayerHostElo;
    private Label labelPlayerVisitorElo;
    private Label labelCountDown;

    private Button buttonGiveUp;
    private Button buttonRemisRequest;

    private HBox topHBox;
    private HBox bottomHBox;
    private List<Node> nodeList;
    
    private GridPane chessboardGrid;
    private FieldView[][] chessboardFields;
    private GameBoard gameBoard;

    private GameViewModel viewModel;


    public GameView(ChessGame gameDTO) {
        this.nodeList = new ArrayList<>();
        initNodes();
        initChessboard();
        initViewModel();
        viewModel.setChessGame(gameDTO);

        this.topHBox = new HBox(labelPlayerHostName, labelTitle, labelPlayerClientName);
        this.bottomHBox = new HBox(buttonGiveUp, buttonRemisRequest);

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

        buttonGiveUp = new Button("I give up");
        buttonRemisRequest = new Button("Suggest Draw - Remis");

        nodeList.add(labelPlayerHostName);
        nodeList.add(labelPlayerClientName);
        nodeList.add(labelPlayerHostElo);
        nodeList.add(labelPlayerVisitorElo);
        nodeList.add(buttonGiveUp);
        nodeList.add(labelCountDown);
        nodeList.add(labelTitle);

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
    }

    @Override
    public void confugureView() {
        this.bottomHBox.setAlignment(Pos.CENTER);
        this.bottomHBox.setSpacing(10);
        this.bottomHBox.setPadding(new Insets(10));
        this.topHBox.setAlignment(Pos.CENTER);
        this.labelTitle.setFont(new Font(TITLE_FONT_SIZE));
        
        // Center the chessboard
        this.chessboardGrid.setAlignment(Pos.CENTER);
        this.chessboardGrid.setPadding(new Insets(20));
    }
    
    private void initChessboard() {
        this.gameBoard = new GameBoard();
        this.gameBoard.initialDistibutionOfChessPieces();
        
        this.chessboardGrid = new GridPane();
        this.chessboardFields = new FieldView[8][8];
        
        // Create 8x8 grid of FieldViews
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Create field with appropriate color (alternating pattern)
                ChessColor fieldColor = (row + col) % 2 == 0 ? ChessColor.WHITE : ChessColor.BLACK;
                
                // Get piece from game board if any
                ChessFigure figure = createFigureFromBoard(row, col);
                
                FieldView field = figure != null ? 
                    new FieldView(row, col, figure, fieldColor) : 
                    new FieldView(row, col);
                    
                chessboardFields[row][col] = field;
                chessboardGrid.add(field, col, row);
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


}
