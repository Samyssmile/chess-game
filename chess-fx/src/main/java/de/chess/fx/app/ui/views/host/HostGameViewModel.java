package de.chess.fx.app.ui.views.host;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.fx.app.ChessFX;
import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.command.StartGameCommand;
import de.chess.fx.app.ui.command.StartHostGameCommand;
import de.chess.fx.app.ui.command.menu.ToMainMenuCommand;
import de.chess.model.ChessColor;
import de.chess.model.GameType;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;

import java.util.logging.Logger;

public class HostGameViewModel {
    private static final Logger LOGGER = Logger.getGlobal();
    private StringProperty gameName =
            new SimpleStringProperty("Default-Chess-Game");

    private StringProperty playerName =
            new SimpleStringProperty(getDefaultRandomName());

    private StringProperty timeLimit =
            new SimpleStringProperty(getDefaultRandomName());


    private BooleanProperty blackSelected = new SimpleBooleanProperty();
    private BooleanProperty whiteSelected = new SimpleBooleanProperty();
    private BooleanProperty randomSelected = new SimpleBooleanProperty(true);

    private BooleanProperty isRankedGame = new SimpleBooleanProperty();

    private String timeLimitValue;
    private ChessColor selectedColorValue;

    public HostGameViewModel() {
        timeLimit.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                timeLimitValue = newValue;
            }
        });

        blackSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    selectedColorValue = ChessColor.BLACK;
                }
            }
        });

        whiteSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    selectedColorValue = ChessColor.WHITE;
                }
            }
        });

        randomSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    double randomValue = Math.random();
                    if (randomValue>0.5){
                        selectedColorValue = ChessColor.WHITE;
                    }else{
                        selectedColorValue = ChessColor.BLACK;
                    }
                    LOGGER.info("Random color selected: "+selectedColorValue);

                }
            }
        });

        initRandomHostColor();
    }

    private void initRandomHostColor() {
        double randomNumber = Math.random();
        if (randomNumber>0.5){
            selectedColorValue = ChessColor.WHITE;
        }else{
            selectedColorValue = ChessColor.BLACK;
        }
    }


    private ChessGame buildGameDTO(){
    ChessGame gameDTO =
        new ChessGame(
            getGameName(),
            new Player(ChessFX.PLAYERS_UUID, getPlayerName(), 1500),
            getTimeLimit(),
            getSelectedColorValue(),
            getGameType());
         return gameDTO;

    }

    public boolean isIsRankedGame() {
        return isRankedGame.get();
    }

    public BooleanProperty isRankedGameProperty() {
        return isRankedGame;
    }

    public void setIsRankedGame(boolean isRankedGame) {
        this.isRankedGame.set(isRankedGame);
    }

    public GameType getGameType() {
        if (isRankedGame.get()){
            return GameType.RANKED;
        }
        return GameType.FUN;
    }


    public String getTimeLimitValue() {
        return timeLimitValue;
    }

    public void setTimeLimitValue(String timeLimitValue) {
        this.timeLimitValue = timeLimitValue;
    }

    public ChessColor getSelectedColorValue() {
        return selectedColorValue;
    }

    public void setSelectedColorValue(ChessColor selectedColorValue) {
        this.selectedColorValue = selectedColorValue;
    }

    private String getDefaultRandomName() {
        return "Default-Player-"+Math.round((Math.random()*10000));
    }

    public String getTimeLimit() {
        return timeLimit.get();
    }

    public StringProperty timeLimitProperty() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit.set(timeLimit);
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public StringProperty playerNameProperty() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName.set(playerName);
    }

    public ICommando getStartCommand(Scene scene){
        return new StartGameCommand(scene, buildGameDTO());
    }

    public ICommando requestHosting(Scene scene){
        return new StartHostGameCommand(scene, buildGameDTO());
    }

    public ICommando getToMainMenuCommand(Scene scene) {
       return new ToMainMenuCommand(scene);
    }

    public String getGameName() {
        return gameName.get();
    }

    public StringProperty gameNameProperty() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName.set(gameName);
    }

    public boolean isBlackSelected() {
        return blackSelected.get();
    }

    public BooleanProperty blackSelectedProperty() {
        return blackSelected;
    }

    public void setBlackSelected(boolean blackSelected) {
        this.blackSelected.set(blackSelected);
    }

    public boolean isWhiteSelected() {
        return whiteSelected.get();
    }

    public BooleanProperty whiteSelectedProperty() {
        return whiteSelected;
    }

    public void setWhiteSelected(boolean whiteSelected) {
        this.whiteSelected.set(whiteSelected);
    }

    public boolean isRandomSelected() {
        return randomSelected.get();
    }

    public BooleanProperty randomSelectedProperty() {
        return randomSelected;
    }

    public void setRandomSelected(boolean randomSelected) {
        this.randomSelected.set(randomSelected);
    }


}
