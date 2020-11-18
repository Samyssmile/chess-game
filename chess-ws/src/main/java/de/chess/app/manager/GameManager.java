package de.chess.app.manager;

import de.chess.dto.GameDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class GameManager implements IGameManager {
    public static final Logger LOGGER = Logger.getGlobal();
    private static IGameManager instance = null;
    private List<GameDTO> activeGameList = new ArrayList<>();
    private int gameLimit = 2;


    private GameManager() {
    }

    public static IGameManager instance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    @Override
    public GameDTO requestGame(GameDTO gameDTO) {
        GameDTO result = null;
        if (gameLimitNotReached()) {
            gameDTO.setUuid(generateUUID());
            activeGameList.add(gameDTO);
            result = gameDTO;
        }else{
            LOGGER.warning("Limit for concurent running games reached");
        }
        return result;
    }


    @Override
    public void killGame(UUID uuid) {
        LOGGER.info("Kill Game: " + uuid + !isGameExists(uuid));
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
    public boolean move(UUID uuid, String move) {
        if (isGameExists(uuid)) {

        }
        return false;
    }


    private boolean gameLimitNotReached() {
        return activeGameList.size() < gameLimit;
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }



    private void removeGame(List<GameDTO> list, UUID element) {
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(element, list.get(i).getUuid())) {
                list.remove(i);
            }
        }
    }


}
