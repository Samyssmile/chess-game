package de.chess.fx.app.ui.views.gameboard;

import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.field.FieldView;
import de.chess.fx.app.ui.views.figure.ChessColor;
import de.chess.fx.app.ui.views.figure.ChessFigure;
import de.chess.fx.app.ui.views.figure.Pawn;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
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


    private SimpleObjectProperty<FieldView[][]> boardMatrix = new SimpleObjectProperty<>(new FieldView[8][8]);


    private GameBoardViewModel viewModel;

    public GameBoardView() {
        initViewModel();
        initNodes();
        Bindings.bindBidirectional(boardMatrix, viewModel.boardMatrixProperty());
    }


    @Override
    public List<Node> initNodes() {
        for (int i = 0; i < viewModel.getBoardMatrix().length; i++) {
            for (int y = 0; y < viewModel.getBoardMatrix()[i].length; y++) {

                this.add(viewModel.getBoardMatrix()[i][y], i, y);

            }
        }

        return null;
    }


    @Override
    public void initActionsEvents() {

    }

    @Override
    public void initViewModel() {
        this.viewModel = new GameBoardViewModel();
    }
}
