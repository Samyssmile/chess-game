package de.chess.reader.model;

import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private int elo;

    public Player(String name) {
        this(name, 0);
    }

    public Player(String name, int elo) {
        this.name = name;
        this.elo = elo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
