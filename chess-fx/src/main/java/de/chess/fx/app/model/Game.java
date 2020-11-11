package de.chess.fx.app.model;

import javafx.beans.property.SimpleStringProperty;

public class Game {
    private SimpleStringProperty creator;
    private SimpleStringProperty oppnent;
    private SimpleStringProperty gameName ;
    private SimpleStringProperty creatorsColor;
    private SimpleStringProperty timeElapsed;

    public Game(String creatorValue,String gameNameValue , String creatorsColorValue) {
        this(creatorValue, "",gameNameValue,creatorsColorValue,"00:00");
    }

    public Game(String creatorValue, String oppnentValue, String gameNameValue, String creatorsColorValue, String timeElapsedValue) {
        this.creator = new SimpleStringProperty(creatorValue);
        this.oppnent = new SimpleStringProperty(oppnentValue);
        this.gameName = new SimpleStringProperty(gameNameValue);
        this.creatorsColor = new SimpleStringProperty(creatorsColorValue);
        this.timeElapsed = new SimpleStringProperty(timeElapsedValue);
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
}
