package de.chess.fx.app.ui.views.host;

import de.chess.dto.GameDTO;
import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.command.StartGameCommand;
import de.chess.fx.app.ui.command.ToMainMenuCommand;
import de.chess.model.ChessColor;
import de.chess.model.GameType;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;

public class HostGameViewModel {

    private StringProperty gameName =
            new SimpleStringProperty("Default-Chess-Game");

    private StringProperty payerName =
            new SimpleStringProperty(getDefaultRandomName());

    private StringProperty timeLimit =
            new SimpleStringProperty(getDefaultRandomName());


    private BooleanProperty blackSelected = new SimpleBooleanProperty();
    private BooleanProperty whiteSelected = new SimpleBooleanProperty();
    private BooleanProperty randomSelected = new SimpleBooleanProperty();

    private SimpleObjectProperty<GameType> gameType = new SimpleObjectProperty();
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

                }
            }
        });

    }


    private GameDTO buildGameDTO(){
         GameDTO gameDTO = new GameDTO(getGameName(), getPayerName(), getTimeLimit(), getSelectedColorValue(), getGameType());
         return gameDTO;

    }

    public GameType getGameType() {
        return gameType.get();
    }

    public SimpleObjectProperty<GameType> gameTypeProperty() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType.set(gameType);
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

    public String getPayerName() {
        return payerName.get();
    }

    public StringProperty payerNameProperty() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName.set(payerName);
    }

    public ICommando getStartCommand(Scene scene){
        return new StartGameCommand(scene, buildGameDTO());
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
