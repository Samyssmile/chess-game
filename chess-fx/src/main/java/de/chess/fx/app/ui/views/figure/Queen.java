package de.chess.fx.app.ui.views.figure;

public class Queen extends ChessFigure{

    public Queen(ChessColor color) {
        super(color);
    }

    @Override
    public String getImageFileName() {
        return "queen.png";
    }
}
