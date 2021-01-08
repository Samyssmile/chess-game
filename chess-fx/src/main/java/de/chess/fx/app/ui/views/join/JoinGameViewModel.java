package de.chess.fx.app.ui.views.join;

import de.chess.dto.ChessGame;
import de.chess.fx.app.model.GameRowData;
import de.chess.fx.app.provider.GameListProvider;
import de.chess.fx.app.provider.IGameListProvider;
import de.chess.fx.app.provider.NicknameProvider;
import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.command.RefreshGameListCommand;
import de.chess.fx.app.ui.command.JoinGameCommand;
import de.chess.fx.app.ui.command.menu.ToMainMenuCommand;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

import java.util.List;
import java.util.concurrent.Flow;

public class JoinGameViewModel implements Flow.Subscriber<List<ChessGame>> {

  private final ObservableList<GameRowData> data;
  public ObjectProperty<GameRowData> selectedGameProperty = new SimpleObjectProperty<>();
  private NicknameProvider nicknameProvider = new NicknameProvider();
  public StringProperty nicknameProperty =
      new SimpleStringProperty(nicknameProvider.getDefaultRandomNickname());
  private IGameListProvider provider = GameListProvider.getInstance();
  private Flow.Subscription subscription;

  public JoinGameViewModel() {
    provider.subscribe(this);

    data = FXCollections.observableArrayList(provider.receiveGameList());

    selectedGameProperty.addListener(
        new ChangeListener<GameRowData>() {
          @Override
          public void changed(
              ObservableValue<? extends GameRowData> observable,
              GameRowData oldValue,
              GameRowData newValue) {
            isJoinButtonDisabled.set(false);
          }
        });

    nicknameProperty.addListener(
        new ChangeListener<String>() {
          @Override
          public void changed(
              ObservableValue<? extends String> observable, String oldValue, String newValue) {
              System.out.println("Nickname Changed to: " + newValue);
          }
        });
  }

  public BooleanProperty isJoinButtonDisabled = new SimpleBooleanProperty(true);

  public ICommando getToMainMenuCommand(Scene scene) {
    return new ToMainMenuCommand(scene);
  }

  public ICommando getJoinGameCommand(Scene scene, GameRowData gameRowData) {
    return new JoinGameCommand(
        gameRowData, scene, selectedGameProperty.get().getUuid(), nicknameProperty.get());
  }

  public ICommando getRefreshGameListCommand() {
    return new RefreshGameListCommand();
  }

  public ObservableList<GameRowData> getData() {
    return data;
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(1);
  }

  @Override
  public void onNext(List<ChessGame> gameList) {
    data.clear();
    for (ChessGame gameDTO : gameList) {
      GameRowData gameRowData =
          new GameRowData(
              gameDTO.getUuid(),
              gameDTO.getHostPlayerName(),
              gameDTO.getGameName(),
              gameDTO.getHostColor(),
              gameDTO.getTimeLimit());
      data.add(gameRowData);
    }
    this.subscription.request(1);
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println("ERROR");
  }

  @Override
  public void onComplete() {
    System.out.println("Publishing onComplete");
  }
}
