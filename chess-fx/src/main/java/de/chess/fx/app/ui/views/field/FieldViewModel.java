package de.chess.fx.app.ui.views.field;

import de.chess.fx.app.ui.views.figure.ChessFigure;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class FieldViewModel {

    private SimpleObjectProperty<ChessFigure> figure = new SimpleObjectProperty<>();
    private SimpleIntegerProperty xCoordinate = new SimpleIntegerProperty();
    private SimpleIntegerProperty yCoordinate= new SimpleIntegerProperty();

    public ChessFigure getFigure() {
        return figure.get();
    }

    public SimpleObjectProperty<ChessFigure> figureProperty() {
        return figure;
    }

    public void setFigure(ChessFigure figure) {
        this.figure.set(figure);
    }

    public int getxCoordinate() {
        return xCoordinate.get();
    }

    public SimpleIntegerProperty xCoordinateProperty() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate.set(xCoordinate);
    }

    public int getyCoordinate() {
        return yCoordinate.get();
    }

    public SimpleIntegerProperty yCoordinateProperty() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate.set(yCoordinate);
    }
}
