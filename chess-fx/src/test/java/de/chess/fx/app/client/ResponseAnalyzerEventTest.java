package de.chess.fx.app.client;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.dto.RequestType;
import de.chess.dto.response.MoveResponse;
import de.chess.fx.app.handler.EventData;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.fx.app.handler.IChannel;
import de.chess.model.ChessColor;
import de.chess.model.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test that catches the REAL bug: MOVE_DONE events not being fired properly
 */
class ResponseAnalyzerEventTest {
    
    private ResponseAnalyzer responseAnalyzer;
    private TestEventListener eventListener;
    private ChessGame testGame;
    
    @BeforeEach
    void setUp() {
        responseAnalyzer = new ResponseAnalyzer();
        eventListener = new TestEventListener();
        
        // Register for MOVE_DONE events
        EventHandler.getInstance().registerForEvent(eventListener, EventType.MOVE_DONE);
        
        // Create test game
        Player hostPlayer = new Player(UUID.randomUUID(), "Host", 1200);
        testGame = new ChessGame("TestGame", hostPlayer, "10", ChessColor.WHITE, GameType.FUN);
        testGame.setUuid(UUID.randomUUID());
        testGame.initGameBoard();
    }
    
    @Test
    @DisplayName("BUG TEST: MoveResponse with GameDTO should fire MOVE_DONE event")
    void testMoveResponseWithGameDTOFiresEvent() throws InterruptedException {
        // Create MoveResponse with GameDTO (this should work)
        MoveResponse moveResponseWithGame = new MoveResponse(
            testGame.getUuid(),
            RequestType.MOVE,
            true, // moveAccepted
            "e2-e4",
            testGame // GameDTO included
        );
        
        // Analyze the response
        responseAnalyzer.analyze(moveResponseWithGame);
        
        // Wait for event
        assertTrue(eventListener.waitForEvent(2, TimeUnit.SECONDS), 
            "MOVE_DONE event should be fired when MoveResponse has GameDTO");
        
        // Verify event data
        EventData eventData = eventListener.getEventData();
        assertNotNull(eventData, "Event data should not be null");
        assertEquals(testGame, eventData.getData(), "Event should contain the game data");
    }
    
    @Test
    @DisplayName("BUG TEST: MoveResponse without GameDTO should NOT fire MOVE_DONE event")
    void testMoveResponseWithoutGameDTODoesNotFireEvent() throws InterruptedException {
        // Create MoveResponse without GameDTO (this is the bug!)
        MoveResponse moveResponseWithoutGame = new MoveResponse(
            testGame.getUuid(),
            RequestType.MOVE,
            true, // moveAccepted
            "e2-e4",
            "Move successful" // message instead of GameDTO
        );
        
        // Analyze the response
        responseAnalyzer.analyze(moveResponseWithoutGame);
        
        // Wait for event (should timeout)
        assertFalse(eventListener.waitForEvent(1, TimeUnit.SECONDS), 
            "MOVE_DONE event should NOT be fired when MoveResponse has no GameDTO - THIS IS THE BUG!");
    }
    
    @Test
    @DisplayName("BUG TEST: Verify MoveResponse.getGameDTO() returns null when constructed without game")
    void testMoveResponseGameDTOIsNull() {
        // This test shows the root cause
        MoveResponse responseWithoutGame = new MoveResponse(
            testGame.getUuid(),
            RequestType.MOVE,
            true,
            "e2-e4",
            "Success message"
        );
        
        // This is the problem! getGameDTO() returns null
        assertNull(responseWithoutGame.getGameDTO(), 
            "MoveResponse constructed without GameDTO should return null - THIS BREAKS EVENT FIRING");
        
        MoveResponse responseWithGame = new MoveResponse(
            testGame.getUuid(),
            RequestType.MOVE,
            true,
            "e2-e4",
            testGame
        );
        
        assertNotNull(responseWithGame.getGameDTO(), 
            "MoveResponse constructed with GameDTO should return the game");
    }
    
    /**
     * Test event listener that captures MOVE_DONE events
     */
    private static class TestEventListener implements IChannel {
        private final CountDownLatch eventLatch = new CountDownLatch(1);
        private final AtomicReference<EventData> capturedEventData = new AtomicReference<>();
        
        @Override
        public void update(EventType eventType, EventData eventData) {
            if (eventType == EventType.MOVE_DONE) {
                capturedEventData.set(eventData);
                eventLatch.countDown();
            }
        }
        
        public boolean waitForEvent(long timeout, TimeUnit unit) throws InterruptedException {
            return eventLatch.await(timeout, unit);
        }
        
        public EventData getEventData() {
            return capturedEventData.get();
        }
    }
}