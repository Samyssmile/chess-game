package de.chess.fx.app.ui.views.figure;

public class Horse extends ChessFigure{

    public Horse(ChessColor color) {
        super(color);
    }

    @Override
    public String getImageFileName() {
        return "horse.png";
    }
}
