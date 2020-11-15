package de.chess.dto;

import java.io.Serializable;
import java.util.Objects;

public class Signal implements Serializable {

    private final String id;
    private final String pgn;

    public Signal(String id, String pgn) {
        this.id = id;
        this.pgn = pgn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signal signal = (Signal) o;
        return Objects.equals(id, signal.id) &&
                Objects.equals(pgn, signal.pgn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pgn);
    }

    @Override
    public String toString() {
        return "Signal{" +
                "id='" + id + '\'' +
                ", pgn='" + pgn + '\'' +
                '}';
    }
}
