package de.chess.dto;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    private UUID uuid;
    private String nickname;
    private int elo;

    public Player(UUID uuid, String nickname, int elo) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.elo = elo;
    }

    public Player(String nickname, int elo) {
        this(UUID.randomUUID(), nickname, elo);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    @Override
    public String toString() {
        return "Player{" +
                "uuid=" + uuid +
                ", nickname='" + nickname + '\'' +
                ", elo=" + elo +
                '}';
    }
}
