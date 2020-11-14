package de.chess.fx.app.ui.command;

import de.chess.fx.app.ui.views.host.HostGameView;
import javafx.scene.Scene;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ToHostGameCommand implements ICommando {
    private final Scene scene;

    public ToHostGameCommand(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Enter Host Game Menu Command...");
        scene.setRoot(new HostGameView());;
    }
}
