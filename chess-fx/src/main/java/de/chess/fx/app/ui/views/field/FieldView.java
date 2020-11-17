package de.chess.fx.app.ui.views.field;

import de.chess.dto.Declaration;
import de.chess.fx.app.ui.views.UIView;
import de.chess.model.ChessColor;
import de.chess.fx.app.ui.views.figure.ChessFigure;
import de.chess.model.PortableGameNotation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FieldView extends Pane implements UIView {
    public static final Logger LOGGER = Logger.getLogger(FieldView.class.getSimpleName());
    private static final double PREFERED_FIELD_SIZE = 90;

    private FieldViewModel viewModel;

    private final SimpleObjectProperty<ChessFigure> figureOnFieldProperty = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty xCoordinate = new SimpleIntegerProperty();
    private final SimpleIntegerProperty yCoordinate = new SimpleIntegerProperty();
    private Flow.Subscriber subscriber;

    public FieldView(int xCoordinate, int yCoordinate) {
        this(xCoordinate, yCoordinate, null, null);
    }

    public FieldView(int xCoordinate, int yCoordinate, ChessFigure figureOnField, ChessColor fieldColor) {
        initViewModel();
        Bindings.bindBidirectional(figureOnFieldProperty, viewModel.figureProperty());
        this.xCoordinate.set(xCoordinate);
        this.yCoordinate.set(yCoordinate);
        SimpleObjectProperty<ChessColor> fieldColor1 = new SimpleObjectProperty<>();
        fieldColor1.set(fieldColor);
        initNodes();
        initActionsEvents();
        viewModel.setFigure(figureOnField);

    }

    public FieldViewModel getViewModel() {
        return viewModel;
    }


    @Override
    public List<Node> initNodes() {
        return Collections.emptyList();
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


        setOnDragDetected(event -> {
            /* drag was detected, start a drag-and-drop gesture*/
            /* allow any transfer mode */
            Dragboard db = startDragAndDrop(TransferMode.ANY);

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(toPGN().toString());

            db.setContent(content);

            event.consume();
        });

        setOnDragDropped(event -> {
            /* data dropped */
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String pgn = db.getString();
                LOGGER.log(Level.INFO, "Von: %s drop nach %s".formatted(pgn, toPGN()));
                success = true;
                publisher.submit(pgn+"-"+toPGN().toString());
                shotAlertDialogWithPGN(pgn, toPGN().toString());


            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });


        setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is not dragged from the same node
             * and if it has a string data */
            if (event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                setEffect(new InnerShadow(30, Color.YELLOW));
            }

            event.consume();
        });

        setOnDragExited(event -> {
            setEffect(null);
            event.consume();
        });


    }

    private void shotAlertDialogWithPGN(String from, String to) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Debug Information Dialog");
        alert.setHeaderText("Drag & Drop Detected");
        alert.setContentText("Draged from: " + from + " to " + to);

        alert.showAndWait();
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

    final SubmissionPublisher<String> publisher =
            new SubmissionPublisher<>(ForkJoinPool.commonPool(), 100);

    public void registerOnPublisher(Flow.Subscriber<String> gameBoardViewModel) {
        publisher.subscribe(gameBoardViewModel);


    }
}
