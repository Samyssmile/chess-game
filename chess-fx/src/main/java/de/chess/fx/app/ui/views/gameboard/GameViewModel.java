package de.chess.fx.app.ui.views.gameboard;

import de.chess.fx.app.i18n.Internalization;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GameViewModel implements Internalization {

    private StringProperty hostPlayerName = new SimpleStringProperty(i18n("game.default.hostname"));
    private StringProperty clientPlayerName = new SimpleStringProperty(i18n("game.client.hostname"));

    public GameViewModel( ) {

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
}
