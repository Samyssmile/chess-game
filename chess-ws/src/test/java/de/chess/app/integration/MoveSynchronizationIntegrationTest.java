package de.chess.app.integration;

import de.chess.app.game.GameManager;
import de.chess.app.server.RequestAnalyzer;
import de.chess.dto.*;
import de.chess.dto.request.*;
import de.chess.dto.response.*;
import de.chess.game.IGameManager;
import de.chess.io.server.ClientThread;
import de.chess.io.server.PlayerConnection;
import de.chess.io.server.PlayerManager;
import de.chess.model.ChessColor;
import de.chess.model.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test that reproduces the move synchronization bug with server and two clients
 */
class MoveSynchronizationIntegrationTest {
    
    private static final Logger LOGGER = Logger.getGlobal();
    
    private IGameManager gameManager;
    private RequestAnalyzer requestAnalyzer;
    private PlayerManager playerManager;
    
    // Test players
    private Player hostPlayer;
    private Player clientPlayer;
    private UUID hostPlayerUUID;
    private UUID clientPlayerUUID;
    
    // Mock client threads to simulate real connections
    private MockClientThread hostClientThread;
    private MockClientThread clientClientThread;
    
    // Game state
    private ChessGame testGame;
    private UUID gameUUID;

    @BeforeEach
    void setUp() {
        // Reset all singletons
        gameManager = GameManager.instance();
        gameManager.reset();
        
        requestAnalyzer = new RequestAnalyzer();
        playerManager = PlayerManager.getInstance();
        playerManager.reset();
        
        // Create test players
        hostPlayerUUID = UUID.randomUUID();
        clientPlayerUUID = UUID.randomUUID();
        hostPlayer = new Player(hostPlayerUUID, "HostPlayer", 1200);
        clientPlayer = new Player(clientPlayerUUID, "ClientPlayer", 1300);
        
        // Create mock client threads
        hostClientThread = new MockClientThread();
        clientClientThread = new MockClientThread();
        
        LOGGER.info("Integration test setup completed");
    }
    
    @AfterEach
    void tearDown() {
        gameManager.reset();
        // Don't reset playerManager during active tests - it breaks notifications
    }

    @Test
    @DisplayName("Reproduce move synchronization bug: moves not visible on both clients")
    void testMoveSynchronizationBug() throws InterruptedException {
        // Step 1: Setup players and game
        setupGameWithTwoPlayers();
        
        // Step 2: Start the game
        startGame();
        
        // Step 3: Make first move (white/host player)
        LOGGER.info("=== Making first move: e2-e4 ===");
        MoveResponse firstMoveResponse = makeMove(hostPlayerUUID, "e2-e4");
        
        // Step 4: Verify first move response
        assertNotNull(firstMoveResponse, "First move response should not be null");
        assertTrue(firstMoveResponse.isMoveAccepted(), 
            "First move (e2-e4) should be accepted. Error: " + firstMoveResponse.getMessage());
        
        // Step 4.1: Verify the board state in the server's game data has changed
        ChessGame serverGameState = firstMoveResponse.getGameDTO();
        assertNotNull(serverGameState, "Server should return updated game state");
        assertNotNull(serverGameState.getGameBoard(), "Server game should have board");
        
        // Check that the pawn actually moved from e2 to e4 on the server
        assertNull(serverGameState.getGameBoard().getPiece("e2"), 
            "e2 should be empty after pawn moved to e4");
        assertNotNull(serverGameState.getGameBoard().getPiece("e4"), 
            "e4 should contain the moved pawn");
        
        LOGGER.info("✅ Server board state verified: Pawn moved from e2 to e4");
        
        // Step 5: Check if both clients received the move update
        LOGGER.info("=== Checking move synchronization ===");
        
        // Host should receive successful move response
        assertTrue(hostClientThread.hasReceivedResponse(), "Host should have received move response");
        Response hostResponse = hostClientThread.getLastResponse();
        assertTrue(hostResponse instanceof MoveResponse, "Host should receive MoveResponse");
        MoveResponse hostMoveResponse = (MoveResponse) hostResponse;
        assertTrue(hostMoveResponse.isMoveAccepted(), "Host should see move as accepted");
        
        // Client should receive move notification
        assertTrue(clientClientThread.hasReceivedResponse(), "Client should have received move notification");
        Response clientResponse = clientClientThread.getLastResponse();
        assertTrue(clientResponse instanceof MoveResponse, "Client should receive MoveResponse notification");
        MoveResponse clientMoveResponse = (MoveResponse) clientResponse;
        assertTrue(clientMoveResponse.isMoveAccepted(), "Client should see move as accepted");
        
        // Step 5.1: CRITICAL TEST - Verify both clients have the SAME updated board state
        LOGGER.info("=== Verifying UI data foundation changes ===");
        
        ChessGame hostGameState = hostMoveResponse.getGameDTO();
        ChessGame clientGameState = clientMoveResponse.getGameDTO();
        
        assertNotNull(hostGameState, "Host should receive updated game state for UI");
        assertNotNull(clientGameState, "Client should receive updated game state for UI");
        
        // Both clients should see the same board state after the move
        assertNotNull(hostGameState.getGameBoard(), "Host should have updated game board");
        assertNotNull(clientGameState.getGameBoard(), "Client should have updated game board");
        
        // CRITICAL: Verify the UI data shows pawn moved from e2 to e4 on BOTH clients
        assertNull(hostGameState.getGameBoard().getPiece("e2"), 
            "HOST UI: e2 should be empty after move - THIS IS WHAT THE UI DISPLAYS");
        assertNotNull(hostGameState.getGameBoard().getPiece("e4"), 
            "HOST UI: e4 should have the moved pawn - THIS IS WHAT THE UI DISPLAYS");
            
        assertNull(clientGameState.getGameBoard().getPiece("e2"), 
            "CLIENT UI: e2 should be empty after move - THIS IS WHAT THE UI DISPLAYS");
        assertNotNull(clientGameState.getGameBoard().getPiece("e4"), 
            "CLIENT UI: e4 should have the moved pawn - THIS IS WHAT THE UI DISPLAYS");
        
        // Verify piece type is correct (should be a white pawn)
        de.chess.model.Piece hostPiece = hostGameState.getGameBoard().getPiece("e4");
        de.chess.model.Piece clientPiece = clientGameState.getGameBoard().getPiece("e4");
        
        assertEquals(de.chess.model.PieceType.PAWN, hostPiece.getPieceType(), 
            "HOST UI should show PAWN at e4");
        assertEquals(de.chess.model.ChessColor.WHITE, hostPiece.getColor(), 
            "HOST UI should show WHITE pawn at e4");
            
        assertEquals(de.chess.model.PieceType.PAWN, clientPiece.getPieceType(), 
            "CLIENT UI should show PAWN at e4");
        assertEquals(de.chess.model.ChessColor.WHITE, clientPiece.getColor(), 
            "CLIENT UI should show WHITE pawn at e4");
        
        LOGGER.info("✅ BOTH clients have identical board state - UI will show pawn at e4");
        
        // Step 6: Verify game state synchronization (turn switching)
        // Both clients should see the same current turn
        assertEquals(hostGameState.getCurrentTurn(), clientGameState.getCurrentTurn(),
            "Both clients should see the same current turn");
        assertEquals(ChessColor.BLACK, hostGameState.getCurrentTurn(),
            "After white's move, it should be black's turn");
        
        LOGGER.info("=== First move completed successfully ===");
        
        // Step 8: Try second move (black/client player)
        LOGGER.info("=== Making second move: e7-e5 ===");
        hostClientThread.clearResponses();
        clientClientThread.clearResponses();
        
        MoveResponse secondMoveResponse = makeMove(clientPlayerUUID, "e7-e5");
        
        // Step 9: Verify second move and turn switching
        assertNotNull(secondMoveResponse, "Second move response should not be null");
        assertTrue(secondMoveResponse.isMoveAccepted(), 
            "Second move (e7-e5) should be accepted. Error: " + secondMoveResponse.getMessage());
        
        // Both players should be notified
        assertTrue(hostClientThread.hasReceivedResponse(), "Host should be notified of opponent's move");
        assertTrue(clientClientThread.hasReceivedResponse(), "Client should receive their move response");
        
        // Step 9.1: Verify SECOND move board state changes
        LOGGER.info("=== Verifying second move UI data ===");
        ChessGame secondMoveGameState = secondMoveResponse.getGameDTO();
        
        // After second move: e2->e4 (white), e7->e5 (black)
        assertNull(secondMoveGameState.getGameBoard().getPiece("e7"), 
            "e7 should be empty after black pawn moved to e5");
        assertNotNull(secondMoveGameState.getGameBoard().getPiece("e5"), 
            "e5 should contain the black pawn");
        assertNotNull(secondMoveGameState.getGameBoard().getPiece("e4"), 
            "e4 should still contain the white pawn");
            
        // Verify piece types and colors
        de.chess.model.Piece blackPawn = secondMoveGameState.getGameBoard().getPiece("e5");
        de.chess.model.Piece whitePawn = secondMoveGameState.getGameBoard().getPiece("e4");
        
        assertEquals(de.chess.model.PieceType.PAWN, blackPawn.getPieceType(), "e5 should have black pawn");
        assertEquals(de.chess.model.ChessColor.BLACK, blackPawn.getColor(), "e5 should have BLACK pawn");
        assertEquals(de.chess.model.PieceType.PAWN, whitePawn.getPieceType(), "e4 should still have white pawn");
        assertEquals(de.chess.model.ChessColor.WHITE, whitePawn.getColor(), "e4 should still have WHITE pawn");
        
        // Turn should be back to WHITE
        assertEquals(ChessColor.WHITE, secondMoveGameState.getCurrentTurn(),
            "After black's move, it should be white's turn again");
            
        LOGGER.info("✅ Second move board state verified: Black pawn at e5, White pawn at e4");
        
        LOGGER.info("=== Move synchronization test completed ===");
    }
    
    @Test
    @DisplayName("Test that rejected moves don't affect game state")
    void testRejectedMovesDoNotAffectGameState() throws InterruptedException {
        setupGameWithTwoPlayers();
        startGame();
        
        // Try to make move when it's not the player's turn
        LOGGER.info("=== Testing rejected move: client tries to move on white's turn ===");
        MoveResponse rejectedMoveResponse = makeMove(clientPlayerUUID, "e7-e5");
        
        assertNotNull(rejectedMoveResponse, "Rejected move should still return response");
        assertFalse(rejectedMoveResponse.isMoveAccepted(), "Move should be rejected - not client's turn");
        assertEquals("Not your turn", rejectedMoveResponse.getMessage(), "Should get 'Not your turn' message");
        
        // Game state should remain unchanged
        ChessGame gameAfterRejection = gameManager.getGameByUUIID(gameUUID).get();
        assertEquals(ChessColor.WHITE, gameAfterRejection.getCurrentTurn(), 
            "Turn should still be WHITE after rejected move");
    }
    
    private void setupGameWithTwoPlayers() {
        LOGGER.info("Setting up game with two players...");
        
        // Register players with player manager (simulate self-introduction)
        playerManager.addPlayerConnection(new PlayerConnection(hostPlayerUUID, hostClientThread));
        playerManager.addPlayerConnection(new PlayerConnection(clientPlayerUUID, clientClientThread));
        
        // Create game request (host creates game)
        testGame = new ChessGame("IntegrationTest", hostPlayer, "10", ChessColor.WHITE, GameType.FUN);
        OpenGameRequest gameRequest = new OpenGameRequest(testGame, hostPlayerUUID);
        
        // Process game creation
        Response gameResponse = requestAnalyzer.analyze(gameRequest, hostClientThread);
        assertTrue(gameResponse instanceof OpenGameResponse, "Should receive OpenGameResponse");
        OpenGameResponse openResponse = (OpenGameResponse) gameResponse;
        assertTrue(openResponse.isGranted(), "Game creation should be granted");
        
        testGame = openResponse.getGameDTO();
        gameUUID = testGame.getUuid();
        
        LOGGER.info("Game created with UUID: " + gameUUID);
        
        // Client joins the game
        JoinGameRequest joinRequest = new JoinGameRequest(RequestType.JOIN, clientPlayer, gameUUID);
        Response joinResponse = requestAnalyzer.analyze(joinRequest, clientClientThread);
        
        assertTrue(joinResponse instanceof JoinGameResponse, "Should receive JoinGameResponse");
        JoinGameResponse joinGameResponse = (JoinGameResponse) joinResponse;
        assertTrue(joinGameResponse.isAccepted(), "Join should be granted");
        
        // Update test game reference
        testGame = joinGameResponse.getChessGame();
        
        LOGGER.info("Client joined game. Game status: " + testGame.getGameStatus());
        
        // Verify both players are in the game
        assertNotNull(testGame.getHostPlayer(), "Host player should be set");
        assertNotNull(testGame.getClientPlayer(), "Client player should be set");
        
        // Game may already be running due to GameThread auto-start, so check for either status
        assertTrue(testGame.getGameStatus() == GameStatus.READY_TO_START || testGame.getGameStatus() == GameStatus.RUNNING,
            "Game should be ready to start or already running, but was: " + testGame.getGameStatus());
    }
    
    private void startGame() throws InterruptedException {
        LOGGER.info("Starting game...");
        
        // Check if game is already running (GameThread may have started it automatically)
        if (testGame.getGameStatus() != GameStatus.RUNNING) {
            testGame.setGameStatus(GameStatus.RUNNING);
            testGame.initGameBoard();
        }
        
        // If board is not initialized yet, initialize it
        if (testGame.getGameBoard() == null) {
            testGame.initGameBoard();
        }
        
        // Re-register players with PlayerManager in case they were cleared
        playerManager.addPlayerConnection(new PlayerConnection(hostPlayerUUID, hostClientThread));
        playerManager.addPlayerConnection(new PlayerConnection(clientPlayerUUID, clientClientThread));
        
        // Simulate GameStarted notification to both players
        GameStartedResponse gameStartedResponse = new GameStartedResponse(gameUUID, RequestType.GAME_STARTED, testGame);
        hostClientThread.sendResponse(gameStartedResponse);
        clientClientThread.sendResponse(gameStartedResponse);
        
        assertEquals(GameStatus.RUNNING, testGame.getGameStatus(), "Game should be running");
        assertEquals(ChessColor.WHITE, testGame.getCurrentTurn(), "Game should start with WHITE turn");
        
        LOGGER.info("Game started successfully. Current turn: " + testGame.getCurrentTurn());
    }
    
    private MoveResponse makeMove(UUID playerUUID, String move) throws InterruptedException {
        LOGGER.info("Player " + playerUUID + " making move: " + move);
        
        // Create move request
        MoveRequest moveRequest = new MoveRequest(gameUUID, playerUUID, RequestType.MOVE, move);
        
        // Determine which client thread to use
        ClientThread playerThread = playerUUID.equals(hostPlayerUUID) ? hostClientThread : clientClientThread;
        
        // Process move request
        Response response = requestAnalyzer.analyze(moveRequest, playerThread);
        
        assertTrue(response instanceof MoveResponse, "Should receive MoveResponse");
        MoveResponse moveResponse = (MoveResponse) response;
        
        LOGGER.info("Move response: accepted=" + moveResponse.isMoveAccepted() + 
                   ", message=" + moveResponse.getMessage());
        
        return moveResponse;
    }
    
    /**
     * Mock ClientThread for testing without actual network connections
     */
    private static class MockClientThread extends ClientThread {
        private AtomicReference<Response> lastResponse = new AtomicReference<>();
        private volatile boolean hasResponse = false;
        
        public MockClientThread() {
            super(new MockSocket(), null);
        }
        
        @Override
        public void sendResponse(Response response) {
            LOGGER.info("MockClientThread sending response: " + response.getClass().getSimpleName());
            lastResponse.set(response);
            hasResponse = true;
        }
        
        public boolean hasReceivedResponse() {
            return hasResponse;
        }
        
        public Response getLastResponse() {
            return lastResponse.get();
        }
        
        public void clearResponses() {
            lastResponse.set(null);
            hasResponse = false;
        }
        
        @Override
        public void run() {
            // Do nothing - we're not actually running a client thread
        }
    }
    
    /**
     * Mock Socket for testing
     */
    private static class MockSocket extends Socket {
        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(new byte[0]);
        }
        
        @Override
        public OutputStream getOutputStream() {
            return new ByteArrayOutputStream();
        }
        
        @Override
        public boolean isClosed() {
            return false;
        }
    }
}