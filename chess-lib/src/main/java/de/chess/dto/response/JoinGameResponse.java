package de.chess.dto.response;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.dto.RequestType;

import java.util.Optional;

public class JoinGameResponse extends Response {

  private boolean isAccepted;



  public JoinGameResponse(RequestType requestType, boolean isAccepted) {
    super(requestType);
    this.isAccepted = isAccepted;
  }

  public JoinGameResponse(ChessGame chessGame, RequestType requestType, boolean isAccepted) {
    super(chessGame.getUuid(), requestType);
    setGameDTO(chessGame);
    this.isAccepted = isAccepted;
  }

  public void setAccepted(boolean accepted) {
    this.isAccepted = accepted;
  }

  public boolean isAccepted() {
    return isAccepted;
  }


}
