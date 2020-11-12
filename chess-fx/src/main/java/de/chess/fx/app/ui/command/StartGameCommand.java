package de.chess.fx.app.ui.command;

import de.chess.fx.app.ui.views.gameboard.GameBoardView;
import de.chess.fx.app.ui.views.host.HostGameView;
import javafx.scene.Scene;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StartGameCommand implements ICommando{
    private final Scene scene;

    public StartGameCommand(Scene scene) {
        this.scene = scene;
    }
    @Override
    public void execute() {
        LOGGER.log(Level.SEVERE, "Start Game Command...");
        scene.setRoot(new GameBoardView());;
    }
}
