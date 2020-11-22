package de.chess.dto;

import de.chess.model.ChessColor;
import de.chess.model.GameType;

import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class ChessGame implements Serializable {

    private UUID uuid;
    private String gameName;
    private Player hostPlayer;
    private Player clientPlayer;
    private String timeLimit;
    private ChessColor hostColor;
    private GameType gameType;
    private GameStatus gameStatus;
    private transient SocketChannel hostChannel;

    public ChessGame(String gameName, Player hostPlayer, String timeLimit, ChessColor hostColor, GameType gameType) {
        this.gameName = gameName;
        this.hostPlayer = hostPlayer;
        this.timeLimit = timeLimit;
        this.hostColor = hostColor;
        this.gameType = gameType;
        gameStatus = GameStatus.WATING;
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

    public String getHostPlayerName() {
        return hostPlayer.getNickname();
    }

    public void setHostPlayerName(String hostPlayerName) {
        this.hostPlayer.setNickname(hostPlayerName);
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

    public boolean isRunning(){
        if(gameStatus==GameStatus.RUNNING){
            return true;
        }
        return false;
    }

    public boolean isWaitingForPlayerToJoin(){
        if(gameStatus==GameStatus.WATING){
            return true;
        }
        return false;
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

    public String getClientPlayerName() {
        if (clientPlayer == null){
            return "WAITING FOR PLAYER";
        }
        return clientPlayer.getNickname();
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setHostChannel(SocketChannel socket) {
        this.hostChannel = socket;
    }

    public SocketChannel getHostChannel() {
        return hostChannel;
    }
}
