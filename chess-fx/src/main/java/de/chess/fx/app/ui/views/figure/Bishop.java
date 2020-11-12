package de.chess.fx.app.ui.views.figure;

public class Bishop extends ChessFigure{

    public Bishop(ChessColor color) {
        super(color);
    }

    @Override
    public String getImageFileName() {
        return "bishop.png";
    }
}
