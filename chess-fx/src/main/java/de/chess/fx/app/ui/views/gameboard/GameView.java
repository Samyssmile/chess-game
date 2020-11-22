package de.chess.fx.app.ui.views.gameboard;


import de.chess.fx.app.ui.views.UIView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GameView extends BorderPane implements UIView {
    private static final double TITLE_FONT_SIZE = 55;
    private Label labelTitle;
    private Label labelPlayerHostName;
    private Label labelPlayerClientName;
    private Label labelPlayerHostElo;
    private Label labelPlayerVisitorElo;
    private Label labelCountDown;

    private ChatView chatView;
    private GameBoardView gameBoardView;
    private Button buttonGiveUp;
    private Button buttonRemisRequest;

    private HBox topHBox;
    private HBox bottomHBox;
    private List<Node> nodeList;

    private GameViewModel viewModel;

    public GameView( ) {
        this.nodeList = new ArrayList<>();

        initNodes();
        initViewModel();
        this.topHBox = new HBox(labelPlayerHostName, labelTitle, labelPlayerClientName);
        this.bottomHBox = new HBox(buttonGiveUp, buttonRemisRequest);

        this.setCenter(gameBoardView);
        this.setRight(chatView);
        this.setTop(topHBox);
        this.setBottom(     this.bottomHBox );


        confugureView();
    }

    @Override
    public List<Node> initNodes() {
        labelTitle = new Label(" - vs - ");
        labelPlayerHostName = new Label();
        labelPlayerClientName = new Label();
        labelPlayerHostElo = new Label("1800");
        labelPlayerVisitorElo = new Label("1566");
        labelCountDown = new Label("3.2.1.GO");
        chatView = new ChatView();
        gameBoardView = new GameBoardView();
        buttonGiveUp = new Button("I give up");
        buttonRemisRequest = new Button("Suggest Draw - Remis");

        nodeList.add(labelPlayerHostName);
        nodeList.add(labelPlayerClientName);
        nodeList.add(labelPlayerHostElo);
        nodeList.add(labelPlayerVisitorElo);
        nodeList.add(chatView);
        nodeList.add(gameBoardView);
        nodeList.add(buttonGiveUp);
        nodeList.add(labelCountDown);
        nodeList.add(labelTitle);

        return nodeList;
    }

    @Override
    public void initActionsEvents() {

    }

    @Override
    public void initViewModel() {
        this.viewModel = new GameViewModel();

        labelPlayerHostName.textProperty().bindBidirectional(viewModel.hostPlayerNameProperty());
        labelPlayerClientName.textProperty().bindBidirectional(viewModel.clientPlayerNameProperty());
    }

    @Override
    public void confugureView() {
        this.bottomHBox .setAlignment(Pos.CENTER);
        this.bottomHBox.setSpacing(10);
        this.bottomHBox.setPadding(new Insets(10));
        this.topHBox.setAlignment(Pos.CENTER);
        this.labelTitle.setFont(new Font(TITLE_FONT_SIZE));
    }
}
