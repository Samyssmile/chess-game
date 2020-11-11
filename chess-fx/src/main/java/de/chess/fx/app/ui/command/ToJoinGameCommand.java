package de.chess.fx.app.ui.command;

import javafx.scene.Scene;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ToJoinGameCommand implements ICommando{
    private final Scene scene;

    public ToJoinGameCommand(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.SEVERE, "Join Game Command...");
    }
}
