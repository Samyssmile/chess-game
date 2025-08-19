package de.chess.fx.app.ui.views.figure;

import de.chess.model.ChessColor;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.File;


public abstract class ChessFigure extends ImageView {

    private final ChessColor color;
    private String pawnImageLocation = "figures";
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


    public void loadImage(String imageFileName) {
        pawnImageLocation = pawnImageLocation + File.separator + (((color == ChessColor.WHITE)) ? "white_" : "black_") + imageFileName;
        setImage(new Image(classLoader.getResourceAsStream(pawnImageLocation)));
    }

    public ChessFigure(ChessColor color) {
        this.color = color;
        loadImage(getImageFileName());
        
        // Größe an Feldgröße anpassen - wird dynamisch vom FieldView gesetzt
        setupDefaultFigureSize();

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setEffect(new DropShadow(10, Color.BLUE));
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setEffect(null);
            }
        });
    }
    
    private void setupDefaultFigureSize() {
        // Standard Feldgröße - kann später überschrieben werden
        setFigureSize(60.0);
    }
    
    public void setFigureSize(double fieldSize) {
        double figureSize = fieldSize * 0.8; // 80% der Feldgröße für bessere Optik
        
        this.setFitWidth(figureSize);
        this.setFitHeight(figureSize);
        this.setPreserveRatio(true);
        this.setSmooth(true);
    }

    public ChessColor getColor() {
        return color;
    }

    public abstract String getImageFileName();
}
