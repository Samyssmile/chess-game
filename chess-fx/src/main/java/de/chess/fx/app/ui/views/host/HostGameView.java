package de.chess.fx.app.ui.views.host;

import de.chess.dto.ChessGame;
import de.chess.fx.app.handler.EventData;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.fx.app.handler.IChannel;
import de.chess.fx.app.i18n.Internalization;
import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.maingameview.GameView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class HostGameView extends VBox implements Internalization, UIView, IChannel {

    private static final double SPACING = 10;
    private static final int RIGHT_COL_WIDTH = 200;
    private static final int LEFT_COL_WIDTH = 100;
    private static final int HEIGHT = 20;
    private static final double FONT_SIZE = 35;
    private final List<Node> nodeList;
    private final HBox hBoxHostName;
    private final HBox hBoxStartAsButtons;
    private final HBox hBoxStartAs;
    private final HBox hBoxRankedGame;
    private final HBox hBoxPlayerName;
    private final HBox hBoxLimit;
    private TextField txtPlayerName;
    private Label lblTitle;
    private TextField txtHostName;
    private Label lblHostName;
    private Label lblStartAs;
    private Label lblRankedGame;
    private CheckBox cbIsRankedGame;
    private RadioButton radioBlack;
    private RadioButton radioWhite;
    private RadioButton radioRandom;
    private Button btnBack;
    private Button btnStart;


    private ToggleGroup radioToggleGroup;
    private HostGameViewModel viewModel;
    private Label lblPlayerName;
    private Label lblTimeLimit;
    private ComboBox<String> comboTimeLimit;

    public HostGameView() {
        confugureView();

        nodeList = initNodes();
        initActionsEvents();

        lblTitle.setFont(new Font(FONT_SIZE));

        lblPlayerName.setMinWidth(LEFT_COL_WIDTH);
        txtPlayerName.setMinWidth(RIGHT_COL_WIDTH);

        lblHostName.setMinWidth(LEFT_COL_WIDTH);
        txtHostName.setMinWidth(RIGHT_COL_WIDTH);


        hBoxPlayerName = getConfiguredHBox();
        hBoxPlayerName.getChildren().add(lblPlayerName);
        hBoxPlayerName.getChildren().add(txtPlayerName);

        hBoxHostName = getConfiguredHBox();
        hBoxHostName.getChildren().add(lblHostName);
        hBoxHostName.getChildren().add(txtHostName);

        lblStartAs.setMinWidth(LEFT_COL_WIDTH);

        radioBlack.setToggleGroup(radioToggleGroup);
        radioWhite.setToggleGroup(radioToggleGroup);
        radioRandom.setToggleGroup(radioToggleGroup);

        hBoxStartAsButtons = getConfiguredHBox();
        radioWhite.setSelected(true);
        hBoxStartAsButtons.getChildren().add(radioWhite);
        hBoxStartAsButtons.getChildren().add(radioBlack);
        hBoxStartAsButtons.getChildren().add(radioRandom);

        hBoxStartAs = getConfiguredHBox();
        hBoxStartAs.getChildren().add(lblStartAs);
        hBoxStartAs.getChildren().add(hBoxStartAsButtons);

        hBoxLimit = getConfiguredHBox();
        hBoxLimit.getChildren().add(lblTimeLimit);
        hBoxLimit.getChildren().add(comboTimeLimit);
        comboTimeLimit.getSelectionModel().select(0);
        lblTimeLimit.setMinWidth(LEFT_COL_WIDTH);
        comboTimeLimit.setMinWidth(RIGHT_COL_WIDTH);


        hBoxRankedGame = getConfiguredHBox();
        lblRankedGame.setMinWidth(LEFT_COL_WIDTH);
        cbIsRankedGame.setMinWidth(RIGHT_COL_WIDTH);
        hBoxRankedGame.getChildren().add(lblRankedGame);
        hBoxRankedGame.getChildren().add(cbIsRankedGame);

        structureMenu();
        initViewModel();
        registerForEvents();

    }


    private void registerForEvents() {
        EventHandler.getInstance().registerForEvent(this, EventType.OPEN_NEW_GAME);
    }



    @Override
    public void initViewModel() {
        viewModel = new HostGameViewModel();
        txtPlayerName.textProperty().bindBidirectional((viewModel).playerNameProperty());
        txtHostName.textProperty().bindBidirectional(viewModel.gameNameProperty());
        radioBlack.selectedProperty().bindBidirectional(viewModel.blackSelectedProperty());
        radioWhite.selectedProperty().bindBidirectional(viewModel.whiteSelectedProperty());
        radioRandom.selectedProperty().bindBidirectional(viewModel.randomSelectedProperty());
        viewModel.timeLimitProperty().bind(comboTimeLimit.getSelectionModel().selectedItemProperty());
        cbIsRankedGame.selectedProperty().bindBidirectional(viewModel.isRankedGameProperty());
    }

    @Override
    public void confugureView() {
        this.setPadding(new Insets(50));
        this.setSpacing(SPACING);
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(RIGHT_COL_WIDTH);
        this.setWidth(RIGHT_COL_WIDTH);

    }

    @Override
    public void initActionsEvents() {
        btnBack.setOnAction(event -> viewModel.getToMainMenuCommand(getScene()).execute());
        btnStart.setOnAction(event -> viewModel.requestHosting(getScene()).execute());
    }

    private void structureMenu() {
        this.getChildren().add(lblTitle);
        this.getChildren().add(getConfiguredSeparator());
        this.getChildren().add(hBoxPlayerName);
        this.getChildren().add(getConfiguredSeparator());
        this.getChildren().add(hBoxHostName);
        this.getChildren().add(getConfiguredSeparator());
        this.getChildren().add(hBoxStartAs);
        this.getChildren().add(getConfiguredSeparator());
        this.getChildren().add(hBoxLimit);
        this.getChildren().add(getConfiguredSeparator());
        this.getChildren().add(hBoxRankedGame);
        this.getChildren().add(getConfiguredSeparator());
        this.getChildren().add(btnStart);
        this.getChildren().add(btnBack);
    }

    private Button getConfiguredButton(String i18n) {
        Button button = new Button(i18n);
        button.setMinWidth(RIGHT_COL_WIDTH + SPACING + LEFT_COL_WIDTH);
        return button;
    }

    @Override
    public List<Node> initNodes() {
        List<Node> nodeList = new ArrayList<>();
        lblTitle = new Label(i18n("menu.hostGame.title"));
        lblPlayerName = new Label(i18n("menu.hostGame.playerName"));
        txtPlayerName = new TextField();
        lblHostName = new Label(i18n("menu.hostGame.gameName"));
        txtHostName = new TextField();
        lblStartAs = new Label(i18n("menu.hostGame.startAs"));
        radioToggleGroup = new ToggleGroup();
        radioBlack = new RadioButton(i18n("menu.hostGame.black"));
        radioWhite = new RadioButton(i18n("menu.hostGame.white"));
        radioRandom = new RadioButton(i18n("menu.hostGame.random"));
        btnStart = getConfiguredButton(i18n("menu.hostGame.start"));
        btnBack = getConfiguredButton(i18n("menu.hostGame.back"));
        lblRankedGame = new Label(i18n("menu.hostGame.isRankedGame"));
        cbIsRankedGame = new CheckBox();
        lblTimeLimit = new Label(i18n("menu.hostGame.timeLimit"));
        comboTimeLimit = new ComboBox<>(getLimitList());

        nodeList.add(lblTitle);
        nodeList.add(lblPlayerName);
        nodeList.add(txtPlayerName);
        nodeList.add(lblHostName);
        nodeList.add(txtHostName);
        nodeList.add(lblStartAs);
        nodeList.add(radioBlack);
        nodeList.add(radioWhite);
        nodeList.add(radioRandom);
        nodeList.add(btnStart);
        nodeList.add(btnBack);
        nodeList.add(lblRankedGame);

        return nodeList;
    }

    private ObservableList<String> getLimitList() {
        return FXCollections.observableArrayList(
                "5:00", "10:00", "20:00", "40:00", "60:00", "120:00");
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




    @Override
    public void update(EventType eventType, EventData eventData) {
        switch (eventType){
            case OPEN_NEW_GAME:
                ChessGame chessGame = (ChessGame) eventData.getData();
                getScene().setRoot(new GameView(chessGame));
        }

    }
}
