package de.chess.app.e2e;

import de.chess.app.server.RequestAnalyzer;
import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.dto.RequestType;
import de.chess.dto.request.MoveRequest;
import de.chess.dto.request.OpenGameRequest;
import de.chess.dto.request.JoinGameRequest;
import de.chess.dto.response.MoveResponse;
import de.chess.dto.response.OpenGameResponse;
import de.chess.dto.response.JoinGameResponse;
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
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simplified E2E test that bypasses network setup and focuses on the serialization bug
 */
class SimplifiedE2ETest {
    
    private static final Logger LOGGER = Logger.getGlobal();
    
    private RequestAnalyzer requestAnalyzer;
    private PlayerManager playerManager;
    
    // Test players
    private Player hostPlayer;
    private Player clientPlayer;
    private UUID hostPlayerUUID;
    private UUID clientPlayerUUID;
    
    // Mock network components for real serialization testing
    private MockClientThread hostClientThread;
    private MockClientThread clientClientThread;
    
    @BeforeEach
    void setUp() {
        LOGGER.info("=== Setting up Simplified E2E Test ===");
        
        // Reset and setup server components
        requestAnalyzer = new RequestAnalyzer();
        playerManager = PlayerManager.getInstance();
        playerManager.reset();
        
        // Create test players
        hostPlayerUUID = UUID.randomUUID();
        clientPlayerUUID = UUID.randomUUID();
        hostPlayer = new Player(hostPlayerUUID, "HostPlayer", 1200);
        clientPlayer = new Player(clientPlayerUUID, "ClientPlayer", 1300);
        
        // Create mock client threads that do REAL serialization
        hostClientThread = new MockClientThread();
        clientClientThread = new MockClientThread();
        
        // Register players
        playerManager.addPlayerConnection(new PlayerConnection(hostPlayerUUID, hostClientThread));
        playerManager.addPlayerConnection(new PlayerConnection(clientPlayerUUID, clientClientThread));
        
        LOGGER.info("=== Simplified E2E Test Setup Complete ===");
    }
    
    @AfterEach
    void tearDown() {
        if (playerManager != null) {
            playerManager.reset();
        }
    }
    
    @Test
    @DisplayName("Simplified E2E: Test real serialization of move responses with actual TCP-like behavior")
    void testRealSerializationOfMoveResponses() throws IOException, ClassNotFoundException, InterruptedException {
        LOGGER.info("=== Testing Real Move Response Serialization ===");
        
        // Step 1: Host creates game
        LOGGER.info("Step 1: Creating game");
        ChessGame gameTemplate = new ChessGame("SimpleE2E", hostPlayer, "10", ChessColor.WHITE, GameType.FUN);
        OpenGameRequest openRequest = new OpenGameRequest(gameTemplate, hostPlayerUUID);
        
        OpenGameResponse openResponse = (OpenGameResponse) requestAnalyzer.analyze(openRequest, hostClientThread);
        assertTrue(openResponse.isGranted(), "Game creation should succeed");
        UUID gameUUID = openResponse.getGameDTO().getUuid();
        
        // Step 2: Client joins game
        LOGGER.info("Step 2: Client joining game");
        JoinGameRequest joinRequest = new JoinGameRequest(RequestType.JOIN, clientPlayer, gameUUID);
        
        JoinGameResponse joinResponse = (JoinGameResponse) requestAnalyzer.analyze(joinRequest, clientClientThread);
        assertTrue(joinResponse.isAccepted(), "Join should succeed");
        
        // Wait for GameThread to start the game automatically
        Thread.sleep(2000);
        
        // Step 3: Make a move and test REAL serialization
        LOGGER.info("Step 3: Making move with REAL serialization test");
        MoveRequest moveRequest = new MoveRequest(gameUUID, hostPlayerUUID, RequestType.MOVE, "e2-e4");
        
        // Clear any previous responses
        hostClientThread.clearResponses();
        clientClientThread.clearResponses();
        
        // Process the move request
        MoveResponse directResponse = (MoveResponse) requestAnalyzer.analyze(moveRequest, hostClientThread);
        
        // Step 4: CRITICAL - Test the direct response first
        LOGGER.info("Step 4: Testing direct response from server");
        assertNotNull(directResponse, "Direct response should not be null");
        assertTrue(directResponse.isMoveAccepted(), "Direct response should show move accepted");
        assertNotNull(directResponse.getGameDTO(), "Direct response should have GameDTO");
        
        ChessGame directGameState = directResponse.getGameDTO();
        logBoardState("Direct response GameDTO", directGameState.getGameBoard());
        
        // Check direct response board state
        de.chess.model.Piece directE2 = directGameState.getGameBoard().getPiece("e2");
        de.chess.model.Piece directE4 = directGameState.getGameBoard().getPiece("e4");
        
        LOGGER.info("DIRECT response e2: " + (directE2 != null ? directE2.toString() : "EMPTY"));
        LOGGER.info("DIRECT response e4: " + (directE4 != null ? directE4.toString() : "EMPTY"));
        
        if (directE2 != null) {
            fail("🐛 BUG FOUND: Direct response still has piece at e2 - server logic is broken!");
        }
        if (directE4 == null) {
            fail("🐛 BUG FOUND: Direct response has no piece at e4 - server logic is broken!");
        }
        
        LOGGER.info("✅ Direct response is correct");
        
        // Step 5: CRITICAL - Test serialization of notifications
        LOGGER.info("Step 5: Testing serialized notifications to both clients");
        
        // Wait for notifications to be sent
        Thread.sleep(1000);
        
        // Both clients should have received responses through the notification system
        assertTrue(hostClientThread.hasReceivedResponse(), "Host should have received notification");
        assertTrue(clientClientThread.hasReceivedResponse(), "Client should have received notification");
        
        // Step 6: CRITICAL - Test the SERIALIZED response content
        LOGGER.info("Step 6: Testing SERIALIZED response content");
        
        MoveResponse hostSerializedResponse = (MoveResponse) hostClientThread.getLastResponse();
        MoveResponse clientSerializedResponse = (MoveResponse) clientClientThread.getLastResponse();
        
        assertNotNull(hostSerializedResponse, "Host serialized response should not be null");
        assertNotNull(clientSerializedResponse, "Client serialized response should not be null");
        
        // Test the GameDTO in serialized responses
        ChessGame hostSerializedGame = hostSerializedResponse.getGameDTO();
        ChessGame clientSerializedGame = clientSerializedResponse.getGameDTO();
        
        assertNotNull(hostSerializedGame, "Host serialized GameDTO should not be null");
        assertNotNull(clientSerializedGame, "Client serialized GameDTO should not be null");
        
        logBoardState("HOST serialized GameDTO", hostSerializedGame.getGameBoard());
        logBoardState("CLIENT serialized GameDTO", clientSerializedGame.getGameBoard());
        
        // CRITICAL ASSERTIONS: Test the actual serialized board state
        de.chess.model.Piece hostSerE2 = hostSerializedGame.getGameBoard().getPiece("e2");
        de.chess.model.Piece hostSerE4 = hostSerializedGame.getGameBoard().getPiece("e4");
        de.chess.model.Piece clientSerE2 = clientSerializedGame.getGameBoard().getPiece("e2");
        de.chess.model.Piece clientSerE4 = clientSerializedGame.getGameBoard().getPiece("e4");
        
        LOGGER.info("HOST serialized e2: " + (hostSerE2 != null ? hostSerE2.toString() : "EMPTY"));
        LOGGER.info("HOST serialized e4: " + (hostSerE4 != null ? hostSerE4.toString() : "EMPTY"));
        LOGGER.info("CLIENT serialized e2: " + (clientSerE2 != null ? clientSerE2.toString() : "EMPTY"));
        LOGGER.info("CLIENT serialized e4: " + (clientSerE4 != null ? clientSerE4.toString() : "EMPTY"));
        
        // These are the CRITICAL assertions that will reveal the bug
        if (hostSerE2 != null) {
            fail("🐛 BUG FOUND: HOST serialized response still has piece at e2 - serialization issue!");
        }
        if (hostSerE4 == null) {
            fail("🐛 BUG FOUND: HOST serialized response has no piece at e4 - serialization issue!");
        }
        if (clientSerE2 != null) {
            fail("🐛 BUG FOUND: CLIENT serialized response still has piece at e2 - serialization issue!");
        }
        if (clientSerE4 == null) {
            fail("🐛 BUG FOUND: CLIENT serialized response has no piece at e4 - serialization issue!");
        }
        
        // Verify piece types
        assertEquals(de.chess.model.PieceType.PAWN, hostSerE4.getPieceType(), "HOST: e4 should have pawn");
        assertEquals(de.chess.model.ChessColor.WHITE, hostSerE4.getColor(), "HOST: e4 should have white pawn");
        assertEquals(de.chess.model.PieceType.PAWN, clientSerE4.getPieceType(), "CLIENT: e4 should have pawn");
        assertEquals(de.chess.model.ChessColor.WHITE, clientSerE4.getColor(), "CLIENT: e4 should have white pawn");
        
        LOGGER.info("🎉 SIMPLIFIED E2E TEST PASSED: Real serialization working correctly!");
    }
    
    private void logBoardState(String description, de.chess.model.GameBoard board) {
        LOGGER.info("=== " + description + " ===");
        if (board == null) {
            LOGGER.warning("Board is NULL!");
            return;
        }
        
        // Log specific positions for e2-e4 move
        de.chess.model.Piece e2Piece = board.getPiece("e2");
        de.chess.model.Piece e4Piece = board.getPiece("e4");
        
        LOGGER.info("e2: " + (e2Piece != null ? e2Piece.getPieceType() + "_" + e2Piece.getColor() : "EMPTY"));
        LOGGER.info("e4: " + (e4Piece != null ? e4Piece.getPieceType() + "_" + e4Piece.getColor() : "EMPTY"));
        
        // Log full board for context
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
     * Mock ClientThread that performs REAL Java serialization like actual TCP connections
     */
    private static class MockClientThread extends ClientThread {
        private volatile MoveResponse lastResponse = null;
        private volatile boolean hasResponse = false;
        
        public MockClientThread() {
            super(new MockSocket(), null);
        }
        
        @Override
        public void sendResponse(de.chess.dto.response.Response response) {
            LOGGER.info("MockClientThread performing REAL serialization for: " + response.getClass().getSimpleName());
            
            try {
                // Step 1: Serialize the response (like real TCP would do)
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(response);
                oos.close();
                
                LOGGER.info("Serialization completed, size: " + baos.size() + " bytes");
                
                // Step 2: Deserialize the response (like client would do)
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                de.chess.dto.response.Response deserializedResponse = (de.chess.dto.response.Response) ois.readObject();
                ois.close();
                
                LOGGER.info("Deserialization completed");
                
                // Step 3: Store the DESERIALIZED response (this is what client actually gets)
                if (deserializedResponse instanceof MoveResponse) {
                    this.lastResponse = (MoveResponse) deserializedResponse;
                    this.hasResponse = true;
                    LOGGER.info("Stored deserialized MoveResponse");
                } else {
                    LOGGER.info("Response is not MoveResponse: " + deserializedResponse.getClass().getSimpleName());
                }
                
            } catch (Exception e) {
                LOGGER.severe("Serialization/deserialization failed: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Serialization test failed", e);
            }
        }
        
        public boolean hasReceivedResponse() {
            return hasResponse;
        }
        
        public MoveResponse getLastResponse() {
            return lastResponse;
        }
        
        public void clearResponses() {
            lastResponse = null;
            hasResponse = false;
        }
        
        @Override
        public void run() {
            // Do nothing - we're not actually running a network thread
        }
    }
    
    /**
     * Mock Socket for testing
     */
    private static class MockSocket extends Socket {
        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(new byte[0]);
        }
        
        @Override
        public OutputStream getOutputStream() throws IOException {
            return new ByteArrayOutputStream();
        }
        
        @Override
        public boolean isClosed() {
            return false;
        }
    }
}