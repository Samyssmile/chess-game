package de.chess.fx.app.ui.command;

import de.chess.dto.ChessGame;
import de.chess.fx.app.ui.views.gameboard.GameView;
import javafx.scene.Scene;

import java.util.UUID;
import java.util.logging.Level;

public class ToJoinGameCommand implements ICommando{
    private final Scene scene;
    private final UUID gameUUID;
    private final ChessGame chessGame;

    public ToJoinGameCommand(Scene scene, UUID gameUUID, ChessGame chessGame) {
        this.scene = scene;
        this.gameUUID = gameUUID;
        this.chessGame = chessGame;
    }

    @Override
    public void execute() {
        GameView gameView = new GameView(chessGame);
        scene.setRoot(gameView);

        LOGGER.log(Level.INFO, "Join Game Command...");
    }
}
