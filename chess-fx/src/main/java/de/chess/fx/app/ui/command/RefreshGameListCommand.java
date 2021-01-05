package de.chess.fx.app.ui.command;

import de.chess.dto.request.ReceiveGameListRequest;
import de.chess.fx.app.ChessFX;
import de.chess.io.client.GameClient;

import java.util.Optional;

public class RefreshGameListCommand implements  ICommando {

    @Override
    public void execute() {
        Optional<GameClient> clientOptional = GameClient.getInstance();
        if (clientOptional.isPresent()) {
            GameClient client = clientOptional.get();
            client.sendRequest(new ReceiveGameListRequest(ChessFX.PLAYERS_UUID));
        }
    }
}
