package de.chess.app.manager;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.game.IGameManager;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class GameManager implements IGameManager {
    public static final Logger LOGGER = Logger.getGlobal();
    private static IGameManager instance = null;
    private final List<ChessGame> activeGameList = new ArrayList<>();
    private int gameLimit = 10;


    private GameManager() {
    }

    public static IGameManager instance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    @Override
    public ChessGame requestGame(ChessGame gameDTO) {
        ChessGame chessGame = gameDTO;
        if (gameLimitNotReached()) {
            chessGame.setUuid(generateUUID());
            chessGame.initGameBoard();
            activeGameList.add(chessGame);
            LOGGER.log(Level.INFO, "Requested Game Granted: GameList Size: {0}", activeGameList.size());
        } else {
            chessGame = null;
            LOGGER.warning("Limit for concurent running games reached");
        }
        return chessGame;
    }


    @Override
    public void killGame(UUID uuid) {
        LOGGER.info("Kill Game: %s%s".formatted(uuid, !isGameExists(uuid)));
        removeGame(activeGameList, uuid);
    }

    @Override
    public boolean isGameExists(UUID uuid) {
        return IntStream.range(0, activeGameList.size()).anyMatch(i -> Objects.equals(uuid, activeGameList.get(i).getUuid()));
    }

    @Override
    public int numberOfRunningGames() {
        return activeGameList.size();
    }

    @Override
    public void reset() {
        activeGameList.clear();
    }

    @Override
    public int getGameLimit() {
        return gameLimit;
    }

    @Override
    public void setGameLimit(int gameLimit) {
        this.gameLimit = gameLimit;
    }

    @Override
    public boolean joinGameRequest(Player player, UUID gameUUID) {
        Optional<ChessGame> chessGameOptional = findGameByUUID(gameUUID);
        boolean result = false;

        if (chessGameOptional.isPresent() && chessGameOptional.get().isWaitingForPlayerToJoin()) {
            ChessGame chessGame = chessGameOptional.get();
            chessGame.setClientPlayer(player);
            result = true;
        }

        return result;
    }

    public Optional<ChessGame> findGameByUUID(UUID gameUUID) {
        LOGGER.info("Searching for Game %s as a Player requested to join".formatted(gameUUID));
        Optional<ChessGame> chessGameOptional = getActiveGameList().stream().filter(chessGame -> chessGame.getUuid().equals(gameUUID)).findFirst();
        return chessGameOptional;
    }

    @Override
    public Optional<ChessGame> requestToJoinGame(UUID gameUUID, Player player) {
        Optional<ChessGame> gameOfInteresst = getGameByUUIID(gameUUID);
        gameOfInteresst.ifPresentOrElse(e -> {
            if(e.isWaitingForPlayerToJoin()){
                e.setClientPlayer(player);
            }else{
                LOGGER.info("Game already running");
                e = null;
            }
        }, () -> {
            LOGGER.info("Requested Game not found.");
        });

        return gameOfInteresst;

    }

    @Override
    public Optional<ChessGame> getGameByUUIID(UUID gameUUID) {
        return activeGameList.stream().filter(e -> e.getUuid().equals(gameUUID)).findFirst();
    }

    public boolean move(UUID uuid, String move) {

        return false;
    }


    private boolean gameLimitNotReached() {
        return activeGameList.size() < gameLimit;
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }

    @Override
    public List<ChessGame> getActiveGameList() {
        return new ArrayList<>(activeGameList);
    }

    private void removeGame(List<ChessGame> list, UUID element) {
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(element, list.get(i).getUuid())) {
                list.remove(i);
            }
        }
    }


}
