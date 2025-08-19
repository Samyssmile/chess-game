package de.chess.fx.app.ui.views.field;

import de.chess.fx.app.ui.views.figure.ChessFigure;
import de.chess.model.ChessColor;
import de.chess.model.PortableGameNotation;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * FieldView represents a single square on the chess board.
 * It can be empty or contain a chess figure.
 */
public class FieldView extends StackPane {
    
    private final int x;
    private final int y;
    private ChessFigure figure;
    private final ChessColor fieldColor;
    private static final double FIELD_SIZE = 60.0;

    /**
     * Constructor for empty field
     */
    public FieldView(int x, int y) {
        this(x, y, null, (x + y) % 2 == 0 ? ChessColor.WHITE : ChessColor.BLACK);
    }

    /**
     * Constructor for field with figure
     */
    public FieldView(int x, int y, ChessFigure figure, ChessColor fieldColor) {
        this.x = x;
        this.y = y;
        this.figure = figure;
        this.fieldColor = fieldColor;
        
        setupFieldAppearance();
        
        if (figure != null) {
            figure.setFigureSize(FIELD_SIZE);
            getChildren().add(figure);
        }
    }

    private void setupFieldAppearance() {
        Rectangle background = new Rectangle(FIELD_SIZE, FIELD_SIZE);
        background.setFill(fieldColor == ChessColor.WHITE ? Color.BEIGE : Color.BROWN);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(1);
        
        getChildren().add(background);
        
        setPrefSize(FIELD_SIZE, FIELD_SIZE);
        setMaxSize(FIELD_SIZE, FIELD_SIZE);
        setMinSize(FIELD_SIZE, FIELD_SIZE);
    }

    /**
     * Check if this field has a chess figure
     */
    public boolean isFigurePresent() {
        return figure != null;
    }

    /**
     * Convert field coordinates to Portable Game Notation
     */
    public PortableGameNotation toPGN() {
        return new PortableGameNotation(x, y);
    }

    public ChessFigure getFigure() {
        return figure;
    }

    public void setFigure(ChessFigure figure) {
        if (this.figure != null) {
            getChildren().remove(this.figure);
        }
        
        this.figure = figure;
        
        if (figure != null) {
            figure.setFigureSize(FIELD_SIZE);
            getChildren().add(figure);
        }
    }

    public int getFieldX() {
        return x;
    }

    public int getFieldY() {
        return y;
    }

    public ChessColor getFieldColor() {
        return fieldColor;
    }
}