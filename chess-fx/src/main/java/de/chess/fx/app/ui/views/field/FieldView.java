package de.chess.fx.app.ui.views.field;

import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.figure.ChessFigure;
import de.chess.model.PortableGameNotation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.List;

public class FieldView extends Pane implements UIView {

    private FieldViewModel viewModel;

    private SimpleObjectProperty<ChessFigure> figureOnFieldProperty = new SimpleObjectProperty<>();
    private SimpleIntegerProperty xCoordinate = new SimpleIntegerProperty();
    private SimpleIntegerProperty yCoordinate = new SimpleIntegerProperty();

    public FieldView(int xCoordinate, int yCoordinate) {
        this(xCoordinate, yCoordinate, null);
    }

    public FieldView(int xCoordinate, int yCoordinate, ChessFigure figureOnField) {
        initViewModel();
        Bindings.bindBidirectional(figureOnFieldProperty, viewModel.figureProperty());
        this.xCoordinate.set(xCoordinate);
        this.yCoordinate.set(yCoordinate);
        initNodes();
        initActionsEvents();
        viewModel.setFigure(figureOnField);
    }

    @Override
    public List<Node> initNodes() {
        return null;
    }

    @Override
    public void initActionsEvents() {

    }

    @Override
    public void initViewModel() {
        this.viewModel = new FieldViewModel();
    }

    public PortableGameNotation toPGN() {
        return new PortableGameNotation(xCoordinate.getValue(), yCoordinate.getValue());
    }

    public boolean isFigurePresent() {
        return figureOnFieldProperty.isNotNull().get();
    }
}
