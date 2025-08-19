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
 * Debug test to isolate the exact problems with the turn system
 */
class DebugTurnTest {

    private IGameManager gameManager;
    private ChessGame testGame;
    private Player whitePlayer;
    private Player blackPlayer;

    @BeforeEach
    void setUp() {
        gameManager = GameManager.instance();
        gameManager.reset();

        whitePlayer = new Player(UUID.randomUUID(), "WhitePlayer", 1200);
        blackPlayer = new Player(UUID.randomUUID(), "BlackPlayer", 1300);

        testGame = new ChessGame("DebugTest", whitePlayer, "10", ChessColor.WHITE, GameType.FUN);
        testGame = gameManager.requestGame(testGame);
        gameManager.requestToJoinGame(testGame.getUuid(), blackPlayer);
        
        // Manually set up the game as RUNNING
        testGame.setGameStatus(GameStatus.RUNNING);
        testGame.initGameBoard();
        
        System.out.println("=== SETUP COMPLETE ===");
        System.out.println("Game Status: " + testGame.getGameStatus());
        System.out.println("Current Turn: " + testGame.getCurrentTurn());
        System.out.println("Host Color: " + testGame.getHostColor());
        System.out.println("Client Color: " + testGame.getClientColor());
        System.out.println("Host Player: " + testGame.getHostPlayer().getNickname());
        System.out.println("Client Player: " + testGame.getClientPlayer().getNickname());
    }

    @Test
    @DisplayName("Debug: Test single move and turn switching")
    void debugSingleMove() {
        System.out.println("\n=== STARTING SINGLE MOVE TEST ===");
        
        // Initial state
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn());
        System.out.println("Initial turn: " + testGame.getCurrentTurn());
        
        // Try white's first move
        System.out.println("Attempting white's move: e2-e4");
        boolean result1 = gameManager.move(testGame.getUuid(), "e2-e4");
        System.out.println("Move result: " + result1);
        System.out.println("Turn after move: " + testGame.getCurrentTurn());
        
        assertTrue(result1, "White's first move should succeed");
        assertEquals(ChessColor.BLACK, testGame.getCurrentTurn(), "Turn should switch to BLACK");
        
        // Try black's response
        System.out.println("\nAttempting black's move: e7-e5");
        boolean result2 = gameManager.move(testGame.getUuid(), "e7-e5");
        System.out.println("Move result: " + result2);
        System.out.println("Turn after move: " + testGame.getCurrentTurn());
        
        assertTrue(result2, "Black's move should succeed");
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), "Turn should switch back to WHITE");
    }
    
    @Test
    @DisplayName("Debug: Test invalid move scenarios")
    void debugInvalidMoves() {
        System.out.println("\n=== STARTING INVALID MOVE TEST ===");
        
        // Try completely invalid move
        System.out.println("Attempting invalid move: a1-h8");
        boolean result1 = gameManager.move(testGame.getUuid(), "a1-h8");
        System.out.println("Invalid move result: " + result1);
        System.out.println("Turn after invalid move: " + testGame.getCurrentTurn());
        
        assertFalse(result1, "Invalid move should fail");
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), "Turn should not change after invalid move");
        
        // Try moving opponent's piece
        System.out.println("\nAttempting to move opponent's piece: e7-e5");
        boolean result2 = gameManager.move(testGame.getUuid(), "e7-e5");
        System.out.println("Opponent move result: " + result2);
        System.out.println("Turn after opponent move: " + testGame.getCurrentTurn());
        
        assertFalse(result2, "Moving opponent's piece should fail");
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), "Turn should not change");
    }
    
    @Test
    @DisplayName("Debug: Test double move prevention")
    void debugDoubleMove() {
        System.out.println("\n=== STARTING DOUBLE MOVE TEST ===");
        
        // White makes valid first move
        System.out.println("White first move: e2-e4");
        boolean result1 = gameManager.move(testGame.getUuid(), "e2-e4");
        System.out.println("Result: " + result1 + ", Turn: " + testGame.getCurrentTurn());
        
        assertTrue(result1);
        assertEquals(ChessColor.BLACK, testGame.getCurrentTurn());
        
        // White tries to make another move immediately
        System.out.println("\nWhite tries second move: d2-d4");
        boolean result2 = gameManager.move(testGame.getUuid(), "d2-d4");
        System.out.println("Result: " + result2 + ", Turn: " + testGame.getCurrentTurn());
        
        assertFalse(result2, "White should NOT be able to make second move");
        assertEquals(ChessColor.BLACK, testGame.getCurrentTurn(), "Turn should remain BLACK");
    }
    
    @Test
    @DisplayName("Debug: Test piece validation details")
    void debugPieceValidation() {
        System.out.println("\n=== STARTING PIECE VALIDATION TEST ===");
        
        // Check board state
        System.out.println("Piece at e2: " + testGame.getGameBoard().getPiece("e2"));
        System.out.println("Piece at e4: " + testGame.getGameBoard().getPiece("e4"));
        System.out.println("Piece at e7: " + testGame.getGameBoard().getPiece("e7"));
        
        // Test various moves
        testMoveAndReport("e2-e4", "Valid pawn move");
        testMoveAndReport("e4-e5", "Pawn move after previous move");  
        testMoveAndReport("a1-a8", "Rook through pieces");
        testMoveAndReport("b1-c3", "Knight move");
    }
    
    private void testMoveAndReport(String move, String description) {
        System.out.println("\nTesting: " + move + " (" + description + ")");
        System.out.println("Turn before: " + testGame.getCurrentTurn());
        boolean result = gameManager.move(testGame.getUuid(), move);
        System.out.println("Result: " + result);
        System.out.println("Turn after: " + testGame.getCurrentTurn());
    }
}