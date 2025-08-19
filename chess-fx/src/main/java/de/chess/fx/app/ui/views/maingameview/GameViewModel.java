package de.chess.fx.app.ui.views.maingameview;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.model.ChessColor;
import de.chess.fx.app.audio.AudioEffectPlayer;
import de.chess.fx.app.audio.AudioEffectType;
import de.chess.fx.app.handler.EventData;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.fx.app.handler.IChannel;
import de.chess.fx.app.i18n.Internalization;
import de.chess.fx.app.ui.command.MoveCommand;
import de.chess.fx.app.ui.dialogs.DialogFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;

import java.util.Objects;

public class GameViewModel implements Internalization, IChannel {

    private StringProperty hostPlayerName = new SimpleStringProperty(i18n("game.default.hostname"));
    private StringProperty clientPlayerName = new SimpleStringProperty(i18n("game.client.hostname"));
    private StringProperty turnIndicator = new SimpleStringProperty("White to move");
    private StringProperty gameStatus = new SimpleStringProperty("Waiting for game to start");
    private AudioEffectPlayer audioEffectPlayer;
    private ChessGame gameDTO;
    private boolean isCurrentUserHost = false;

    public GameViewModel( ) {
        audioEffectPlayer = new AudioEffectPlayer();
        registerForEvents();
    }


    private void registerForEvents() {
        EventHandler.getInstance().registerForEvent(this, EventType.PLAYER_JOINED);
        EventHandler.getInstance().registerForEvent(this, EventType.GAME_STARTED);
        EventHandler.getInstance().registerForEvent(this, EventType.MOVE_DONE);
        EventHandler.getInstance().registerForEvent(this, EventType.REMIS);
        EventHandler.getInstance().registerForEvent(this, EventType.WON);
        EventHandler.getInstance().registerForEvent(this, EventType.LOSE);
    }

    public String getHostPlayerName() {
        return hostPlayerName.get();
    }

    public StringProperty hostPlayerNameProperty() {
        return hostPlayerName;
    }

    public void setHostPlayerName(String hostPlayerName) {
        this.hostPlayerName.set(hostPlayerName);
    }

    public String getClientPlayerName() {
        return clientPlayerName.get();
    }

    public StringProperty clientPlayerNameProperty() {
        return clientPlayerName;
    }

    public void setClientPlayerName(String clientPlayerName) {
        this.clientPlayerName.set(clientPlayerName);
    }

    public String getTurnIndicator() {
        return turnIndicator.get();
    }

    public StringProperty turnIndicatorProperty() {
        return turnIndicator;
    }

    public void setTurnIndicator(String turnIndicator) {
        this.turnIndicator.set(turnIndicator);
    }

    public String getGameStatus() {
        return gameStatus.get();
    }

    public StringProperty gameStatusProperty() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus.set(gameStatus);
    }

    @Override
    public void update(EventType eventType, EventData eventData) {
        switch (eventType){
            case PLAYER_JOINED:
                audioEffectPlayer.playSound(AudioEffectType.PLAYER_JOINED);
                Player joinedPlayer = (Player) eventData.getData();
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        clientPlayerName.setValue(joinedPlayer.getNickname());
                        updateGameStatus();
                        updateTurnIndicator();
                    }
                });
                break;

            case GAME_STARTED:
                Platform.runLater(() -> {
                    updateGameStatus();
                    updateTurnIndicator();
                });
                break;

            case MOVE_DONE:
                Platform.runLater(() -> {
                    // Update the game state from server response
                    ChessGame updatedGame = (ChessGame) eventData.getData();
                    if (updatedGame != null) {
                        this.gameDTO = updatedGame; // Update local game state
                    }
                    updateTurnIndicator();
                    updateGameStatus();
                });
                break;

            case REMIS:
                Platform.runLater(() -> {
                    setGameStatus("Draw agreed");
                    setTurnIndicator("Game ended");
                    showGameEndDialog("Game Draw", "Draw", "The game ended in a draw!");
                });
                break;

            case WON:
                Platform.runLater(() -> {
                    setGameStatus("You won!");
                    setTurnIndicator("Game ended");
                    showGameEndDialog("Victory!", "Congratulations!", "You have won the game!");
                });
                break;

            case LOSE:
                Platform.runLater(() -> {
                    setGameStatus("You lost!");
                    setTurnIndicator("Game ended");
                    showGameEndDialog("Defeat", "Game Over", "You have lost the game. Better luck next time!");
                });
                break;
        }
    }


    public void setChessGame(ChessGame gameDTO) {
        this.gameDTO = gameDTO;
        hostPlayerName.setValue(gameDTO.getHostPlayer().getNickname());
        if(!Objects.isNull(gameDTO.getClientPlayer())){
            clientPlayerName.setValue(gameDTO.getClientPlayer().getNickname());
        }
        updateGameStatus();
        updateTurnIndicator();
    }
    
    public void setCurrentUserAsHost(boolean isHost) {
        this.isCurrentUserHost = isHost;
    }
    
    public boolean isCurrentUserHost() {
        return isCurrentUserHost;
    }
    
    /**
     * Get the color of the current user
     */
    public ChessColor getCurrentUserColor() {
        if (gameDTO == null) return null;
        
        if (isCurrentUserHost) {
            return gameDTO.getHostColor();
        } else {
            return gameDTO.getClientColor();
        }
    }
    
    /**
     * Check if the board should be rotated for the current user
     */
    public boolean shouldRotateBoard() {
        return getCurrentUserColor() == ChessColor.BLACK;
    }

    public ChessGame getGameDTO() {
        return gameDTO;
    }

    /**
     * Send a move request to the server
     * @param moveString The move in chess notation format (e.g., "e2-e4")
     * @return true if the request was sent successfully (doesn't guarantee move validity)
     */
    public boolean makeMove(String moveString) {
        try {
            if (gameDTO == null || gameDTO.getUuid() == null) {
                System.err.println("Cannot make move: Game data not available");
                return false;
            }

            // Create and execute move command
            MoveCommand moveCommand = new MoveCommand(gameDTO.getUuid(), moveString);
            moveCommand.execute();
            
            return true;
        } catch (Exception e) {
            System.err.println("Error making move: " + e.getMessage());
            return false;
        }
    }

    private void updateTurnIndicator() {
        if (gameDTO == null) {
            setTurnIndicator("Game not started");
            return;
        }

        if (gameDTO.getCurrentTurn() == null) {
            setTurnIndicator("White to move");
            return;
        }

        String colorText = gameDTO.getCurrentTurn() == ChessColor.WHITE ? "White" : "Black";
        setTurnIndicator(colorText + " to move");
    }

    private void updateGameStatus() {
        if (gameDTO == null) {
            setGameStatus("No game data");
            return;
        }

        switch (gameDTO.getGameStatus()) {
            case WATING:
                setGameStatus("Waiting for second player");
                break;
            case READY_TO_START:
                setGameStatus("Ready to start");
                break;
            case RUNNING:
                if (gameDTO.isCheckmate()) {
                    ChessColor winner = gameDTO.getCurrentTurn() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
                    String winnerText = winner == ChessColor.WHITE ? "White" : "Black";
                    setGameStatus("Checkmate - " + winnerText + " wins!");
                    
                    // Show checkmate dialog
                    boolean isPlayerWinner = (isCurrentUserHost && gameDTO.getHostColor() == winner) || 
                                           (!isCurrentUserHost && gameDTO.getClientColor() == winner);
                    if (isPlayerWinner) {
                        showGameEndDialog("Victory!", "Checkmate!", "Checkmate! You have won the game!");
                    } else {
                        showGameEndDialog("Defeat", "Checkmate", "Checkmate! Your opponent has won the game.");
                    }
                } else if (gameDTO.isStalemate()) {
                    setGameStatus("Stalemate - Draw!");
                    showGameEndDialog("Game Draw", "Stalemate", "The game ended in stalemate - it's a draw!");
                } else if (gameDTO.isWhiteInCheck() || gameDTO.isBlackInCheck()) {
                    String checkedColor = gameDTO.isWhiteInCheck() ? "White" : "Black";
                    setGameStatus("Check - " + checkedColor + " king in danger!");
                } else {
                    setGameStatus("Game in progress");
                }
                break;
            case FINISHED:
                setGameStatus("Game finished");
                break;
            default:
                setGameStatus("Unknown status");
        }
    }

    private void showGameEndDialog(String title, String header, String message) {
        try {
            DialogFactory.instance().getInfoDialog(title, header, message);
        } catch (Exception e) {
            System.err.println("Error showing game end dialog: " + e.getMessage());
        }
    }
}
