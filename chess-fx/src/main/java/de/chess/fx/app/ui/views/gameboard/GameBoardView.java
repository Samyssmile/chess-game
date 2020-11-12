package de.chess.fx.app.ui.views.gameboard;

import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.figure.ChessColor;
import de.chess.fx.app.ui.views.figure.ChessFigure;
import de.chess.fx.app.ui.views.figure.Pawn;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameBoardView extends GridPane implements UIView {

    private static final String COLOR_BLACK = "#D18B47";
    private static final String COLOR_WHITE = "#FFCE9E";
    private static final double PREFERED_FIELD_SIZE = 90;
    private static final Background BLACK_COLOR = new Background(new BackgroundFill(Color.web(COLOR_BLACK), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background WHITE_COLOR = new Background(new BackgroundFill(Color.web(COLOR_WHITE), CornerRadii.EMPTY, Insets.EMPTY));
    private final Pane[][] boardMatrix;
    private final ChessFigure[][] figureMatrix;

    private GameBoardViewModel viewModel;

    public GameBoardView() {
        initViewModel();

        boardMatrix = new Pane[8][8];
        initNodes();
        figureMatrix = viewModel.getFigureMatrix();
        placeFigures();
    }



    private void placeFigures() {
        for (int i = 0; i < figureMatrix.length; i++) {
            for (int y = 0; y < figureMatrix[i].length; y++) {
                if (figureMatrix[i][y] != null){
                    figureMatrix[i][y].prefHeight(PREFERED_FIELD_SIZE);
                    figureMatrix[i][y].prefWidth(PREFERED_FIELD_SIZE);
                    figureMatrix[i][y].setFitHeight(PREFERED_FIELD_SIZE);
                    figureMatrix[i][y].setFitWidth(PREFERED_FIELD_SIZE);
                    boardMatrix[i][y].getChildren().add(figureMatrix[i][y]);

                }
            }
        }
    }


    @Override
    public List<Node> initNodes() {
        List<Node> nodeList = new ArrayList<>();
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int y = 0; y < boardMatrix[i].length; y++) {
                Pane pane = new Pane();

                boardMatrix[i][y] = pane;
                nodeList.add(pane);
                pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                if ((i % 2) == 0) {
                    if ((y % 2) != 0) {
                        pane.setBackground(BLACK_COLOR);
                    } else {
                        pane.setBackground(WHITE_COLOR);
                    }
                } else {
                    if ((y % 2) == 0) {
                        pane.setBackground(BLACK_COLOR);
                    } else {
                        pane.setBackground(WHITE_COLOR);
                    }
                }
                pane.setPrefWidth(PREFERED_FIELD_SIZE);
                pane.setPrefHeight(PREFERED_FIELD_SIZE);
                this.add(pane, i, y);
            }

        }
        return nodeList;
    }


    @Override
    public void initActionsEvents() {

    }

    @Override
    public void initViewModel() {
      this.viewModel = new GameBoardViewModel();
    }
}
