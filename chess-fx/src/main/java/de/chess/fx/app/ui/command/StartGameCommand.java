package de.chess.fx.app.ui.command;

import de.chess.fx.app.ui.views.gameboard.GameBoardView;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import java.util.logging.Level;

public class StartGameCommand implements ICommando{
    private final Scene scene;

    public StartGameCommand(Scene scene) {
        this.scene = scene;
    }
    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Start Game Command...");
        GameBoardView gameBoardView = new GameBoardView();

        gameBoardView.setAlignment(Pos.CENTER);
        scene.setRoot(gameBoardView);
    }
}
