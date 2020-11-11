package de.chess.fx.app.ui.views.join;

import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.command.ToJoinGameCommand;
import de.chess.fx.app.ui.command.ToMainMenuCommand;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;

public class JoinGameViewModel {


    public BooleanProperty isJoinButtonDisabled = new SimpleBooleanProperty(true);

    public ICommando getToMainMenuCommand(Scene scene) {
        return new ToMainMenuCommand(scene);
    }

    public ICommando getJoinGameCommand(Scene scene) {
        return new ToJoinGameCommand(scene);
    }
}
