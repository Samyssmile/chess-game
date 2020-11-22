package de.chess.fx.app.ui.command;

import de.chess.fx.app.ui.views.gameboard.GameView;
import javafx.scene.Scene;

import java.util.UUID;
import java.util.logging.Level;

public class ToJoinGameCommand implements ICommando{
    private final Scene scene;
    private final UUID gameUUID;

    public ToJoinGameCommand(Scene scene, UUID gameUUID) {
        this.scene = scene;
        this.gameUUID = gameUUID;
    }

    @Override
    public void execute() {
        GameView gameView = new GameView();
        scene.setRoot(gameView);

        LOGGER.log(Level.INFO, "Join Game Command...");
    }
}
