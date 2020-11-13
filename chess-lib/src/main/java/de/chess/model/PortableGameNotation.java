package de.chess.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Portable Game Notation (PGN) ist ein als Text lesbares Datenformat zur Speicherung von Schachpartien.
 * Es wurde 1994 von Stephen J. Edwards entwickelt, um den Austausch von Schachdaten zwischen verschiedenen
 * Schachprogrammen (zum Beispiel über das Internet) zu ermöglichen und zu vereinfachen.
 * https://de.wikipedia.org/wiki/Portable_Game_Notation
 */
public class PortableGameNotation implements Serializable {

    private int xCoordninate;
    private int yCoordinate;

    public PortableGameNotation(int xCoordninate, int yCoordinate) {
        this.xCoordninate = xCoordninate;
        this.yCoordinate = yCoordinate;
    }

    @Override
    public String toString() {

        return xCoordinateToSign(xCoordninate) + yCoordinate;
    }

    private String xCoordinateToSign(int xCoordninate){
        assert(xCoordninate<8);
        assert (xCoordninate>=0);

        switch (xCoordninate){
            case(0): return "a";
            case(1): return "b";
            case(2): return "c";
            case(3): return "d";
            case(4): return "e";
            case(5): return "f";
            case(6): return "g";
            default: return "h";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortableGameNotation that = (PortableGameNotation) o;
        return yCoordinate == that.yCoordinate &&
                Objects.equals(xCoordninate, that.xCoordninate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoordninate, yCoordinate);
    }
}
