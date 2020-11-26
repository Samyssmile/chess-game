package de.chess.fx.app.ui.command.menu;

import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.views.join.JoinGameView;
import javafx.scene.Scene;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ToJoinMenuCommand implements ICommando {
    private final Scene scene;

    public ToJoinMenuCommand(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Enter Game Browser Command...");
        scene.setRoot(new JoinGameView());
    }
}
