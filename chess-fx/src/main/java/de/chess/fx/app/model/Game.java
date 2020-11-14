package de.chess.fx.app.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.Objects;

public class Game implements Serializable {
    private SimpleStringProperty creator;
    private SimpleStringProperty oppnent;
    private SimpleStringProperty gameName ;
    private SimpleStringProperty creatorsColor;
    private SimpleStringProperty timeElapsed;
    private SimpleStringProperty timeLimit;

    private SimpleBooleanProperty isRankedGame;

    public Game(String creatorValue,String gameNameValue , String creatorsColorValue) {
        this(creatorValue, "",gameNameValue,creatorsColorValue,"00:00", "05:00");
    }

    public Game(String creatorValue, String oppnentValue, String gameNameValue, String creatorsColorValue, String timeElapsedValue, String timeLimitValue) {
        this.creator = new SimpleStringProperty(creatorValue);
        this.oppnent = new SimpleStringProperty(oppnentValue);
        this.gameName = new SimpleStringProperty(gameNameValue);
        this.creatorsColor = new SimpleStringProperty(creatorsColorValue);
        this.timeElapsed = new SimpleStringProperty(timeElapsedValue);
        this.timeLimit = new SimpleStringProperty(timeLimitValue);
    }

    public Game(String creatorValue,String gameNameValue , String creatorsColorValue, String timeLimitValue) {
        this(creatorValue, "",gameNameValue,creatorsColorValue,"00:00", timeLimitValue);
    }


    public String getTimeLimit() {
        return timeLimit.get();
    }

    public SimpleStringProperty timeLimitProperty() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit.set(timeLimit);
    }

    public String getCreator() {
        return creator.get();
    }

    public SimpleStringProperty creatorProperty() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator.set(creator);
    }

    public String getOppnent() {
        return oppnent.get();
    }

    public SimpleStringProperty oppnentProperty() {
        return oppnent;
    }

    public void setOppnent(String oppnent) {
        this.oppnent.set(oppnent);
    }

    public String getGameName() {
        return gameName.get();
    }

    public SimpleStringProperty gameNameProperty() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName.set(gameName);
    }

    public String getCreatorsColor() {
        return creatorsColor.get();
    }

    public SimpleStringProperty creatorsColorProperty() {
        return creatorsColor;
    }

    public void setCreatorsColor(String creatorsColor) {
        this.creatorsColor.set(creatorsColor);
    }

    public String getTimeElapsed() {
        return timeElapsed.get();
    }

    public SimpleStringProperty timeElapsedProperty() {
        return timeElapsed;
    }

    public void setTimeElapsed(String timeElapsed) {
        this.timeElapsed.set(timeElapsed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(creator, game.creator) && Objects.equals(oppnent, game.oppnent) && Objects.equals(gameName, game.gameName) && Objects.equals(creatorsColor, game.creatorsColor) && Objects.equals(timeElapsed, game.timeElapsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creator, oppnent, gameName, creatorsColor, timeElapsed);
    }
}
