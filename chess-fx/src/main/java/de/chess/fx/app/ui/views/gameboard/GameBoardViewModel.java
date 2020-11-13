package de.chess.fx.app.ui.views.gameboard;

import de.chess.fx.app.ui.views.figure.*;

public class GameBoardViewModel {

    public GameBoardViewModel() {
        initChessFigureMatrix(ChessColor.WHITE);
    }

    private final ChessFigure[][] figureMatrix = new ChessFigure[8][8];

    public void resetFigures() {

    }

    private void initChessFigureMatrix(ChessColor color) {
        initPawns(color);
        initTowers(color);
        initHorses(color);
        initBishops(color);
        initQueens(color);
        initKings(color);
    }

    private void initKings(ChessColor color) {
        figureMatrix[4][0] = new King((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);

        figureMatrix[4][7] = new King((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
    }

    private void initQueens(ChessColor color) {
        figureMatrix[3][0] = new Queen((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);

        figureMatrix[3][7] = new Queen((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
    }

    private void initBishops(ChessColor color) {
        //Spalte Zeile
        figureMatrix[2][0] = new Bishop((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[5][0] = new Bishop((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);

        figureMatrix[2][7] = new Bishop((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[5][7] = new Bishop((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
    }

    private void initHorses(ChessColor color) {
        //Spalte Zeile
        figureMatrix[1][0] = new Horse((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[6][0] = new Horse((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);

        figureMatrix[1][7] = new Horse((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[6][7] = new Horse((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
    }

    private void initTowers(ChessColor color) {
        figureMatrix[0][0] = new Tower((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[7][0] = new Tower((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);

        figureMatrix[0][7] = new Tower((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[7][7] = new Tower((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
    }

    private void initPawns(ChessColor color) {
        figureMatrix[0][1] = new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[1][1] = new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[2][1] = new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[3][1] = new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[4][1] = new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[5][1] = new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[6][1] = new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);
        figureMatrix[7][1] = new Pawn((color == ChessColor.BLACK) ? ChessColor.WHITE : ChessColor.BLACK);

        figureMatrix[0][6] = new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[1][6] = new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[2][6] = new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[3][6] = new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[4][6] = new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[5][6] = new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[6][6] = new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
        figureMatrix[7][6] = new Pawn((color == ChessColor.BLACK) ? ChessColor.BLACK : ChessColor.WHITE);
    }

    public ChessFigure[][] getFigureMatrix() {
        return figureMatrix;
    }
}
