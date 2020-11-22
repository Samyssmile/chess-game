package de.chess.dto.request;

import de.chess.dto.Player;
import de.chess.dto.RequestType;

import java.util.UUID;

public class JoinGameRequest extends Request {

    private Player player;

    public JoinGameRequest(RequestType requestType, Player player, UUID gameUUID) {
        super(gameUUID, requestType);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "JoinGameRequest{" +
                "player=" + player +
                '}';
    }

}
