package de.chess.fx.app.ui.views.field;

import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.figure.ChessColor;
import de.chess.fx.app.ui.views.figure.ChessFigure;
import de.chess.model.PortableGameNotation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;

public class FieldView extends Pane implements UIView {

    private static final double PREFERED_FIELD_SIZE = 90;

    private FieldViewModel viewModel;

    private final SimpleObjectProperty<ChessFigure> figureOnFieldProperty = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty xCoordinate = new SimpleIntegerProperty();
    private final SimpleIntegerProperty yCoordinate = new SimpleIntegerProperty();

    private final SimpleObjectProperty<ChessColor> fieldColor = new SimpleObjectProperty<>();

    public FieldView(int xCoordinate, int yCoordinate) {
        this(xCoordinate, yCoordinate, null, null);
    }

    public FieldView(int xCoordinate, int yCoordinate, ChessFigure figureOnField, ChessColor fieldColor) {
        initViewModel();
        Bindings.bindBidirectional(figureOnFieldProperty, viewModel.figureProperty());
        this.xCoordinate.set(xCoordinate);
        this.yCoordinate.set(yCoordinate);
        this.fieldColor.set(fieldColor);
        initNodes();
        initActionsEvents();
        viewModel.setFigure(figureOnField);

    }

    public FieldViewModel getViewModel() {
        return viewModel;
    }


    @Override
    public List<Node> initNodes() {
        return null;
    }

    @Override
    public void initActionsEvents() {
        figureOnFieldProperty.addListener((observable, oldValue, newValue) -> {
            newValue.prefHeight(PREFERED_FIELD_SIZE);
            newValue.prefWidth(PREFERED_FIELD_SIZE);
            newValue.fitWidthProperty().bind(widthProperty());
            newValue.fitHeightProperty().bind(heightProperty());
            getChildren().add(newValue);
        });


        setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start a drag-and-drop gesture*/
                /* allow any transfer mode */
                Dragboard db = startDragAndDrop(TransferMode.ANY);

                /* Put a string on a dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello Drag Drop Function ;)");
                db.setContent(content);

                event.consume();
                System.out.println("Drag Detected");
            }
        });

        setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    System.out.println(db.getString() + " String aus Target");
                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);
                System.out.println("Drag Dropped...");
                event.consume();
            }
        });

        setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                /* accept it only if it is not dragged from the same node
                 * and if it has a string data */
                if (event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    setEffect(new InnerShadow(30, Color.YELLOW));
                }

                event.consume();
            }
        });

        setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                setEffect(null);
                event.consume();
            }
        });


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