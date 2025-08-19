package de.chess.fx.app.ui.views.maingameview;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.dto.RequestType;
import de.chess.dto.response.MoveResponse;
import de.chess.fx.app.handler.EventData;
import de.chess.fx.app.handler.EventType;
import de.chess.model.ChessColor;
import de.chess.model.GameType;
import de.chess.model.GameBoard;
import de.chess.model.Piece;
import de.chess.model.PieceType;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test that reproduces the exact client-side board synchronization bug
 */
class GameViewBoardSyncTest {
    
    private static final Logger LOGGER = Logger.getGlobal();
    
    private ChessGame testGame;
    private GameView gameView;
    private TestGameViewModel testViewModel;
    
    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit (required for JavaFX components)
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
    }
    
    @BeforeEach
    void setUp() throws InterruptedException {
        // Create test game with updated board state (after e2-e4 move)
        Player hostPlayer = new Player(UUID.randomUUID(), "Host", 1200);
        Player clientPlayer = new Player(UUID.randomUUID(), "Client", 1300);
        
        testGame = new ChessGame("TestGame", hostPlayer, clientPlayer, "10", ChessColor.WHITE, GameType.FUN);
        testGame.setUuid(UUID.randomUUID());
        
        // Create board with e2-e4 move already made
        GameBoard movedBoard = new GameBoard();
        movedBoard.initialDistibutionOfChessPieces(); // Start position
        
        // Simulate e2-e4 move
        Piece whitePawn = movedBoard.getPiece("e2");
        movedBoard.removePiece("e2"); // Remove from e2
        movedBoard.putPiece("e4", whitePawn); // Place at e4
        
        testGame.setGameBoard(movedBoard);
        testGame.switchTurn(); // Should be BLACK's turn after white moves
        
        // Create GameView with test setup
        CountDownLatch viewCreatedLatch = new CountDownLatch(1);
        AtomicReference<GameView> gameViewRef = new AtomicReference<>();
        
        Platform.runLater(() -> {
            try {
                gameViewRef.set(new GameView(testGame, true));
                viewCreatedLatch.countDown();
            } catch (Exception e) {
                LOGGER.severe("Failed to create GameView: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        assertTrue(viewCreatedLatch.await(5, TimeUnit.SECONDS), "GameView should be created within 5 seconds");
        gameView = gameViewRef.get();
        assertNotNull(gameView, "GameView should be created successfully");
    }
    
    @Test
    @DisplayName("BUG REPRODUCTION: Client receives updated GameDTO but UI shows old board state")
    void testClientReceivesUpdatedGameDTOButUIShowsOldState() throws InterruptedException {
        LOGGER.info("=== Testing client board synchronization bug ===");
        
        // Step 1: Verify initial game state in UI
        logCurrentGameState("Initial game state", testGame);
        
        // Step 2: Create a MoveResponse with board that has e2-e4 move
        ChessGame serverUpdatedGame = createGameWithMove();
        MoveResponse moveResponse = new MoveResponse(
            testGame.getUuid(),
            RequestType.MOVE,
            true,
            "e2-e4",
            serverUpdatedGame
        );
        
        logCurrentGameState("Server game state (should show e2-e4 move)", serverUpdatedGame);
        
        // Step 3: Simulate what happens in ResponseAnalyzer
        CountDownLatch eventProcessedLatch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            try {
                LOGGER.info("=== Simulating MOVE_DONE event processing ===");
                
                // This is exactly what happens in GameView.update()
                ChessGame updatedGame = moveResponse.getGameDTO();
                logCurrentGameState("GameDTO received by client", updatedGame);
                
                // Verify we have the correct board state in the DTO
                assertNotNull(updatedGame.getGameBoard(), "Updated game should have board");
                
                // CRITICAL TEST: Does the received GameDTO actually contain the moved piece?
                Piece pieceAtE2 = updatedGame.getGameBoard().getPiece("e2");
                Piece pieceAtE4 = updatedGame.getGameBoard().getPiece("e4");
                
                LOGGER.info("Piece at e2: " + (pieceAtE2 != null ? pieceAtE2.toString() : "NULL"));
                LOGGER.info("Piece at e4: " + (pieceAtE4 != null ? pieceAtE4.toString() : "NULL"));
                
                // This is the CRITICAL assertion that should reveal the bug
                if (pieceAtE2 != null) {
                    fail("BUG CONFIRMED: GameDTO still has piece at e2 - move was not properly serialized/communicated");
                }
                if (pieceAtE4 == null) {
                    fail("BUG CONFIRMED: GameDTO has no piece at e4 - move was not properly serialized/communicated");
                }
                
                // If we get here, the DTO is correct, so the bug might be in UI update
                assertEquals(PieceType.PAWN, pieceAtE4.getPieceType(), "e4 should have a pawn");
                assertEquals(de.chess.model.ChessColor.WHITE, pieceAtE4.getColor(), "e4 should have white pawn");
                
                // Simulate UI update (this is what GameView.update() does)
                gameView.update(EventType.MOVE_DONE, new EventData(updatedGame));
                
                LOGGER.info("✅ GameDTO contains correct board state - move properly communicated");
                eventProcessedLatch.countDown();
                
            } catch (Exception e) {
                LOGGER.severe("Error in event processing: " + e.getMessage());
                e.printStackTrace();
                eventProcessedLatch.countDown();
            }
        });
        
        assertTrue(eventProcessedLatch.await(10, TimeUnit.SECONDS), 
            "Event processing should complete within 10 seconds");
        
        LOGGER.info("=== Board synchronization test completed ===");
    }
    
    @Test
    @DisplayName("Direct test: Create MoveResponse and verify GameDTO content")
    void testMoveResponseGameDTOContent() {
        LOGGER.info("=== Direct GameDTO content test ===");
        
        // Create game with move
        ChessGame gameWithMove = createGameWithMove();
        
        // Create MoveResponse
        MoveResponse response = new MoveResponse(
            gameWithMove.getUuid(),
            RequestType.MOVE,
            true,
            "e2-e4",
            gameWithMove
        );
        
        // Test the GameDTO content
        ChessGame receivedGame = response.getGameDTO();
        assertNotNull(receivedGame, "MoveResponse should contain GameDTO");
        assertNotNull(receivedGame.getGameBoard(), "GameDTO should contain board");
        
        // Log and verify board state
        logCurrentGameState("Direct MoveResponse GameDTO", receivedGame);
        
        // Critical assertions
        assertNull(receivedGame.getGameBoard().getPiece("e2"), 
            "e2 should be empty after move in GameDTO");
        assertNotNull(receivedGame.getGameBoard().getPiece("e4"), 
            "e4 should have pawn after move in GameDTO");
            
        Piece movedPawn = receivedGame.getGameBoard().getPiece("e4");
        assertEquals(PieceType.PAWN, movedPawn.getPieceType(), "e4 should contain a pawn");
        assertEquals(de.chess.model.ChessColor.WHITE, movedPawn.getColor(), "e4 should contain white pawn");
        
        LOGGER.info("✅ Direct GameDTO test passed - MoveResponse contains correct board state");
    }
    
    private ChessGame createGameWithMove() {
        Player host = new Player(UUID.randomUUID(), "Host", 1200);
        Player client = new Player(UUID.randomUUID(), "Client", 1300);
        
        ChessGame game = new ChessGame("TestWithMove", host, client, "10", ChessColor.WHITE, GameType.FUN);
        game.setUuid(UUID.randomUUID());
        
        // Create board and execute e2-e4 move
        GameBoard board = new GameBoard();
        board.initialDistibutionOfChessPieces();
        
        // Execute move e2-e4
        Piece pawn = board.getPiece("e2");
        assertNotNull(pawn, "Should have pawn at e2 initially");
        board.removePiece("e2");
        board.putPiece("e4", pawn);
        
        game.setGameBoard(board);
        game.switchTurn(); // Turn switches to BLACK
        
        return game;
    }
    
    private void logCurrentGameState(String description, ChessGame game) {
        LOGGER.info("=== " + description + " ===");
        if (game == null) {
            LOGGER.warning("Game is NULL");
            return;
        }
        
        LOGGER.info("Game UUID: " + game.getUuid());
        LOGGER.info("Current turn: " + game.getCurrentTurn());
        
        GameBoard board = game.getGameBoard();
        if (board == null) {
            LOGGER.warning("Game board is NULL");
            return;
        }
        
        // Log specific positions
        Piece e2Piece = board.getPiece("e2");
        Piece e4Piece = board.getPiece("e4");
        
        LOGGER.info("Piece at e2: " + (e2Piece != null ? e2Piece.getPieceType() + "_" + e2Piece.getColor() : "EMPTY"));
        LOGGER.info("Piece at e4: " + (e4Piece != null ? e4Piece.getPieceType() + "_" + e4Piece.getColor() : "EMPTY"));
        
        // Log full board state for debugging
        StringBuilder boardState = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            boardState.append("Row ").append(8 - row).append(": ");
            for (int col = 0; col < 8; col++) {
                String field = ((char)('a' + col)) + String.valueOf(8 - row);
                Piece piece = board.getPiece(field);
                if (piece != null) {
                    boardState.append(piece.getPieceType().toString().charAt(0))
                             .append(piece.getColor().toString().charAt(0))
                             .append(" ");
                } else {
                    boardState.append("-- ");
                }
            }
            boardState.append("\n");
        }
        LOGGER.info("Board state:\n" + boardState.toString());
    }
    
    /**
     * Minimal test ViewModel for testing
     */
    private static class TestGameViewModel extends GameViewModel {
        // Inherits all functionality, just for testing isolation
    }
}