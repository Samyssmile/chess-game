package de.chess.fx.app.ui.command.menu;

import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.views.gameboard.GameView;
import javafx.scene.Scene;

import java.util.UUID;
import java.util.logging.Level;

public class ToJoinGameCommand implements ICommando {
    private final Scene scene;
    private final UUID gameUUID;

    public ToJoinGameCommand(Scene scene, UUID gameUUID) {
        this.scene = scene;
        this.gameUUID = gameUUID;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Join Game Command...");
        GameView gameView = new GameView();
        scene.setRoot(gameView);

    }
}
