package de.chess.fx.app.model;

import de.chess.model.ChessColor;
import de.chess.model.GameType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;
import java.util.UUID;

public class GameRowData {

    private UUID uuid;
    private final SimpleStringProperty creator;
    private final SimpleStringProperty oppnent;
    private final SimpleStringProperty gameName;
    private final SimpleObjectProperty<ChessColor> creatorsColor;
    private final SimpleStringProperty timeElapsed;
    private final SimpleStringProperty timeLimit;
    private final SimpleObjectProperty<GameType> isRankedGame;


    public GameRowData(UUID uuid, String creatorValue, String gameNameValue, ChessColor creatorsColorValue) {
        this(uuid, creatorValue, "", gameNameValue, creatorsColorValue, "00:00", "05:00", GameType.FUN);
    }

    public GameRowData(UUID uuid, String creatorValue, String oppnentValue, String gameNameValue, ChessColor creatorsColorValue, String timeElapsedValue, String timeLimitValue, GameType isRankedGameValue) {
        this.uuid= uuid;
        this.creator = new SimpleStringProperty(creatorValue);
        this.oppnent = new SimpleStringProperty(oppnentValue);
        this.gameName = new SimpleStringProperty(gameNameValue);
        this.creatorsColor = new SimpleObjectProperty<>(creatorsColorValue);
        this.timeElapsed = new SimpleStringProperty(timeElapsedValue);
        this.timeLimit = new SimpleStringProperty(timeLimitValue);
        this.isRankedGame = new SimpleObjectProperty<>(isRankedGameValue);
    }

    public GameRowData(UUID uuid, String creatorValue, String gameNameValue, ChessColor creatorsColorValue, String timeLimitValue) {
        this(uuid, creatorValue, "", gameNameValue, creatorsColorValue, "00:00", timeLimitValue, GameType.FUN);
    }


    public GameType getIsRankedGame() {
        return isRankedGame.get();
    }

    public SimpleObjectProperty<GameType> isRankedGameProperty() {
        return isRankedGame;
    }

    public void setIsRankedGame(GameType isRankedGame) {
        this.isRankedGame.set(isRankedGame);
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

    public ChessColor getCreatorsColor() {
        return creatorsColor.get();
    }

    public SimpleObjectProperty<ChessColor> creatorsColorProperty() {
        return creatorsColor;
    }

    public void setCreatorsColor(ChessColor creatorsColor) {
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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRowData game = (GameRowData) o;
        return Objects.equals(uuid, game.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
