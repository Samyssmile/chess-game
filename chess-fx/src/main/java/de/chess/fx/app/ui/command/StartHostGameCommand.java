package de.chess.fx.app.ui.command;

import de.chess.dto.ChessGame;
import de.chess.dto.request.OpenGameRequest;
import de.chess.fx.app.ui.views.gameboard.GameView;
import de.chess.io.client.GameClient;
import javafx.scene.Scene;

import java.util.Optional;
import java.util.logging.Level;

public class StartHostGameCommand implements ICommando {
    private final Scene scene;
    private final ChessGame gameDTO;

    public StartHostGameCommand(Scene scene, ChessGame gameDTO) {
        this.scene = scene;
        this.gameDTO = gameDTO;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Request To Host Game Command...");
        Optional<GameClient> clientOptional = GameClient.getInstance();
        if (clientOptional.isPresent()) {
            GameClient client = clientOptional.get();
            client.sendRequest(new OpenGameRequest(gameDTO));
        }


    }
}
