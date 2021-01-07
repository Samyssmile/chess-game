package de.chess.fx.app.ui.views.gameboard;

import de.chess.dto.ChessGame;
import de.chess.dto.Player;
import de.chess.fx.app.audio.AudioEffectPlayer;
import de.chess.fx.app.audio.AudioEffectType;
import de.chess.fx.app.handler.EventData;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.fx.app.handler.IChannel;
import de.chess.fx.app.i18n.Internalization;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GameViewModel implements Internalization, IChannel {

    private StringProperty hostPlayerName = new SimpleStringProperty(i18n("game.default.hostname"));
    private StringProperty clientPlayerName = new SimpleStringProperty(i18n("game.client.hostname"));
    private AudioEffectPlayer audioEffectPlayer;
    public GameViewModel( ) {
        audioEffectPlayer = new AudioEffectPlayer();
        registerForEvents();
    }


    private void registerForEvents() {
        EventHandler.getInstance().registerForEvent(this, EventType.PLAYER_JOINED);
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

    @Override
    public void update(EventType eventType) {
        switch (eventType){
            case PLAYER_JOINED -> audioEffectPlayer.playSound(AudioEffectType.PLAYER_JOINED);
        }
    }

    @Override
    public void update(EventType eventType, EventData eventData) {
        switch (eventType){
            case PLAYER_JOINED:
                Player joinedPlayer = (Player) eventData.getData();
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        clientPlayerName.setValue(joinedPlayer.getNickname());
                    }
                });

        }

    }


    public void setChessGame(ChessGame gameDTO) {
        hostPlayerName.setValue(gameDTO.getHostPlayerName());
    }
}
