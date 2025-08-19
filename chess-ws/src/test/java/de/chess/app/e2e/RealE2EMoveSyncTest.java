package de.chess.app.e2e;

import de.chess.app.server.RequestAnalyzer;
import de.chess.app.server.ServerProperties;
import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.dto.RequestType;
import de.chess.dto.request.*;
import de.chess.dto.response.*;
// Note: Can't import chess-fx ResponseAnalyzer from chess-ws module, using custom implementation
import de.chess.io.client.GameClient;
import de.chess.io.client.IResponseAnalyzer;
import de.chess.io.server.GameServer;
import de.chess.model.ChessColor;
import de.chess.model.GameType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Real E2E test with actual server and client processes, real TCP communication
 * NO MOCKS - this tests the complete flow including network serialization
 */
class RealE2EMoveSyncTest {
    
    private static final Logger LOGGER = Logger.getGlobal();
    private static final int TEST_SERVER_PORT = 8087; // Different from default to avoid conflicts
    
    private GameServer server;
    private Thread serverThread;
    private TestResponseAnalyzer hostResponseAnalyzer;
    private TestResponseAnalyzer clientResponseAnalyzer;
    private GameClient hostClient;
    private GameClient clientClient;
    
    // Test data
    private Player hostPlayer;
    private Player clientPlayer;
    private UUID hostPlayerUUID;
    private UUID clientPlayerUUID;
    private UUID gameUUID;
    
    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        LOGGER.info("=== Setting up Real E2E Test ===");
        
        // Create test players
        hostPlayerUUID = UUID.randomUUID();
        clientPlayerUUID = UUID.randomUUID();
        hostPlayer = new Player(hostPlayerUUID, "HostPlayer", 1200);
        clientPlayer = new Player(clientPlayerUUID, "ClientPlayer", 1300);
        
        // Setup and start real server
        startRealServer();
        
        // Wait for server to be ready
        Thread.sleep(2000);
        
        // Create real client response analyzers with event capturing
        hostResponseAnalyzer = new TestResponseAnalyzer("HOST");
        clientResponseAnalyzer = new TestResponseAnalyzer("CLIENT");
        
        // Create and connect real clients
        connectRealClients();
        
        LOGGER.info("=== E2E Test Setup Complete ===");
    }
    
    @AfterEach
    void tearDown() throws InterruptedException {
        LOGGER.info("=== Tearing down Real E2E Test ===");
        
        // Close client connections
        if (hostClient != null) {
            // Clients will disconnect when server stops
        }
        if (clientClient != null) {
            // Clients will disconnect when server stops
        }
        
        // Stop server
        if (server != null) {
            server.stopServer();
        }
        if (serverThread != null) {
            serverThread.interrupt();
            serverThread.join(2000); // Wait up to 2 seconds
        }
        
        // Reset singleton
        try {
            java.lang.reflect.Field instanceField = GameClient.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            LOGGER.warning("Could not reset GameClient singleton: " + e.getMessage());
        }
        
        LOGGER.info("=== E2E Test Teardown Complete ===");
    }
    
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    @DisplayName("Real E2E Test: Complete move synchronization with actual server and clients")
    void testRealE2EMoveSynchronization() throws InterruptedException {
        LOGGER.info("=== Starting Real E2E Move Synchronization Test ===");
        
        // Step 1: Host creates a game
        LOGGER.info("Step 1: Host creates game");
        ChessGame gameTemplate = new ChessGame("E2E_Test_Game", hostPlayer, "10", ChessColor.WHITE, GameType.FUN);
        OpenGameRequest openGameRequest = new OpenGameRequest(gameTemplate, hostPlayerUUID);
        
        hostResponseAnalyzer.expectResponse(OpenGameResponse.class);
        hostClient.sendRequest(openGameRequest);
        
        // Wait for game creation response
        OpenGameResponse openResponse = (OpenGameResponse) hostResponseAnalyzer.waitForResponse(5000);
        assertTrue(openResponse.isGranted(), "Game creation should be granted");
        gameUUID = openResponse.getGameDTO().getUuid();
        assertNotNull(gameUUID, "Game UUID should be set");
        
        LOGGER.info("✅ Game created with UUID: " + gameUUID);
        
        // Step 2: Client joins the game
        LOGGER.info("Step 2: Client joins game");
        JoinGameRequest joinRequest = new JoinGameRequest(RequestType.JOIN, clientPlayer, gameUUID);
        
        // Both players should get notifications
        hostResponseAnalyzer.expectResponse(PlayerJoinedIntoMyHostedGameResponse.class);
        clientResponseAnalyzer.expectResponse(JoinGameResponse.class);
        
        clientClient.sendRequest(joinRequest);
        
        // Wait for join responses
        PlayerJoinedIntoMyHostedGameResponse hostJoinNotification = 
            (PlayerJoinedIntoMyHostedGameResponse) hostResponseAnalyzer.waitForResponse(5000);
        JoinGameResponse clientJoinResponse = 
            (JoinGameResponse) clientResponseAnalyzer.waitForResponse(5000);
            
        assertNotNull(hostJoinNotification, "Host should be notified of player join");
        assertTrue(clientJoinResponse.isAccepted(), "Client join should be accepted");
        
        LOGGER.info("✅ Client joined game successfully");
        
        // Step 3: Wait for game to start (GameThread should start it automatically)
        LOGGER.info("Step 3: Waiting for game to start");
        
        // Both players should receive game started notifications
        hostResponseAnalyzer.expectResponse(GameStartedResponse.class);
        clientResponseAnalyzer.expectResponse(GameStartedResponse.class);
        
        GameStartedResponse hostGameStarted = 
            (GameStartedResponse) hostResponseAnalyzer.waitForResponse(10000);
        GameStartedResponse clientGameStarted = 
            (GameStartedResponse) clientResponseAnalyzer.waitForResponse(10000);
            
        assertNotNull(hostGameStarted, "Host should receive game started notification");
        assertNotNull(clientGameStarted, "Client should receive game started notification");
        
        LOGGER.info("✅ Game started successfully");
        
        // Step 4: HOST MAKES FIRST MOVE (e2-e4)
        LOGGER.info("Step 4: Host makes first move e2-e4");
        MoveRequest firstMoveRequest = new MoveRequest(gameUUID, hostPlayerUUID, RequestType.MOVE, "e2-e4");
        
        // Both players should receive move responses
        hostResponseAnalyzer.expectResponse(MoveResponse.class);
        clientResponseAnalyzer.expectResponse(MoveResponse.class);
        
        hostClient.sendRequest(firstMoveRequest);
        
        // Wait for move responses
        MoveResponse hostMoveResponse = (MoveResponse) hostResponseAnalyzer.waitForResponse(5000);
        MoveResponse clientMoveResponse = (MoveResponse) clientResponseAnalyzer.waitForResponse(5000);
        
        assertNotNull(hostMoveResponse, "Host should receive move response");
        assertNotNull(clientMoveResponse, "Client should receive move response");
        
        // CRITICAL: Verify both responses indicate success
        assertTrue(hostMoveResponse.isMoveAccepted(), 
            "Host move response should show success. Message: " + hostMoveResponse.getMessage());
        assertTrue(clientMoveResponse.isMoveAccepted(), 
            "Client move response should show success. Message: " + clientMoveResponse.getMessage());
            
        LOGGER.info("✅ Both players received move responses");
        
        // Step 5: CRITICAL VERIFICATION - Check GameDTO content
        LOGGER.info("Step 5: CRITICAL - Verifying GameDTO board states");
        
        ChessGame hostGameState = hostMoveResponse.getGameDTO();
        ChessGame clientGameState = clientMoveResponse.getGameDTO();
        
        assertNotNull(hostGameState, "Host should receive GameDTO");
        assertNotNull(clientGameState, "Client should receive GameDTO");
        assertNotNull(hostGameState.getGameBoard(), "Host GameDTO should have board");
        assertNotNull(clientGameState.getGameBoard(), "Client GameDTO should have board");
        
        // THE CRITICAL TEST: Verify board state after e2-e4 move
        logBoardState("HOST received GameDTO", hostGameState.getGameBoard());
        logBoardState("CLIENT received GameDTO", clientGameState.getGameBoard());
        
        // Check e2 position (should be empty after move)
        de.chess.model.Piece hostE2Piece = hostGameState.getGameBoard().getPiece("e2");
        de.chess.model.Piece clientE2Piece = clientGameState.getGameBoard().getPiece("e2");
        
        // Check e4 position (should have white pawn after move)
        de.chess.model.Piece hostE4Piece = hostGameState.getGameBoard().getPiece("e4");
        de.chess.model.Piece clientE4Piece = clientGameState.getGameBoard().getPiece("e4");
        
        LOGGER.info("HOST GameDTO - e2: " + (hostE2Piece != null ? hostE2Piece.toString() : "EMPTY"));
        LOGGER.info("HOST GameDTO - e4: " + (hostE4Piece != null ? hostE4Piece.toString() : "EMPTY"));
        LOGGER.info("CLIENT GameDTO - e2: " + (clientE2Piece != null ? clientE2Piece.toString() : "EMPTY"));
        LOGGER.info("CLIENT GameDTO - e4: " + (clientE4Piece != null ? clientE4Piece.toString() : "EMPTY"));
        
        // CRITICAL ASSERTIONS - This will reveal the bug if it exists
        if (hostE2Piece != null) {
            fail("🐛 BUG FOUND: HOST GameDTO still has piece at e2 after move - starting position not updated!");
        }
        if (hostE4Piece == null) {
            fail("🐛 BUG FOUND: HOST GameDTO has no piece at e4 after move - move not applied!");
        }
        if (clientE2Piece != null) {
            fail("🐛 BUG FOUND: CLIENT GameDTO still has piece at e2 after move - starting position not updated!");
        }
        if (clientE4Piece == null) {
            fail("🐛 BUG FOUND: CLIENT GameDTO has no piece at e4 after move - move not applied!");
        }
        
        // Verify piece types are correct
        assertEquals(de.chess.model.PieceType.PAWN, hostE4Piece.getPieceType(), "HOST: e4 should have pawn");
        assertEquals(de.chess.model.ChessColor.WHITE, hostE4Piece.getColor(), "HOST: e4 should have white pawn");
        assertEquals(de.chess.model.PieceType.PAWN, clientE4Piece.getPieceType(), "CLIENT: e4 should have pawn");
        assertEquals(de.chess.model.ChessColor.WHITE, clientE4Piece.getColor(), "CLIENT: e4 should have white pawn");
        
        LOGGER.info("✅ CRITICAL TEST PASSED: Both clients received correct board state");
        
        // Step 6: CLIENT MAKES SECOND MOVE (e7-e5)
        LOGGER.info("Step 6: Client makes second move e7-e5");
        MoveRequest secondMoveRequest = new MoveRequest(gameUUID, clientPlayerUUID, RequestType.MOVE, "e7-e5");
        
        // Clear previous responses and expect new ones
        hostResponseAnalyzer.clearResponses();
        clientResponseAnalyzer.clearResponses();
        hostResponseAnalyzer.expectResponse(MoveResponse.class);
        clientResponseAnalyzer.expectResponse(MoveResponse.class);
        
        clientClient.sendRequest(secondMoveRequest);
        
        // Wait for second move responses
        MoveResponse hostSecondMoveResponse = (MoveResponse) hostResponseAnalyzer.waitForResponse(5000);
        MoveResponse clientSecondMoveResponse = (MoveResponse) clientResponseAnalyzer.waitForResponse(5000);
        
        assertNotNull(hostSecondMoveResponse, "Host should receive second move response");
        assertNotNull(clientSecondMoveResponse, "Client should receive second move response");
        assertTrue(hostSecondMoveResponse.isMoveAccepted(), "Host should see second move as accepted");
        assertTrue(clientSecondMoveResponse.isMoveAccepted(), "Client should see second move as accepted");
        
        // Step 7: Verify final board state has both moves
        LOGGER.info("Step 7: Verifying final board state with both moves");
        
        ChessGame finalHostState = hostSecondMoveResponse.getGameDTO();
        ChessGame finalClientState = clientSecondMoveResponse.getGameDTO();
        
        logBoardState("FINAL HOST GameDTO", finalHostState.getGameBoard());
        logBoardState("FINAL CLIENT GameDTO", finalClientState.getGameBoard());
        
        // Verify both moves are present
        assertNull(finalHostState.getGameBoard().getPiece("e2"), "HOST final: e2 should be empty");
        assertNotNull(finalHostState.getGameBoard().getPiece("e4"), "HOST final: e4 should have white pawn");
        assertNull(finalHostState.getGameBoard().getPiece("e7"), "HOST final: e7 should be empty");
        assertNotNull(finalHostState.getGameBoard().getPiece("e5"), "HOST final: e5 should have black pawn");
        
        assertNull(finalClientState.getGameBoard().getPiece("e2"), "CLIENT final: e2 should be empty");
        assertNotNull(finalClientState.getGameBoard().getPiece("e4"), "CLIENT final: e4 should have white pawn");
        assertNull(finalClientState.getGameBoard().getPiece("e7"), "CLIENT final: e7 should be empty");
        assertNotNull(finalClientState.getGameBoard().getPiece("e5"), "CLIENT final: e5 should have black pawn");
        
        LOGGER.info("🎉 REAL E2E TEST PASSED: Complete move synchronization working correctly!");
    }
    
    private void startRealServer() throws IOException, InterruptedException {
        LOGGER.info("Starting real GameServer on port " + TEST_SERVER_PORT);
        
        RequestAnalyzer requestAnalyzer = new RequestAnalyzer();
        InetAddress localhost = InetAddress.getLocalHost();
        
        server = new GameServer(TEST_SERVER_PORT, 10, localhost, requestAnalyzer);
        serverThread = new Thread(server);
        serverThread.start();
        
        // Wait for server socket to actually start listening
        // Try to connect to verify server is ready
        for (int attempts = 0; attempts < 10; attempts++) {
            try (java.net.Socket testSocket = new java.net.Socket()) {
                testSocket.connect(new java.net.InetSocketAddress("localhost", TEST_SERVER_PORT), 1000);
                LOGGER.info("Real GameServer is ready and accepting connections");
                return;
            } catch (IOException e) {
                Thread.sleep(500);
            }
        }
        
        throw new RuntimeException("Server failed to start within 5 seconds");
    }
    
    private void connectRealClients() throws InterruptedException {
        LOGGER.info("Connecting real clients to server");
        
        // Create and connect host client
        hostClient = GameClient.getAndIniTInstance("localhost", TEST_SERVER_PORT, hostResponseAnalyzer, hostPlayerUUID);
        assertTrue(hostClient.execute(), "Host client should connect successfully");
        
        // Reset singleton to create second client
        try {
            java.lang.reflect.Field instanceField = GameClient.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException("Could not reset GameClient singleton", e);
        }
        
        // Create and connect client client
        clientClient = GameClient.getAndIniTInstance("localhost", TEST_SERVER_PORT, clientResponseAnalyzer, clientPlayerUUID);
        assertTrue(clientClient.execute(), "Client client should connect successfully");
        
        // Wait for connections to stabilize
        Thread.sleep(1000);
        
        LOGGER.info("Both real clients connected successfully");
    }
    
    private void logBoardState(String description, de.chess.model.GameBoard board) {
        LOGGER.info("=== " + description + " ===");
        if (board == null) {
            LOGGER.warning("Board is NULL!");
            return;
        }
        
        StringBuilder boardStr = new StringBuilder("Board:\n");
        for (int row = 0; row < 8; row++) {
            boardStr.append("Row ").append(8 - row).append(": ");
            for (int col = 0; col < 8; col++) {
                String field = ((char)('a' + col)) + String.valueOf(8 - row);
                de.chess.model.Piece piece = board.getPiece(field);
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
    
    /**
     * Test ResponseAnalyzer that captures responses for verification
     */
    private static class TestResponseAnalyzer implements IResponseAnalyzer {
        private final String clientName;
        private final AtomicReference<Response> lastResponse = new AtomicReference<>();
        private final CountDownLatch responseLatch = new CountDownLatch(1);
        private Class<?> expectedResponseType = null;
        private volatile CountDownLatch currentLatch = null;
        
        public TestResponseAnalyzer(String clientName) {
            this.clientName = clientName;
        }
        
        @Override
        public void analyze(Response response) {
            LOGGER.info(clientName + " received response: " + response.getClass().getSimpleName() 
                + " (type: " + response.getRequestType() + ")");
                
            lastResponse.set(response);
            
            // Signal waiting test if this is the expected response type
            if (currentLatch != null && (expectedResponseType == null || expectedResponseType.isInstance(response))) {
                currentLatch.countDown();
            }
        }
        
        public void expectResponse(Class<?> responseType) {
            expectedResponseType = responseType;
            currentLatch = new CountDownLatch(1);
            LOGGER.info(clientName + " expecting response: " + responseType.getSimpleName());
        }
        
        public Response waitForResponse(long timeoutMs) throws InterruptedException {
            if (currentLatch == null) {
                throw new IllegalStateException("Must call expectResponse() first");
            }
            
            boolean received = currentLatch.await(timeoutMs, TimeUnit.MILLISECONDS);
            if (!received) {
                throw new RuntimeException(clientName + " did not receive expected response within " + timeoutMs + "ms");
            }
            
            Response response = lastResponse.get();
            LOGGER.info(clientName + " successfully received: " + response.getClass().getSimpleName());
            return response;
        }
        
        public void clearResponses() {
            lastResponse.set(null);
            expectedResponseType = null;
            currentLatch = null;
        }
        
        @Override
        public Response analyze(String jsonResponse) {
            return null; // Not used in this implementation
        }
    }
}