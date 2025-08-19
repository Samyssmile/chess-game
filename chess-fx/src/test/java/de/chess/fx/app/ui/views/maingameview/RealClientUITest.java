package de.chess.fx.app.ui.views.maingameview;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.dto.RequestType;
import de.chess.dto.response.MoveResponse;
import de.chess.fx.app.client.ResponseAnalyzer;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.model.ChessColor;
import de.chess.model.GameBoard;
import de.chess.model.GameType;
import de.chess.model.Piece;
import de.chess.model.PieceType;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * REAL CLIENT UI TEST - Tests actual JavaFX components with real server data
 * NO MOCKS - Uses real ResponseAnalyzer, EventHandler, GameView with actual server GameDTO
 */
class RealClientUITest {
    
    private static final Logger LOGGER = Logger.getGlobal();
    
    // Real JavaFX components
    private ResponseAnalyzer realResponseAnalyzer;
    private EventHandler realEventHandler;
    private GameView realGameView;
    
    // Real game data (as it would come from server)
    private ChessGame realServerGameData;
    private Player hostPlayer;
    private Player clientPlayer;
    
    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit for UI components
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
    }
    
    @BeforeEach
    void setUp() throws InterruptedException {
        LOGGER.info("=== Setting up REAL Client UI Test ===");
        
        // Create REAL game data as server would send it
        hostPlayer = new Player(UUID.randomUUID(), "RealHost", 1200);
        clientPlayer = new Player(UUID.randomUUID(), "RealClient", 1300);
        realServerGameData = createRealServerGameWithMove();
        
        // Create REAL JavaFX components (no mocks!)
        realResponseAnalyzer = new ResponseAnalyzer();
        realEventHandler = EventHandler.getInstance();
        
        // Create REAL GameView with real server data
        createRealGameView();
        
        LOGGER.info("=== REAL Client UI Test Setup Complete ===");
    }
    
    @AfterEach
    void tearDown() {
        // Clean up JavaFX components
        if (realGameView != null) {
            Platform.runLater(() -> {
                // Clean up UI components if needed
            });
        }
    }
    
    @Test
    @DisplayName("REAL UI Test: Verify JavaFX GameView displays correct board state from server")
    void testRealGameViewWithRealServerData() throws InterruptedException {
        LOGGER.info("=== Testing REAL JavaFX GameView with REAL server data ===");
        
        // Step 1: Verify the real server game data is correct (baseline)
        LOGGER.info("Step 1: Verifying real server game data");
        logGameState("Real server game data", realServerGameData);
        
        Piece serverE2 = realServerGameData.getGameBoard().getPiece("e2");
        Piece serverE4 = realServerGameData.getGameBoard().getPiece("e4");
        
        LOGGER.info("Server data e2: " + (serverE2 != null ? serverE2.toString() : "EMPTY"));
        LOGGER.info("Server data e4: " + (serverE4 != null ? serverE4.toString() : "EMPTY"));
        
        assertNull(serverE2, "Server data: e2 should be empty after move");
        assertNotNull(serverE4, "Server data: e4 should have moved pawn");
        assertEquals(PieceType.PAWN, serverE4.getPieceType(), "Server data: e4 should have pawn");
        assertEquals(ChessColor.WHITE, serverE4.getColor(), "Server data: e4 should have white pawn");
        
        LOGGER.info("✅ Real server data is correct - baseline established");
        
        // Step 2: Create REAL MoveResponse as server would send it
        LOGGER.info("Step 2: Creating REAL MoveResponse with server data");
        MoveResponse realMoveResponse = new MoveResponse(
            realServerGameData.getUuid(),
            RequestType.MOVE,
            true,
            "e2-e4",
            realServerGameData  // REAL server game data
        );
        
        // Verify the MoveResponse contains correct data
        ChessGame responseGameDTO = realMoveResponse.getGameDTO();
        assertNotNull(responseGameDTO, "MoveResponse should contain GameDTO");
        assertSame(realServerGameData, responseGameDTO, "MoveResponse should contain the exact server data");
        
        // Step 3: Process through REAL ResponseAnalyzer
        LOGGER.info("Step 3: Processing through REAL ResponseAnalyzer");
        
        // Set up listener to capture what happens in the UI
        CountDownLatch uiUpdateLatch = new CountDownLatch(1);
        AtomicReference<Exception> uiUpdateError = new AtomicReference<>();
        
        // Register GameView to listen for MOVE_DONE events (like real application does)
        realEventHandler.registerForEvent(realGameView, EventType.MOVE_DONE);
        
        Platform.runLater(() -> {
            try {
                // This is exactly what happens in the real client when server response arrives
                realResponseAnalyzer.analyze(realMoveResponse);
                
                LOGGER.info("ResponseAnalyzer.analyze() completed");
                uiUpdateLatch.countDown();
                
            } catch (Exception e) {
                LOGGER.severe("Error in ResponseAnalyzer: " + e.getMessage());
                e.printStackTrace();
                uiUpdateError.set(e);
                uiUpdateLatch.countDown();
            }
        });
        
        // Wait for UI update to complete
        boolean uiCompleted = uiUpdateLatch.await(10, TimeUnit.SECONDS);
        assertTrue(uiCompleted, "UI update should complete within 10 seconds");
        
        if (uiUpdateError.get() != null) {
            throw new RuntimeException("Error in UI update", uiUpdateError.get());
        }
        
        LOGGER.info("✅ Real ResponseAnalyzer processing completed");
        
        // Step 4: CRITICAL - Verify the GameView's internal state after UI update
        LOGGER.info("Step 4: CRITICAL - Checking GameView internal state after update");
        
        CountDownLatch inspectionLatch = new CountDownLatch(1);
        AtomicReference<String> inspectionResult = new AtomicReference<>();
        
        Platform.runLater(() -> {
            try {
                // Access GameView's internal game state
                GameViewModel viewModel = getGameViewModelFromGameView(realGameView);
                ChessGame currentGameDTO = viewModel.getGameDTO();
                
                if (currentGameDTO == null) {
                    inspectionResult.set("CRITICAL ERROR: GameView's GameDTO is NULL after update");
                    inspectionLatch.countDown();
                    return;
                }
                
                GameBoard currentBoard = currentGameDTO.getGameBoard();
                if (currentBoard == null) {
                    inspectionResult.set("CRITICAL ERROR: GameView's GameBoard is NULL after update");
                    inspectionLatch.countDown();
                    return;
                }
                
                // Check the actual board state in GameView
                Piece viewE2 = currentBoard.getPiece("e2");
                Piece viewE4 = currentBoard.getPiece("e4");
                
                StringBuilder result = new StringBuilder();
                result.append("GameView internal state:\n");
                result.append("e2: ").append(viewE2 != null ? viewE2.toString() : "EMPTY").append("\n");
                result.append("e4: ").append(viewE4 != null ? viewE4.toString() : "EMPTY").append("\n");
                
                // Log full board state from GameView
                logBoardState("GameView internal board", currentBoard);
                
                // Check if the bug is here - does GameView have wrong board state?
                if (viewE2 != null) {
                    result.append("🐛 BUG FOUND: GameView still shows piece at e2 - UI not updated correctly!\n");
                }
                if (viewE4 == null) {
                    result.append("🐛 BUG FOUND: GameView shows no piece at e4 - UI not updated correctly!\n");
                }
                if (viewE2 == null && viewE4 != null && viewE4.getPieceType() == PieceType.PAWN && viewE4.getColor() == ChessColor.WHITE) {
                    result.append("✅ GameView internal state is CORRECT - move properly applied in UI\n");
                }
                
                inspectionResult.set(result.toString());
                inspectionLatch.countDown();
                
            } catch (Exception e) {
                inspectionResult.set("ERROR during inspection: " + e.getMessage());
                inspectionLatch.countDown();
            }
        });
        
        boolean inspectionCompleted = inspectionLatch.await(10, TimeUnit.SECONDS);
        assertTrue(inspectionCompleted, "GameView inspection should complete within 10 seconds");
        
        String result = inspectionResult.get();
        LOGGER.info("GameView inspection result:\n" + result);
        
        // Step 5: Final assertions based on GameView internal state
        LOGGER.info("Step 5: Final verification of GameView state");
        
        if (result.contains("BUG FOUND")) {
            fail("🐛 BUG CONFIRMED in JavaFX UI: " + result);
        }
        if (result.contains("NULL")) {
            fail("💥 CRITICAL ERROR in GameView: " + result);
        }
        if (result.contains("ERROR")) {
            fail("💥 ERROR during GameView inspection: " + result);
        }
        
        assertTrue(result.contains("CORRECT"), "GameView should show correct board state: " + result);
        
        LOGGER.info("🎉 REAL CLIENT UI TEST PASSED: JavaFX GameView correctly displays server data!");
    }
    
    /**
     * Create real server game data with e2-e4 move already applied (as server would send)
     */
    private ChessGame createRealServerGameWithMove() {
        LOGGER.info("Creating REAL server game data with e2-e4 move");
        
        ChessGame serverGame = new ChessGame("RealServerGame", hostPlayer, clientPlayer, "10", ChessColor.WHITE, GameType.FUN);
        serverGame.setUuid(UUID.randomUUID());
        
        // Create board with starting position
        GameBoard serverBoard = new GameBoard();
        serverBoard.initialDistibutionOfChessPieces();
        
        // Apply e2-e4 move (as server would do)
        Piece whitePawn = serverBoard.getPiece("e2");
        assertNotNull(whitePawn, "Should have pawn at e2 initially");
        serverBoard.removePiece("e2");
        serverBoard.putPiece("e4", whitePawn);
        
        serverGame.setGameBoard(serverBoard);
        serverGame.switchTurn(); // Turn becomes BLACK after white moves
        
        LOGGER.info("Real server game data created with move applied");
        return serverGame;
    }
    
    /**
     * Create REAL GameView with real server data
     */
    private void createRealGameView() throws InterruptedException {
        LOGGER.info("Creating REAL GameView with server data");
        
        CountDownLatch gameViewLatch = new CountDownLatch(1);
        AtomicReference<GameView> gameViewRef = new AtomicReference<>();
        AtomicReference<Exception> creationError = new AtomicReference<>();
        
        Platform.runLater(() -> {
            try {
                // Create REAL GameView exactly as the application would
                realGameView = new GameView(realServerGameData, true);
                gameViewRef.set(realGameView);
                LOGGER.info("REAL GameView created successfully");
                gameViewLatch.countDown();
            } catch (Exception e) {
                LOGGER.severe("Failed to create REAL GameView: " + e.getMessage());
                e.printStackTrace();
                creationError.set(e);
                gameViewLatch.countDown();
            }
        });
        
        boolean created = gameViewLatch.await(10, TimeUnit.SECONDS);
        assertTrue(created, "GameView should be created within 10 seconds");
        
        if (creationError.get() != null) {
            throw new RuntimeException("Failed to create GameView", creationError.get());
        }
        
        realGameView = gameViewRef.get();
        assertNotNull(realGameView, "Real GameView should be created");
        
        LOGGER.info("✅ REAL GameView created and ready");
    }
    
    /**
     * Access GameViewModel from GameView using reflection (since it's private)
     */
    private GameViewModel getGameViewModelFromGameView(GameView gameView) throws Exception {
        java.lang.reflect.Field viewModelField = GameView.class.getDeclaredField("viewModel");
        viewModelField.setAccessible(true);
        return (GameViewModel) viewModelField.get(gameView);
    }
    
    private void logGameState(String description, ChessGame game) {
        LOGGER.info("=== " + description + " ===");
        if (game == null) {
            LOGGER.warning("Game is NULL");
            return;
        }
        
        GameBoard board = game.getGameBoard();
        if (board == null) {
            LOGGER.warning("GameBoard is NULL");
            return;
        }
        
        logBoardState(description + " board", board);
    }
    
    private void logBoardState(String description, GameBoard board) {
        LOGGER.info("=== " + description + " ===");
        if (board == null) {
            LOGGER.warning("Board is NULL!");
            return;
        }
        
        // Log specific positions
        Piece e2Piece = board.getPiece("e2");
        Piece e4Piece = board.getPiece("e4");
        
        LOGGER.info("e2: " + (e2Piece != null ? e2Piece.getPieceType() + "_" + e2Piece.getColor() : "EMPTY"));
        LOGGER.info("e4: " + (e4Piece != null ? e4Piece.getPieceType() + "_" + e4Piece.getColor() : "EMPTY"));
        
        // Log full board
        StringBuilder boardStr = new StringBuilder("Board:\n");
        for (int row = 0; row < 8; row++) {
            boardStr.append("Row ").append(8 - row).append(": ");
            for (int col = 0; col < 8; col++) {
                String field = ((char)('a' + col)) + String.valueOf(8 - row);
                Piece piece = board.getPiece(field);
                if (piece != null) {
                    boardStr.append(piece.getPieceType().toString().charAt(0))
                           .append(piece.getColor().toString().charAt(0))
                           .append(" ");
                } else {
                    boardStr.append("-- ");
                }
            }
            boardStr.append("\n");
        }
        LOGGER.info(boardStr.toString());
    }
}