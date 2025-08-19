package de.chess.app.debug;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.dto.RequestType;
import de.chess.dto.response.MoveResponse;
import de.chess.model.ChessColor;
import de.chess.model.GameBoard;
import de.chess.model.GameType;
import de.chess.model.Piece;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.*;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Debug test to identify the exact serialization issue
 */
class SerializationDebugTest {
    
    private static final Logger LOGGER = Logger.getGlobal();
    
    @Test
    @DisplayName("Debug: Serialize/deserialize GameBoard after move")
    void testGameBoardSerialization() throws IOException, ClassNotFoundException {
        LOGGER.info("=== Testing GameBoard Serialization ===");
        
        // Step 1: Create a game with a move
        Player host = new Player(UUID.randomUUID(), "Host", 1200);
        Player client = new Player(UUID.randomUUID(), "Client", 1300);
        
        ChessGame game = new ChessGame("DebugGame", host, client, "10", ChessColor.WHITE, GameType.FUN);
        game.setUuid(UUID.randomUUID());
        game.initGameBoard(); // This sets up starting position
        
        // Step 2: Log initial state
        logGameBoardState("Initial state", game.getGameBoard());
        
        // Step 3: Make a move (e2-e4)
        GameBoard board = game.getGameBoard();
        Piece pawn = board.getPiece("e2");
        assertNotNull(pawn, "Should have pawn at e2 initially");
        assertEquals(de.chess.model.PieceType.PAWN, pawn.getPieceType());
        assertEquals(de.chess.model.ChessColor.WHITE, pawn.getColor());
        
        // Execute move
        board.removePiece("e2");
        board.putPiece("e4", pawn);
        game.switchTurn();
        
        // Step 4: Log state after move
        logGameBoardState("After e2-e4 move", game.getGameBoard());
        
        // Step 5: Verify move was applied
        assertNull(game.getGameBoard().getPiece("e2"), "e2 should be empty");
        assertNotNull(game.getGameBoard().getPiece("e4"), "e4 should have pawn");
        
        // Step 6: Test serialization via MoveResponse
        LOGGER.info("=== Testing MoveResponse Serialization ===");
        MoveResponse response = new MoveResponse(game.getUuid(), RequestType.MOVE, true, "e2-e4", game);
        
        // Serialize the response
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(response);
        oos.close();
        
        // Deserialize the response
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        MoveResponse deserializedResponse = (MoveResponse) ois.readObject();
        ois.close();
        
        // Step 7: Check deserialized state
        ChessGame deserializedGame = deserializedResponse.getGameDTO();
        assertNotNull(deserializedGame, "Deserialized game should not be null");
        assertNotNull(deserializedGame.getGameBoard(), "Deserialized game board should not be null");
        
        logGameBoardState("Deserialized state", deserializedGame.getGameBoard());
        
        // Step 8: Critical verification - this should reveal the bug
        Piece deserializedE2Piece = deserializedGame.getGameBoard().getPiece("e2");
        Piece deserializedE4Piece = deserializedGame.getGameBoard().getPiece("e4");
        
        LOGGER.info("CRITICAL CHECK:");
        LOGGER.info("Deserialized e2 piece: " + (deserializedE2Piece != null ? deserializedE2Piece.toString() : "NULL"));
        LOGGER.info("Deserialized e4 piece: " + (deserializedE4Piece != null ? deserializedE4Piece.toString() : "NULL"));
        
        if (deserializedE2Piece != null) {
            fail("BUG CONFIRMED: After serialization, e2 still has a piece - move was not preserved!");
        }
        if (deserializedE4Piece == null) {
            fail("BUG CONFIRMED: After serialization, e4 has no piece - move was not preserved!");
        }
        
        // If we get here, serialization worked correctly
        assertEquals(de.chess.model.PieceType.PAWN, deserializedE4Piece.getPieceType(), "e4 should have pawn");
        assertEquals(de.chess.model.ChessColor.WHITE, deserializedE4Piece.getColor(), "e4 should have white pawn");
        
        LOGGER.info("✅ Serialization test PASSED - move was preserved correctly");
    }
    
    @Test
    @DisplayName("Debug: Test direct GameBoard serialization")
    void testDirectGameBoardSerialization() throws IOException, ClassNotFoundException {
        LOGGER.info("=== Testing Direct GameBoard Serialization ===");
        
        // Create and modify a GameBoard directly
        GameBoard board = new GameBoard();
        board.initialDistibutionOfChessPieces();
        
        logGameBoardState("Initial board", board);
        
        // Make move
        Piece pawn = board.getPiece("e2");
        board.removePiece("e2");
        board.putPiece("e4", pawn);
        
        logGameBoardState("After move", board);
        
        // Serialize directly
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(board);
        oos.close();
        
        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        GameBoard deserializedBoard = (GameBoard) ois.readObject();
        ois.close();
        
        logGameBoardState("Deserialized board", deserializedBoard);
        
        // Verify
        assertNull(deserializedBoard.getPiece("e2"), "Direct serialization: e2 should be empty");
        assertNotNull(deserializedBoard.getPiece("e4"), "Direct serialization: e4 should have pawn");
        
        LOGGER.info("✅ Direct GameBoard serialization PASSED");
    }
    
    private void logGameBoardState(String description, GameBoard board) {
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