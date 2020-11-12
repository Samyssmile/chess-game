package de.chess.fx.app.ui.views.figure;

public class King extends ChessFigure{
    public King(ChessColor color) {
        super(color);
    }

    @Override
    public String getImageFileName() {
        return "king.png";
    }
}
