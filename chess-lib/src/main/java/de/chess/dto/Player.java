package de.chess.dto;

import java.io.Serializable;
import java.util.Objects;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return elo == player.elo &&
                Objects.equals(uuid, player.uuid) &&
                Objects.equals(nickname, player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, nickname, elo);
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
