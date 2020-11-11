package de.chess.fx.app.ui.command;

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
        LOGGER.log(Level.SEVERE, "Enter Game Browser Command...");
        scene.setRoot(new JoinGameView());
    }
}
