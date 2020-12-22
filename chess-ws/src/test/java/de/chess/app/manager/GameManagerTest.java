package de.chess.app.manager;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.game.IGameManager;
import de.chess.model.ChessColor;
import de.chess.model.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private IGameManager gameManager;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        this.gameManager = GameManager.instance();

    }

    @Test
    void requestGame() {
        gameManager.reset();
        ChessGame randomGame = randomGame();
        assertNull(randomGame.getUuid());
        randomGame = this.gameManager.requestGame(randomGame());
        assertNotNull(randomGame.getUuid());
        assertTrue(this.gameManager.isGameExists(randomGame.getUuid()));
    }

    @Test
    void killGame() {
        List<ChessGame> runningGames = new ArrayList<>();
        gameManager.reset();
        int limit = ramdomGames().size();
        gameManager.setGameLimit(limit);
        ramdomGames().forEach(game -> {
            runningGames.add(gameManager.requestGame(randomGame()));
        });

        int numberOfGames = gameManager.numberOfRunningGames();
        assertTrue(numberOfGames == limit);
        ChessGame gameToKill = runningGames.get(0);
        assertTrue(gameManager.isGameExists(gameToKill.getUuid()));
        gameManager.killGame(gameToKill.getUuid());
        assertFalse(gameManager.isGameExists(gameToKill.getUuid()));
    }

    @Test
    void move() {

    }

    private ChessGame randomGame() {
        List<ChessGame> gameDTOS = ramdomGames();
        return ramdomGames().get(random.nextInt(gameDTOS.size()));
    }

    private List<ChessGame> ramdomGames() {
        List<ChessGame> preparedDummyGames = new ArrayList<>();
        preparedDummyGames.add(new ChessGame("Junut Test Game 1", new Player("Random Player", 1500), "10", ChessColor.BLACK, GameType.FUN));

        return preparedDummyGames;
    }
}