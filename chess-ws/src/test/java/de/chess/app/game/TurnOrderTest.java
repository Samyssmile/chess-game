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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for chess game turn order mechanics
 */
class TurnOrderTest {

    private IGameManager gameManager;
    private ChessGame testGame;
    private Player whitePlayer;
    private Player blackPlayer;

    @BeforeEach
    void setUp() {
        gameManager = GameManager.instance();
        gameManager.reset();

        // Create test players
        whitePlayer = new Player(UUID.randomUUID(), "WhitePlayer", 1200);
        blackPlayer = new Player(UUID.randomUUID(), "BlackPlayer", 1300);

        // Create and setup a game
        testGame = new ChessGame("TurnOrderTest", whitePlayer, "10", ChessColor.WHITE, GameType.FUN);
        testGame = gameManager.requestGame(testGame);
        
        // Add second player to start the game
        gameManager.requestToJoinGame(testGame.getUuid(), blackPlayer);
    }

    @Test
    @DisplayName("Game should start with White player's turn")
    void testGameStartsWithWhiteTurn() {
        // Verify game is running
        assertEquals(GameStatus.READY_TO_START, testGame.getGameStatus(), 
            "Game should be ready to start after both players join");

        // Start the game (simulate game thread startup)
        testGame.setGameStatus(GameStatus.RUNNING);
        testGame.initGameBoard(); // This should set currentTurn to WHITE

        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), 
            "Game should always start with WHITE player's turn");
    }

    @Test
    @DisplayName("Only the current turn player should be able to make moves")
    void testOnlyCurrentPlayerCanMove() {
        // Setup running game
        testGame.setGameStatus(GameStatus.RUNNING);
        testGame.initGameBoard();
        
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), 
            "Game should start with WHITE turn");

        // White player should be able to make a move
        boolean whiteCanMove = gameManager.move(testGame.getUuid(), "e2-e4");
        assertTrue(whiteCanMove, "White player should be able to make first move");

        // After white's move, turn should switch to black
        assertEquals(ChessColor.BLACK, testGame.getCurrentTurn(),
            "Turn should switch to BLACK after white's move");

        // White should not be able to move again immediately
        boolean whiteCanMoveAgain = gameManager.move(testGame.getUuid(), "e4-e5");
        assertFalse(whiteCanMoveAgain, "White player should NOT be able to move when it's black's turn");
    }

    @Test
    @DisplayName("Turn should alternate correctly between players")
    void testTurnAlternation() {
        // Setup running game
        testGame.setGameStatus(GameStatus.RUNNING);
        testGame.initGameBoard();

        // Track turns through multiple moves
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), "Should start with WHITE");

        // Move 1: White
        gameManager.move(testGame.getUuid(), "e2-e4");
        assertEquals(ChessColor.BLACK, testGame.getCurrentTurn(), "Should be BLACK's turn after white moves");

        // Move 2: Black  
        gameManager.move(testGame.getUuid(), "e7-e5");
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), "Should be WHITE's turn after black moves");

        // Move 3: White
        gameManager.move(testGame.getUuid(), "Nf3");
        assertEquals(ChessColor.BLACK, testGame.getCurrentTurn(), "Should be BLACK's turn again");

        // Move 4: Black
        gameManager.move(testGame.getUuid(), "Nc6");
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), "Should be WHITE's turn again");
    }

    @Test
    @DisplayName("Invalid moves should not change the turn")
    void testInvalidMoveDoesNotChangeTurn() {
        // Setup running game
        testGame.setGameStatus(GameStatus.RUNNING);
        testGame.initGameBoard();

        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), "Should start with WHITE");

        // Try an invalid move
        boolean invalidMoveResult = gameManager.move(testGame.getUuid(), "e2-e5"); // Invalid: pawn can't jump 3 squares
        assertFalse(invalidMoveResult, "Invalid move should return false");

        // Turn should remain WHITE
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), 
            "Turn should remain WHITE after invalid move");
    }

    @Test
    @DisplayName("Game should not allow moves when game is not running")
    void testNoMovesWhenGameNotRunning() {
        // Test with WATING status
        testGame.setGameStatus(GameStatus.WATING);
        boolean result1 = gameManager.move(testGame.getUuid(), "e2-e4");
        assertFalse(result1, "Should not allow moves when game status is WATING");

        // Test with FINISHED status
        testGame.setGameStatus(GameStatus.FINISHED);
        boolean result2 = gameManager.move(testGame.getUuid(), "e2-e4");
        assertFalse(result2, "Should not allow moves when game status is FINISHED");
    }

    @Test
    @DisplayName("Player colors should be assigned correctly")
    void testPlayerColorAssignment() {
        // Host chose WHITE, so host should be white, client should be black
        assertEquals(ChessColor.WHITE, testGame.getHostColor(), "Host should be WHITE");
        assertEquals(ChessColor.BLACK, testGame.getClientColor(), "Client should be BLACK");

        // Test that the correct players are assigned to correct colors
        assertEquals(whitePlayer, testGame.getHostPlayer(), "Host player should be the white player");
        assertEquals(blackPlayer, testGame.getClientPlayer(), "Client player should be the black player");
    }

    @Test
    @DisplayName("Move validation should respect piece ownership")
    void testMoveValidationRespectsPieceOwnership() {
        // Setup running game
        testGame.setGameStatus(GameStatus.RUNNING);
        testGame.initGameBoard();

        // WHITE's turn - try to move a black piece
        boolean result = gameManager.move(testGame.getUuid(), "e7-e5"); // Black pawn move on white's turn
        assertFalse(result, "Should not allow white player to move black pieces");

        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), 
            "Turn should remain WHITE after invalid piece ownership move");
    }

    @Test
    @DisplayName("Move format parsing should work correctly")
    void testMoveFormatParsing() {
        testGame.setGameStatus(GameStatus.RUNNING);
        testGame.initGameBoard();

        // Test different move formats
        assertTrue(gameManager.move(testGame.getUuid(), "e2-e4"), "Should accept 'e2-e4' format");
        assertTrue(gameManager.move(testGame.getUuid(), "e7:e5"), "Should accept 'e7:e5' format"); 

        // Test promotion format (if implemented)
        // This test might fail if promotion is not fully implemented
        testGame.switchTurn(); // Back to white for promotion test setup
        // Move pawn to 7th rank first... this is complex to set up
        // For now, just test that the format doesn't crash
        boolean promotionResult = gameManager.move(testGame.getUuid(), "e7-e8Q");
        // This might return false due to invalid position, but shouldn't crash
    }

    @Test
    @DisplayName("Game state should be properly synchronized after moves")
    void testGameStateSynchronization() {
        testGame.setGameStatus(GameStatus.RUNNING);
        testGame.initGameBoard();

        // Make a move
        boolean moveResult = gameManager.move(testGame.getUuid(), "e2-e4");
        assertTrue(moveResult, "Move should succeed");

        // Verify game state was updated
        assertEquals(ChessColor.BLACK, testGame.getCurrentTurn(), "Turn should have switched");
        
        // Verify board state was updated (basic check)
        assertNotNull(testGame.getGameBoard(), "Game board should exist");
        
        // The piece should have moved from e2 to e4
        // Note: This test might fail if board coordinate system is incorrect
        assertNull(testGame.getGameBoard().getPiece("e2"), "e2 should be empty after move");
        assertNotNull(testGame.getGameBoard().getPiece("e4"), "e4 should have the moved piece");
    }
}