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

    public boolean isGameExists(UUID uuid);

    public int numberOfRunningGames();

    public List<ChessGame> getActiveGameList();

    /**
     * Delete all games.
     */
    public void reset();

    public int getGameLimit();

    public void setGameLimit(int gameLimit);

    public Optional<ChessGame> findGameByUUID(UUID gameUUID);

    Optional<ChessGame> requestToJoinGame(UUID gameUUID, Player player);

    /**
     * Find Game by UUID
     * @param gameUUID Game UUID
     * @return Optional of ChessGame, this is empty if no game found by given UUID.
     */
    Optional<ChessGame> getGameByUUIID(UUID gameUUID);
}

