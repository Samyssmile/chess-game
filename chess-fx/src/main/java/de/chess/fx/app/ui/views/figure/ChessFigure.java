package de.chess.fx.app.ui.views.figure;

import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.File;


public abstract class ChessFigure extends ImageView {

    private final ChessColor color;
    private  String pawnImageLocation = "figures";
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


    public void loadImage(String imageFileName) {
        pawnImageLocation=pawnImageLocation+ File.separator+(((color == ChessColor.WHITE))?"white_":"black_")+imageFileName;
        setImage(new Image( classLoader.getResourceAsStream(pawnImageLocation)));
    }

    public ChessFigure(ChessColor color)  {
        this.color = color;
        loadImage(getImageFileName());

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

    public abstract String getImageFileName();
}
