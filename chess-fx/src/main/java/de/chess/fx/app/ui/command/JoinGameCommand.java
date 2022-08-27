package de.chess.fx.app.ui.command;

import de.chess.dto.Player;
import de.chess.dto.RequestType;
import de.chess.dto.request.JoinGameRequest;
import de.chess.fx.app.ChessFX;
import de.chess.fx.app.model.GameRowData;
import de.chess.io.client.GameClient;
import javafx.scene.Scene;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public class JoinGameCommand implements ICommando{
    private final UUID gameUUID;
    private final Scene scene;
    private final GameRowData gameRowData;
    private final String nickname;


    public JoinGameCommand(GameRowData gameRowData, Scene scene, UUID gameUUID, String nickname) {
        this.gameRowData = gameRowData;
        this.scene = scene;
        this.gameUUID = gameUUID;
        this.nickname = nickname;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Join Game Command...");

        Optional<GameClient> clientOptional = GameClient.getInstance();
        if (clientOptional.isPresent()) {
            GameClient client = clientOptional.get();
            client.sendRequest(new JoinGameRequest(RequestType.JOIN, new Player(ChessFX.PLAYERS_UUID, nickname, 1500), gameRowData.getUuid()));
        }

    }
}
