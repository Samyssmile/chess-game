package de.chess.app.manager;

import de.chess.dto.GameDTO;

import java.util.List;
import java.util.UUID;

public interface IGameManager {

    public void killGame(UUID uuid);
    public boolean move(UUID uuid, String move);
    public GameDTO requestGame(GameDTO gameDTO);
    public boolean isGameExists(UUID uuid) ;
    public int numberOfRunningGames();
    public List<GameDTO> getActiveGameList();
    /**
     * Delete all games.
     */
    public void reset();
    public int getGameLimit();
    public void setGameLimit(int gameLimit);
}

