package de.chess.io.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class PlayerManager {
  private static final Logger LOGGER = Logger.getGlobal();
  private List<PlayerConnection> playerList = new ArrayList<>();

  private static PlayerManager instance = null;

  private PlayerManager() {}

  public static PlayerManager getInstance() {
    if (instance == null) {
      instance = new PlayerManager();
    }
    return instance;
  }

  public Optional<PlayerConnection> getConnectionByPlayerUUID(UUID playerUUID) {
    Optional<PlayerConnection> result = playerList.stream().filter(e -> e.getPlayerUUID().equals(playerUUID)).findFirst();
    return result;
  }

  public void addPlayerConnection(PlayerConnection playerConnection) {
    playerList.add(playerConnection);
    LOGGER.info("New Player Connection added "+playerConnection.getPlayerUUID());
  }

  public boolean removePlayerConnectionByPlayerUUID(UUID playerUUID) {
    boolean removed = playerList.removeIf(e -> e.getPlayerUUID().equals(playerUUID));
    LOGGER.info("Player Connection removed");
    return removed;
  }
}
