package de.chess.fx.app.ui.command;

import de.chess.dto.ChessGame;
import de.chess.fx.app.ui.views.gameboard.GameView;
import javafx.scene.Scene;

import java.util.logging.Level;

public class StartGameCommand implements ICommando {
    private final Scene scene;
    private final ChessGame gameDTO;

    public StartGameCommand(Scene scene, ChessGame gameDTO) {
        this.scene = scene;
        this.gameDTO = gameDTO;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Start Game Command...");
        GameView gameView = new GameView(gameDTO);
        scene.setRoot(gameView);
    }
}
