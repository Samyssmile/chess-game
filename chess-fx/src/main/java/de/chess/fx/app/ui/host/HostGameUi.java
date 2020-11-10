package de.chess.fx.app.ui.host;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class HostGameUi extends VBox implements Internalization {

    private static final double SPACING = 10;
    private static final int RIGHT_COL_WIDTH = 200;
    private static final int LEFT_COL_WIDTH = 100;
    private static final int HEIGHT = 50;
    private static final double FONT_SIZE = 35;
    private final List<Node> nodeList;
    private HBox hBoxHostName;
    private HBox hBoxStartAsButtons;
    private HBox hBoxStartAs;
    private Label lblTitle;
    private TextField txtHostName;
    private Label lblHostName;
    private Label lblStartAs;
    private RadioButton radioBlack;
    private RadioButton radioWhite;
    private RadioButton radioRandom;
    private Button btnBack;
    private Button btnStart;


    private ToggleGroup radioToggleGroup;

    public HostGameUi() {
        this.setPadding(new Insets(50));
        this.setSpacing(SPACING);
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(RIGHT_COL_WIDTH);
        this.setWidth(RIGHT_COL_WIDTH);

        nodeList = createNodes();

        lblTitle.setFont(new Font(FONT_SIZE));

        lblHostName.setMinWidth(LEFT_COL_WIDTH);
        txtHostName.setMinWidth(RIGHT_COL_WIDTH);

        hBoxHostName = getConfiguredHBox();
        hBoxHostName.getChildren().add(lblHostName);
        hBoxHostName.getChildren().add(txtHostName);

        lblStartAs.setMinWidth(LEFT_COL_WIDTH);

        radioBlack.setToggleGroup(radioToggleGroup);
        radioWhite.setToggleGroup(radioToggleGroup);
        radioRandom.setToggleGroup(radioToggleGroup);

        hBoxStartAsButtons = getConfiguredHBox();
        hBoxStartAsButtons.getChildren().add(radioBlack);
        hBoxStartAsButtons.getChildren().add(radioWhite);
        hBoxStartAsButtons.getChildren().add(radioRandom);

        hBoxStartAs = getConfiguredHBox();
        hBoxStartAs.getChildren().add(lblStartAs);
        hBoxStartAs.getChildren().add(hBoxStartAsButtons);

        structureMenu();
    }

    private void structureMenu() {
        this.getChildren().add(lblTitle);
        this.getChildren().add(getConfiguredSeparator());
        this.getChildren().add(hBoxHostName);
        this.getChildren().add(getConfiguredSeparator());
        this.getChildren().add(hBoxStartAs);
        this.getChildren().add(getConfiguredSeparator());
        this.getChildren().add(btnStart);
        this.getChildren().add(btnBack);
    }

    private Button getConfiguredButton(String i18n) {
        Button button = new Button(i18n);
        button.setMinWidth(RIGHT_COL_WIDTH + SPACING + LEFT_COL_WIDTH);
        return button;
    }

    private List<Node> createNodes() {
        List<Node> nodeList = new ArrayList<>();
        lblTitle = new Label(i18n("menu.hostGame.title"));
        lblHostName = new Label(i18n("menu.hostGame.gameName"));
        txtHostName = new TextField();
        lblStartAs = new Label(i18n("menu.hostGame.startAs"));
        radioToggleGroup = new ToggleGroup();
        radioBlack = new RadioButton(i18n("menu.hostGame.black"));
        radioWhite = new RadioButton(i18n("menu.hostGame.white"));
        radioRandom = new RadioButton(i18n("menu.hostGame.random"));
        btnStart = getConfiguredButton(i18n("menu.hostGame.start"));
        btnBack = getConfiguredButton(i18n("menu.hostGame.back"));

        nodeList.add(lblTitle);
        nodeList.add(lblHostName);
        nodeList.add(txtHostName);
        nodeList.add(lblStartAs);
        nodeList.add(radioBlack);
        nodeList.add(radioWhite);
        nodeList.add(radioRandom);
        nodeList.add(btnStart);
        nodeList.add(btnBack);
        return nodeList;
    }


    private HBox getConfiguredHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(SPACING);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefWidth(RIGHT_COL_WIDTH);
        hBox.setMinHeight(HEIGHT);
        hBox.setMinWidth(RIGHT_COL_WIDTH);

        return hBox;

    }

    private Separator getConfiguredSeparator() {
        Separator separator = new Separator();
        separator.setMaxWidth(RIGHT_COL_WIDTH + SPACING + LEFT_COL_WIDTH);
        return separator;
    }


}
