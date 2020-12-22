package de.chess.game;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGameManager {

    public void killGame(UUID uuid);
    public boolean move(UUID uuid, String move);
    public ChessGame requestGame(ChessGame gameDTO);
    public boolean isGameExists(UUID uuid) ;
    public int numberOfRunningGames();
    public List<ChessGame> getActiveGameList();
    /**
     * Delete all games.
     */
    public void reset();
    public int getGameLimit();
    public void setGameLimit(int gameLimit);

    boolean joinGameRequest(Player player, UUID gameUUID);
    public Optional<ChessGame> findGameByUUID(UUID gameUUID);
}

