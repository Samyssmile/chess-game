package de.chess.dto.response;

import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;


public class JoinGameResponse extends Response {

    private boolean accepted;

    public JoinGameResponse(ChessGame chessGame, RequestType requestType, boolean accepted) {
        super(chessGame.getUuid(), requestType);
        setGameDTO(chessGame);
        this.accepted = accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
