package de.chess.fx.app.ui.views.join;

import de.chess.fx.app.model.Game;
import de.chess.fx.app.provider.DummyProvider;
import de.chess.fx.app.provider.IGameListProvider;
import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.command.ToJoinGameCommand;
import de.chess.fx.app.ui.command.ToMainMenuCommand;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

public class JoinGameViewModel {


    private final ObservableList<Game> data;
    public ObjectProperty<Game> selectedGameProperty = new SimpleObjectProperty<>();

    public JoinGameViewModel() {
        IGameListProvider provider = new DummyProvider();
        data = FXCollections.observableArrayList(provider.receiveGameList());
        selectedGameProperty.addListener(new ChangeListener<Game>() {
            @Override
            public void changed(ObservableValue<? extends Game> observable, Game oldValue, Game newValue) {
                isJoinButtonDisabled.set(false);
            }
        });
    }

    public BooleanProperty isJoinButtonDisabled = new SimpleBooleanProperty(true);

    public ICommando getToMainMenuCommand(Scene scene) {
        return new ToMainMenuCommand(scene);
    }

    public ICommando getJoinGameCommand(Scene scene) {
        return new ToJoinGameCommand(scene);
    }

    public ObservableList<Game> getData() {
        return data;
    }
}
