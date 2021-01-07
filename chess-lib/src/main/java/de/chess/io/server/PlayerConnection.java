package de.chess.io.server;

import java.util.UUID;

public class PlayerConnection {

    private UUID playerUUID;
    private ClientThread clientThread;

    public PlayerConnection(UUID playerUUID, ClientThread clientThread) {
        this.playerUUID = playerUUID;
        this.clientThread = clientThread;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public ClientThread getClientThread() {
        return clientThread;
    }

    public void setClientThread(ClientThread clientThread) {
        this.clientThread = clientThread;
    }
}
