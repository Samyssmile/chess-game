package de.chess.app.game;

import de.chess.dto.ChessGame;
import de.chess.dto.GameStatus;
import de.chess.dto.Player;
import de.chess.game.IGameManager;
import de.chess.model.ChessColor;
import de.chess.model.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test game joining functionality
 */
class JoinGameTest {

    private IGameManager gameManager;
    private Player hostPlayer;
    private Player clientPlayer;

    @BeforeEach
    void setUp() {
        gameManager = GameManager.instance();
        gameManager.reset();

        hostPlayer = new Player(UUID.randomUUID(), "HostPlayer", 1200);
        clientPlayer = new Player(UUID.randomUUID(), "ClientPlayer", 1300);
    }

    @Test
    @DisplayName("Host should be able to create a game")
    void testHostCanCreateGame() {
        // Create game
        ChessGame newGame = new ChessGame("TestGame", hostPlayer, "10", ChessColor.WHITE, GameType.FUN);
        ChessGame createdGame = gameManager.requestGame(newGame);
        
        assertNotNull(createdGame, "Game should be created");
        assertNotNull(createdGame.getUuid(), "Game should have UUID");
        assertEquals(GameStatus.WATING, createdGame.getGameStatus(), "New game should be waiting");
        assertEquals(hostPlayer, createdGame.getHostPlayer(), "Host player should be set");
        assertNull(createdGame.getClientPlayer(), "Client player should be null initially");
        
        System.out.println("Created game: " + createdGame.getUuid());
        System.out.println("Game status: " + createdGame.getGameStatus());
    }

    @Test
    @DisplayName("Second player should be able to join game")
    void testClientCanJoinGame() {
        // Create game first
        ChessGame newGame = new ChessGame("TestGame", hostPlayer, "10", ChessColor.WHITE, GameType.FUN);
        ChessGame createdGame = gameManager.requestGame(newGame);
        
        System.out.println("Game before join: " + createdGame.getGameStatus());
        
        // Second player joins
        Optional<ChessGame> joinedGameOpt = gameManager.requestToJoinGame(createdGame.getUuid(), clientPlayer);
        
        assertTrue(joinedGameOpt.isPresent(), "Client should be able to join game");
        
        ChessGame joinedGame = joinedGameOpt.get();
        assertEquals(GameStatus.READY_TO_START, joinedGame.getGameStatus(), "Game should be ready to start after join");
        assertEquals(hostPlayer, joinedGame.getHostPlayer(), "Host player should remain");
        assertEquals(clientPlayer, joinedGame.getClientPlayer(), "Client player should be set");
        
        System.out.println("Game after join: " + joinedGame.getGameStatus());
        System.out.println("Host: " + joinedGame.getHostPlayer().getNickname());
        System.out.println("Client: " + joinedGame.getClientPlayer().getNickname());
    }

    @Test
    @DisplayName("Game should initialize correctly when started")
    void testGameInitialization() {
        // Create and join game
        ChessGame newGame = new ChessGame("TestGame", hostPlayer, "10", ChessColor.WHITE, GameType.FUN);
        ChessGame createdGame = gameManager.requestGame(newGame);
        Optional<ChessGame> joinedGameOpt = gameManager.requestToJoinGame(createdGame.getUuid(), clientPlayer);
        
        assertTrue(joinedGameOpt.isPresent());
        ChessGame game = joinedGameOpt.get();
        
        // Start the game
        game.setGameStatus(GameStatus.RUNNING);
        game.initGameBoard();
        
        // Verify initialization
        assertEquals(GameStatus.RUNNING, game.getGameStatus());
        assertEquals(ChessColor.WHITE, game.getCurrentTurn(), "White should start");
        assertNotNull(game.getGameBoard(), "Game board should be initialized");
        
        // Test player colors
        assertEquals(ChessColor.WHITE, game.getHostColor(), "Host should be white");
        assertEquals(ChessColor.BLACK, game.getClientColor(), "Client should be black");
        
        System.out.println("Game running: " + game.getGameStatus());
        System.out.println("Current turn: " + game.getCurrentTurn());
        System.out.println("Host color: " + game.getHostColor());
        System.out.println("Client color: " + game.getClientColor());
    }
    
    @Test
    @DisplayName("Multiple games should work independently")
    void testMultipleGames() {
        // Create first game
        ChessGame game1 = new ChessGame("Game1", hostPlayer, "10", ChessColor.WHITE, GameType.FUN);
        ChessGame created1 = gameManager.requestGame(game1);
        
        // Create second game with different players
        Player host2 = new Player(UUID.randomUUID(), "Host2", 1400);
        ChessGame game2 = new ChessGame("Game2", host2, "10", ChessColor.BLACK, GameType.FUN);
        ChessGame created2 = gameManager.requestGame(game2);
        
        assertNotEquals(created1.getUuid(), created2.getUuid(), "Games should have different UUIDs");
        assertEquals(2, gameManager.numberOfRunningGames(), "Should have 2 games");
        
        // Join games
        Optional<ChessGame> joined1 = gameManager.requestToJoinGame(created1.getUuid(), clientPlayer);
        Player client2 = new Player(UUID.randomUUID(), "Client2", 1500);
        Optional<ChessGame> joined2 = gameManager.requestToJoinGame(created2.getUuid(), client2);
        
        assertTrue(joined1.isPresent(), "Should be able to join first game");
        assertTrue(joined2.isPresent(), "Should be able to join second game");
        
        System.out.println("Game 1 status: " + joined1.get().getGameStatus());
        System.out.println("Game 2 status: " + joined2.get().getGameStatus());
    }
}