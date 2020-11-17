package de.chess.fx.app.ui.command;

import de.chess.dto.GameDTO;
import de.chess.fx.app.client.GameClient;
import de.chess.fx.app.ui.views.gameboard.GameBoardView;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import java.util.logging.Level;

public class StartGameCommand implements ICommando {
    private final Scene scene;
    private final GameDTO gameDTO;

    public StartGameCommand(Scene scene, GameDTO gameDTO) {
        this.scene = scene;
        this.gameDTO = gameDTO;

    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Start Game Command...");



        GameBoardView gameBoardView = new GameBoardView(new GameClient(gameDTO));

        gameBoardView.setAlignment(Pos.CENTER);
        scene.setRoot(gameBoardView);
    }
}
