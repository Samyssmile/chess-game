package de.chess.fx.app.ui.host;

import de.chess.fx.app.ui.MainMenu;
import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.command.StartGameCommand;
import de.chess.fx.app.ui.command.ToMainMenuCommand;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;

public class HostGameViewModel {

    private StringProperty gameName =
            new SimpleStringProperty("Default-Chess-Game");
    private BooleanProperty blackSelected = new SimpleBooleanProperty();
    private BooleanProperty whiteSelected = new SimpleBooleanProperty();
    private BooleanProperty randomSelected = new SimpleBooleanProperty();

    public ICommando getStartCommand(){
        return new StartGameCommand();
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
