package de.chess.fx.app.ui.host;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class HostGameUi extends VBox implements Internalization {

    private static final double SPACING = 10;
    private final HBox hBoxHostName;
    private final HBox hBoxStartAsButtons;
    private final HBox hBoxStartAs;
    private TextField txtHostName;
    private Label lblHostName;
    private Label lblStartAs;
    private RadioButton radioBlack;
    private RadioButton radioWhite;
    private RadioButton radioRandom;
    private Button btnBack;
    private Button btnStart;

    private static final int WIDTH = 200;
    private static final int HEIGHT = 50;
    private final ToggleGroup radioToggleGroup;

    public HostGameUi() {
        this.setPadding(new Insets(50));
        this.setSpacing(SPACING);
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(WIDTH);
        this.setWidth(WIDTH);

        lblHostName = new Label(i18n("menu.hostGame.gameName"));
        lblHostName.setMinWidth(100);
        txtHostName = new TextField();
        txtHostName.setMinWidth(200);
        hBoxHostName = getConfiguredHBox();
        hBoxHostName.setAlignment(Pos.CENTER);
        hBoxHostName.setPrefWidth(WIDTH);
        hBoxHostName.setSpacing(SPACING);
        hBoxHostName.getChildren().add(lblHostName);
        hBoxHostName.getChildren().add(txtHostName);

        lblStartAs = new Label(i18n("menu.hostGame.startAs"));
        lblStartAs.setMinWidth(100);
        radioToggleGroup = new ToggleGroup();
        radioBlack = new RadioButton(i18n("menu.hostGame.black"));
        radioWhite = new RadioButton(i18n("menu.hostGame.white"));
        radioRandom = new RadioButton(i18n("menu.hostGame.random"));
        radioBlack.setToggleGroup(radioToggleGroup);
        radioWhite.setToggleGroup(radioToggleGroup);
        radioRandom.setToggleGroup(radioToggleGroup);
        hBoxStartAsButtons = getConfiguredHBox();
        hBoxStartAsButtons.setSpacing(SPACING);
        hBoxStartAsButtons.getChildren().add(radioBlack);
        hBoxStartAsButtons.getChildren().add(radioWhite);
        hBoxStartAsButtons.getChildren().add(radioRandom);
        hBoxStartAsButtons.setMinWidth(200);
        hBoxStartAs = getConfiguredHBox();
        hBoxStartAs.setAlignment(Pos.CENTER);
        hBoxStartAs.setPrefWidth(WIDTH);
        hBoxStartAs.setSpacing(SPACING);
        hBoxStartAs.getChildren().add(lblStartAs);
        hBoxStartAs.getChildren().add(hBoxStartAsButtons);

        btnStart = new Button(i18n("menu.hostGame.start"));
        btnStart.setMinWidth(300);
        btnBack = new Button(i18n("menu.hostGame.back"));
        btnBack.setMinWidth(300);


        this.getChildren().add(hBoxHostName);
        this.getChildren().add(new Separator());
        this.getChildren().add(hBoxStartAs);
        this.getChildren().add(new Separator());
        this.getChildren().add(btnStart);
        this.getChildren().add(btnBack);
    }


    private HBox getConfiguredHBox(){
        HBox hBox = new HBox();
        hBox.setSpacing(SPACING);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPrefWidth(WIDTH);
        hBox.setMinHeight(HEIGHT);
        hBox.setMinWidth(WIDTH);
        return hBox;

    }



}
