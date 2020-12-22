package de.chess.fx.app.ui.command.menu;

import de.chess.dto.Player;
import de.chess.dto.RequestType;
import de.chess.dto.request.JoinGameRequest;
import de.chess.dto.request.ReceiveGameListRequest;
import de.chess.fx.app.model.GameRowData;
import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.views.gameboard.GameView;
import de.chess.io.client.GameClient;
import javafx.scene.Scene;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public class ToJoinGameCommand implements ICommando {
    private final UUID gameUUID;
    private final Scene scene;
    private final GameRowData gameRowData;


    public ToJoinGameCommand(GameRowData gameRowData, Scene scene, UUID gameUUID) {
        this.gameRowData = gameRowData;
        this.scene = scene;
        this.gameUUID = gameUUID;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Join Game Command...");

        Optional<GameClient> clientOptional = GameClient.getInstance();
        if (clientOptional.isPresent()) {
            GameClient client = clientOptional.get();
            client.sendRequest(new JoinGameRequest(RequestType.JOIN, new Player(UUID.randomUUID(), "Jon Snow", 1500), gameRowData.getUuid()));
        }


/*        GameView gameView = new GameView();
        scene.setRoot(gameView);*/

    }
}
