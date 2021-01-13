package de.chess.dto;

import de.chess.io.server.ClientThread;
import de.chess.model.ChessColor;
import de.chess.model.GameBoard;
import de.chess.model.GameType;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ChessGame implements Serializable {

  private UUID uuid;
  private String gameName;

  private Player[] players = new Player[2];
  private String timeLimit;
  private ChessColor hostColor;
  private GameType gameType;
  private GameStatus gameStatus;
  private GameBoard gameBoard;
  private transient ClientThread hostClientThread;

  public ChessGame(
      String gameName,
      Player hostPlayer,
      String timeLimit,
      ChessColor hostColor,
      GameType gameType) {
    this.gameName = gameName;
    setHostPlayer(hostPlayer);
    this.timeLimit = timeLimit;
    this.hostColor = hostColor;
    this.gameType = gameType;
    gameStatus = GameStatus.WATING;
  }

  public ChessGame(
          String gameName,
          Player hostPlayer,
          Player clientPlayer,
          String timeLimit,
          ChessColor hostColor,
          GameType gameType) {
    this.gameName = gameName;
    setHostPlayer(hostPlayer);
    setClientPlayer(clientPlayer);
    this.timeLimit = timeLimit;
    this.hostColor = hostColor;
    this.gameType = gameType;
    gameStatus = GameStatus.WATING;
  }


  public GameBoard getGameBoard() {
    return gameBoard;
  }

  public void setGameBoard(GameBoard gameBoard) {
    this.gameBoard = gameBoard;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String getGameName() {
    return gameName;
  }

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public Player[] getPlayers() {
    return players;
  }

  public void setPlayers(Player[] players) {
    this.players = players;
  }

  public String getHostPlayerName() {
    return getHostPlayer().getNickname();
  }

  public void setHostPlayerName(String hostPlayerName) {
    getHostPlayer().setNickname(hostPlayerName);
  }

  public String getTimeLimit() {
    return timeLimit;
  }

  public void setTimeLimit(String timeLimit) {
    this.timeLimit = timeLimit;
  }

  public ChessColor getHostColor() {
    return hostColor;
  }

  public void setHostColor(ChessColor hostColor) {
    this.hostColor = hostColor;
  }

  public GameType getGameType() {
    return gameType;
  }

  public void setGameType(GameType gameType) {
    this.gameType = gameType;
  }

  public boolean isRunning() {
    if (gameStatus == GameStatus.RUNNING) {
      return true;
    }
    return false;
  }

  public boolean isWaitingForPlayerToJoin() {
    if (gameStatus == GameStatus.WATING) {
      return true;
    }
    return false;
  }

  public Player getHostPlayer() {
    return players[0];
  }

  public Player getClientPlayer() {
    return players[1];
  }

  /**
   * Add second player to this Game. This Method will change the Gamestatus to @see
   * GameStatus.RUNNING.
   *
   * @param clientPlayer Player to join.
   */
  public void setClientPlayer(Player clientPlayer) {
    this.setGameName("Default Game Name");
    players[1] = clientPlayer;
    gameStatus = GameStatus.RUNNING;
  }

  public String getClientPlayerName() {
    if (players[1] == null) {
      return "WAITING FOR PLAYER";
    }
    return players[1].getNickname();
  }

  public GameStatus getGameStatus() {
    return gameStatus;
  }

  public void setGameStatus(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  public ClientThread getHostClientThread() {
    return hostClientThread;
  }

  public void setHostClientThread(ClientThread hostClientThread) {
    this.hostClientThread = hostClientThread;
  }

  /** Set all pieces on start position. This is a new Game State */
  public void initGameBoard() {
    gameBoard = new GameBoard();
    gameBoard.initStartState();
  }

  public void setHostPlayer(Player player) {
    players[0] = player;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ChessGame chessGame = (ChessGame) o;
    return Objects.equals(uuid, chessGame.uuid)
        && Objects.equals(gameName, chessGame.gameName)
        && Objects.equals(timeLimit, chessGame.timeLimit)
        && hostColor == chessGame.hostColor
        && gameType == chessGame.gameType
        && gameStatus == chessGame.gameStatus
        && Objects.equals(gameBoard, chessGame.gameBoard)
        && Objects.equals(hostClientThread, chessGame.hostClientThread);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        uuid,
        gameName,
        timeLimit,
        hostColor,
        gameType,
        gameStatus,
        gameBoard,
        hostClientThread);
  }
}
