package de.chess.fx.app.ui.views.figure;

import de.chess.model.ChessColor;

public class Tower extends ChessFigure{
    public Tower(ChessColor color) {
        super(color);
    }

    @Override
    public String getImageFileName() {
        return "tower.png";
    }
}
