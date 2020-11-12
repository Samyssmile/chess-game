package de.chess.fx.app.ui.views.figure;

public class Pawn extends ChessFigure{

    public Pawn( ChessColor color) {
        super(color);
    }

    @Override
    public String getImageFileName() {
        return "pawn.png";
    }

}