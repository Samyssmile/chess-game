package de.chess.fx.app.ui.views.gameboard;

import de.chess.fx.app.client.GameClient;
import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.field.FieldView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.Collections;
import java.util.List;

public class GameBoardView extends GridPane implements UIView {


    private final SimpleObjectProperty<FieldView[][]> boardMatrix = new SimpleObjectProperty<>(new FieldView[8][8]);


    private GameBoardViewModel viewModel;

    public GameBoardView(GameClient gameClient) {
        initViewModel();
        viewModel.setGameClient(gameClient);
        viewModel.openGame();

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

        return Collections.emptyList();
    }


    @Override
    public void initActionsEvents() {
    }

    @Override
    public void initViewModel() {
        this.viewModel = new GameBoardViewModel();
    }
}
