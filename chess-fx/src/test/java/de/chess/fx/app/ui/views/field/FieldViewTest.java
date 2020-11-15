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
        PortableGameNotation pgnB3 = fieldB2.toPGN();
        String actual = pgnB3.toString();
        assertEquals("b3",actual );

        FieldView fieldH8 = new FieldView(1, 7, new Pawn(ChessColor.BLACK), ChessColor.BLACK);
        PortableGameNotation pgnH8 = fieldH8.toPGN();
        assertEquals("b8", pgnH8.toString());
    }
}