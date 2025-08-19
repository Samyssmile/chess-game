package de.chess.app.game;

import de.chess.dto.ChessGame;
import de.chess.dto.GameStatus;
import de.chess.dto.Player;
import de.chess.dto.RequestType;
import de.chess.dto.response.GameStartedResponse;
import de.chess.io.server.PlayerConnection;
import de.chess.io.server.PlayerManager;
import de.chess.model.ChessColor;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread {

    private static final Logger LOGGER = Logger.getGlobal();
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
            
            // Notify both players that the game has started
            notifyPlayersGameStarted();
            
            return true;
        }

        return false;
    }
    
    private void notifyPlayersGameStarted() {
        try {
            // Create game started response
            GameStartedResponse gameStartedResponse = new GameStartedResponse(
                chessGame.getUuid(), 
                RequestType.GAME_STARTED, 
                chessGame
            );
            
            // Notify host player
            Optional<PlayerConnection> hostConnection = 
                PlayerManager.getInstance().getConnectionByPlayerUUID(chessGame.getHostPlayer().getUuid());
            if (hostConnection.isPresent()) {
                hostConnection.get().getClientThread().sendResponse(gameStartedResponse);
                LOGGER.log(Level.INFO, "Notified host player that game started: " + chessGame.getUuid());
            } else {
                LOGGER.warning("Could not find host player connection for game: " + chessGame.getUuid());
            }
            
            // Notify client player
            if (chessGame.getClientPlayer() != null) {
                Optional<PlayerConnection> clientConnection = 
                    PlayerManager.getInstance().getConnectionByPlayerUUID(chessGame.getClientPlayer().getUuid());
                if (clientConnection.isPresent()) {
                    clientConnection.get().getClientThread().sendResponse(gameStartedResponse);
                    LOGGER.log(Level.INFO, "Notified client player that game started: " + chessGame.getUuid());
                } else {
                    LOGGER.warning("Could not find client player connection for game: " + chessGame.getUuid());
                }
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error notifying players that game started", e);
        }
    }
}
