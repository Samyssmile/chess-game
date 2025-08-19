package de.chess.dto.response;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;

import java.util.UUID;

public class GameStartedResponse extends Response {
    
    private ChessGame chessGame;
    
    public GameStartedResponse(UUID gameUUID, RequestType responseType, ChessGame chessGame) {
        super(gameUUID, responseType);
        this.chessGame = chessGame;
    }
    
    public ChessGame getChessGame() {
        return chessGame;
    }
    
    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }
}