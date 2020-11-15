package de.chess.dto;

import de.chess.model.ChessColor;
import de.chess.model.GameType;

import java.io.Serializable;
import java.util.UUID;

public class GameDTO implements Serializable {

    private UUID uuid;
    private String gameName;
    private String hostPlayerName;
    private String timeLimit;
    private ChessColor hostColor;
    private GameType gameType;

    public GameDTO(String gameName, String hostPlayerName, String timeLimit, ChessColor hostColor, GameType gameType) {
        this.gameName = gameName;
        this.hostPlayerName = hostPlayerName;
        this.timeLimit = timeLimit;
        this.hostColor = hostColor;
        this.gameType = gameType;
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
        return hostPlayerName;
    }

    public void setHostPlayerName(String hostPlayerName) {
        this.hostPlayerName = hostPlayerName;
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
}
