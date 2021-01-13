package de.chess.dto.response;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.dto.RequestType;

import java.util.Optional;

public class JoinGameResponse extends Response {

  private ChessGame chessGame;
  private boolean isAccepted;
  private Player hostPlayer;
  private Player clientPlayer;


  public JoinGameResponse(RequestType requestType, boolean isAccepted) {
    super(requestType);
    this.isAccepted = isAccepted;
  }

  public JoinGameResponse(ChessGame chessGame, RequestType requestType, boolean isAccepted) {
    super(chessGame.getUuid(), requestType);
    setGameDTO(chessGame);
    this.hostPlayer = chessGame.getHostPlayer();
    this.clientPlayer = chessGame.getClientPlayer();
    this.chessGame = chessGame;
    this.isAccepted = isAccepted;
  }

  public void setAccepted(boolean accepted) {
    this.isAccepted = accepted;
  }

  public boolean isAccepted() {
    return isAccepted;
  }

  public Player getHostPlayer() {
    return hostPlayer;
  }

  public void setHostPlayer(Player hostPlayer) {
    this.hostPlayer = hostPlayer;
  }

  public Player getClientPlayer() {
    return clientPlayer;
  }

  public void setClientPlayer(Player clientPlayer) {
    this.clientPlayer = clientPlayer;
  }

  public ChessGame getChessGame() {
    return chessGame;
  }

  public void setChessGame(ChessGame chessGame) {
    this.chessGame = chessGame;
  }
}
