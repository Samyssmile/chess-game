package de.chess.fx.app.ui.views.figure;

import de.chess.model.ChessColor;

public class King extends ChessFigure{
    public King(ChessColor color) {
        super(color);
    }

    @Override
    public String getImageFileName() {
        return "king.png";
    }
}
