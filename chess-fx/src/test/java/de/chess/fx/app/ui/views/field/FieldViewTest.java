package de.chess.fx.app.ui.views.field;

import de.chess.model.ChessColor;
import de.chess.fx.app.ui.views.figure.Pawn;
import de.chess.model.PortableGameNotation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldViewTest {

    @Test
    public void testEmptyField() {
        FieldView fieldViewEmpty = new FieldView(0, 2);
        boolean result = fieldViewEmpty.isFigurePresent();
        assertFalse(result);
    }

    @Test
    public void testNotEmptyField() {
        FieldView fieldViewEmpty = new FieldView(7, 2, new Pawn(ChessColor.BLACK), ChessColor.BLACK);
        boolean result = fieldViewEmpty.isFigurePresent();
        assertTrue(result);
    }

    @Test
    public void testPGN(){
        FieldView fieldB2 = new FieldView(1, 2, new Pawn(ChessColor.BLACK), ChessColor.BLACK);
        PortableGameNotation pgnB2 = fieldB2.toPGN();
        assertEquals("b2", pgnB2.toString());

        FieldView fieldH7 = new FieldView(1, 7, new Pawn(ChessColor.BLACK), ChessColor.BLACK);
        PortableGameNotation pgnH7 = fieldH7.toPGN();
        assertEquals("b7", pgnH7.toString());
    }
}