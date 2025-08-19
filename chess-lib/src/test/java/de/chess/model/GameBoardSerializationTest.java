package de.chess.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to confirm GameBoard deserialization bug - constructor resets board during deserialization
 */
class GameBoardSerializationTest {
    
    private static final Logger LOGGER = Logger.getGlobal();
    
    @Test
    @DisplayName("GameBoard serialization bug: constructor resets board during deserialization")
    void testGameBoardSerializationBug() throws IOException, ClassNotFoundException {
        LOGGER.info("=== Testing GameBoard serialization bug ===%n");
        
        // Step 1: Create board with starting position
        GameBoard originalBoard = new GameBoard();
        originalBoard.initialDistibutionOfChessPieces();
        
        // Step 2: Apply e2-e4 move (as server would do)
        Piece whitePawn = originalBoard.getPiece("e2");
        assertNotNull(whitePawn, "Should have pawn at e2 initially");
        assertEquals(PieceType.PAWN, whitePawn.getPieceType());
        assertEquals(ChessColor.WHITE, whitePawn.getColor());
        
        originalBoard.removePiece("e2");
        originalBoard.putPiece("e4", whitePawn);
        
        // Verify the move was applied
        assertNull(originalBoard.getPiece("e2"), "e2 should be empty after move");
        assertNotNull(originalBoard.getPiece("e4"), "e4 should have pawn after move");
        assertEquals(PieceType.PAWN, originalBoard.getPiece("e4").getPieceType());
        assertEquals(ChessColor.WHITE, originalBoard.getPiece("e4").getColor());
        
        LOGGER.info("Original board after move:");
        logBoardState("Original board", originalBoard);
        
        // Step 3: Serialize the board (as server would do)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(originalBoard);
        oos.close();
        
        byte[] serializedData = baos.toByteArray();
        LOGGER.info("Board serialized, size: " + serializedData.length + " bytes");
        
        // Step 4: Deserialize the board (as client would do)
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        ObjectInputStream ois = new ObjectInputStream(bais);
        GameBoard deserializedBoard = (GameBoard) ois.readObject();
        ois.close();
        
        LOGGER.info("Board deserialized");
        logBoardState("Deserialized board", deserializedBoard);
        
        // Step 5: CRITICAL TEST - Check if the bug occurs
        Piece deserializedE2 = deserializedBoard.getPiece("e2");
        Piece deserializedE4 = deserializedBoard.getPiece("e4");
        
        LOGGER.info("Deserialized board e2: " + (deserializedE2 != null ? deserializedE2.toString() : "EMPTY"));
        LOGGER.info("Deserialized board e4: " + (deserializedE4 != null ? deserializedE4.toString() : "EMPTY"));
        
        // These assertions will FAIL if the bug exists
        if (deserializedE2 != null) {
            fail("🐛 BUG CONFIRMED: Deserialized board still has piece at e2 - constructor reset the board!");
        }
        if (deserializedE4 == null) {
            fail("🐛 BUG CONFIRMED: Deserialized board has no piece at e4 - constructor reset the board!");
        }
        
        // If we get here, the bug is fixed
        assertEquals(PieceType.PAWN, deserializedE4.getPieceType(), "e4 should have pawn after deserialization");
        assertEquals(ChessColor.WHITE, deserializedE4.getColor(), "e4 should have white pawn after deserialization");
        
        LOGGER.info("✅ GameBoard serialization test passed - no constructor bug!");
    }
    
    private void logBoardState(String description, GameBoard board) {
        LOGGER.info("=== " + description + " ===");
        
        // Log specific positions for e2-e4 move
        Piece e2Piece = board.getPiece("e2");
        Piece e4Piece = board.getPiece("e4");
        
        LOGGER.info("e2: " + (e2Piece != null ? e2Piece.getPieceType() + "_" + e2Piece.getColor() : "EMPTY"));
        LOGGER.info("e4: " + (e4Piece != null ? e4Piece.getPieceType() + "_" + e4Piece.getColor() : "EMPTY"));
        
        // Log key pieces to verify it's not completely empty
        LOGGER.info("a1: " + (board.getPiece("a1") != null ? board.getPiece("a1").getPieceType() + "_" + board.getPiece("a1").getColor() : "EMPTY"));
        LOGGER.info("d1: " + (board.getPiece("d1") != null ? board.getPiece("d1").getPieceType() + "_" + board.getPiece("d1").getColor() : "EMPTY"));
        LOGGER.info("e8: " + (board.getPiece("e8") != null ? board.getPiece("e8").getPieceType() + "_" + board.getPiece("e8").getColor() : "EMPTY"));
    }
}