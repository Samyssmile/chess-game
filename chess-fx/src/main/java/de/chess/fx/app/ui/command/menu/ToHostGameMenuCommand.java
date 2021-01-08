package de.chess.fx.app.ui.command.menu;

import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.views.host.HostGameView;
import javafx.scene.Scene;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ToHostGameMenuCommand implements ICommando {
    private final Scene scene;

    public ToHostGameMenuCommand(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Enter Host Game Menu Command...");
        scene.setRoot(new HostGameView());;
    }
}
