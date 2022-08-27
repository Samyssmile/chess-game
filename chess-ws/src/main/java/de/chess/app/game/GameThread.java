package de.chess.app.game;

import de.chess.dto.ChessGame;
import de.chess.dto.GameStatus;
import de.chess.dto.Player;
import de.chess.model.ChessColor;

public class GameThread extends Thread {

    private final ChessGame chessGame;
    private final ChessColor hostColor;
    private final ChessColor clientColor;

    private Player playerOnTurn;


    public GameThread(ChessGame chessGame) {
        this.chessGame = chessGame;
        hostColor = this.chessGame.getHostColor();
        clientColor = this.chessGame.getClientColor();
        setWhitePlayerOnTurn();
    }

    private void setWhitePlayerOnTurn() {
        assert this.chessGame.getHostColor() != ChessColor.RANDOM;
        assert this.chessGame.getClientColor() != ChessColor.RANDOM;

        if(hostColor == ChessColor.WHITE){
            playerOnTurn = this.chessGame.getHostPlayer();
        }else{
            playerOnTurn = this.chessGame.getClientPlayer();
        }
    }

    @Override
    public void run() {
        String timelimit = this.chessGame.getTimeLimit();
        startGame();
        while (this.chessGame.isRunning()) {


        }
        System.out.println("Will start game with timer: " + timelimit);
    }

    private boolean startGame() {
        if (this.chessGame.getGameStatus() == GameStatus.READY_TO_START) {
            chessGame.setGameStatus(GameStatus.RUNNING);
            return true;
        }

        return false;
    }
}
