package de.chess.fx.app.ui.command;

import de.chess.dto.RequestType;
import de.chess.dto.request.MoveRequest;
import de.chess.fx.app.ChessFX;
import de.chess.io.client.GameClient;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Command to send move requests to the server
 */
public class MoveCommand implements ICommando {
    
    private static final Logger LOGGER = Logger.getGlobal();
    
    private final UUID gameUUID;
    private final String move;

    public MoveCommand(UUID gameUUID, String move) {
        this.gameUUID = gameUUID;
        this.move = move;
    }

    @Override
    public void execute() {
        try {
            Optional<GameClient> clientOptional = GameClient.getInstance();
            if (clientOptional.isPresent()) {
                GameClient client = clientOptional.get();
                
                MoveRequest moveRequest = new MoveRequest(
                    gameUUID,
                    ChessFX.PLAYERS_UUID,
                    RequestType.MOVE,
                    move
                );
                
                LOGGER.info("Sending move request: " + move + " for game: " + gameUUID);
                client.sendRequest(moveRequest);
            } else {
                LOGGER.warning("GameClient not available - cannot send move request");
            }
        } catch (Exception e) {
            LOGGER.severe("Error sending move request: " + e.getMessage());
        }
    }
}