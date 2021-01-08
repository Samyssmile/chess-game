package de.chess.fx.app.ui.views.gameboard;

import de.chess.dto.Player;
import de.chess.fx.app.handler.EventData;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.fx.app.handler.IChannel;
import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.field.FieldView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.Collections;
import java.util.List;

/** Represent a chess board. */
public class GameBoardView extends GridPane implements UIView, IChannel {

  private static final double SPACING = 10;
  private final SimpleObjectProperty<FieldView[][]> boardMatrix =
      new SimpleObjectProperty<>(new FieldView[8][8]);

  private GameBoardViewModel viewModel;

  public GameBoardView() {
    initViewModel();
    viewModel.openGame();

    initNodes();
    Bindings.bindBidirectional(boardMatrix, viewModel.boardMatrixProperty());
    confugureView();
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
  public void initActionsEvents() {}

  @Override
  public void initViewModel() {
    this.viewModel = new GameBoardViewModel();
  }

  @Override
  public void confugureView() {
    this.setPadding(new Insets(0, 0, 0, SPACING));
  }


  @Override
  public void update(EventType eventType, EventData eventData) {
    switch (eventType) {
      case PLAYER_JOINED:
        Player joinedPlayer = (Player) eventData.getData();
        System.out.println("Works");
        break;
    }
  }
}
