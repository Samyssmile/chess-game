package de.chess.dto.response;

import de.chess.dto.Player;
import de.chess.dto.RequestType;

import java.util.UUID;

public class PlayerJoinedIntoMyHostedGameResponse extends Response {

    private final Player player;

    public PlayerJoinedIntoMyHostedGameResponse(UUID gameUUID, RequestType requestType, Player player) {
        super(gameUUID, requestType);
        this.player = player; 
    }

    public Player getPlayer() {
        return player;
    }
}
