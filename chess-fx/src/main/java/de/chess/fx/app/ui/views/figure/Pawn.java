package de.chess.fx.app.ui.views.figure;

import de.chess.model.ChessColor;

public class Pawn extends ChessFigure{

    public Pawn( ChessColor color) {
        super(color);
    }

    @Override
    public String getImageFileName() {
        return "pawn.png";
    }

}