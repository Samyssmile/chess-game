package de.chess.fx.app.ui.views.join;

import de.chess.fx.app.i18n.Internalization;
import de.chess.fx.app.model.GameRowData;
import de.chess.fx.app.ui.views.UIView;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class JoinGameView extends VBox implements Internalization, UIView {

    private static final double SPACING = 10;
    private static final double FONT_SIZE = 35;
    private final List<Node> nodeList;
    private TableColumn gameName;
    private TableColumn creator;
    private TableColumn creatorsColor;
    private TableColumn timeElapsed;
    private TableColumn isRanked;
    private Label title;
    private Button btnRefresh;
    private Button btnJoin;
    private Button btnLeave;
    private TableView table;
    private JoinGameViewModel viewModel;
    private TableColumn timeLimit;


    public JoinGameView() {
        initViewModel();
        table = setupGameBrowserTable();
        nodeList = initNodes();
        initActionsEvents();
        confugureView();

        getChildren().add(title);
        getChildren().add(table);
        getChildren().add(btnRefresh);
        getChildren().add(btnJoin);
        getChildren().add(btnLeave);
    }


    private TableView setupGameBrowserTable() {
        TableView table = new TableView();
        gameName = new TableColumn(i18n("menu.joinGame.gameName"));
        gameName.setCellValueFactory(new PropertyValueFactory<GameRowData, String>("gameName"));
        creator = new TableColumn(i18n("menu.joinGame.creator"));
        creator.setCellValueFactory(new PropertyValueFactory<GameRowData, String>("creator"));
        creatorsColor = new TableColumn(i18n("menu.joinGame.color"));
        creatorsColor.setCellValueFactory(new PropertyValueFactory<GameRowData, String>("creatorsColor"));
        timeElapsed = new TableColumn(i18n("menu.joinGame.runingSince"));
        timeElapsed.setCellValueFactory(new PropertyValueFactory<GameRowData, String>("timeElapsed"));

        timeLimit = new TableColumn(i18n("menu.joinGame.timeLimit"));
        timeLimit.setCellValueFactory(new PropertyValueFactory<GameRowData, String>("timeLimit"));

        isRanked = new TableColumn(i18n("menu.joinGame.isRanked"));
        isRanked.setCellValueFactory(new PropertyValueFactory<GameRowData, String>("isRankedGame"));

        table.getColumns().addAll(gameName, creator, creatorsColor, timeElapsed, timeLimit, isRanked);

        viewModel.getData().addListener(new ListChangeListener<GameRowData>() {
            @Override
            public void onChanged(Change<? extends GameRowData> c) {
                System.out.println("Changed!!");
                table.setItems(viewModel.getData());
            }
        });

        viewModel.selectedGameProperty.bind(table.getSelectionModel().selectedItemProperty());
        return table;
    }

    @Override
    public List<Node> initNodes() {
        List<Node> nodeList = new ArrayList<>();
        title = new Label(i18n("menu.joinGame.title"));
        title.setFont(new Font(FONT_SIZE));


        btnRefresh = new Button(i18n("menu.joinGame.refresh"));
        btnRefresh.setMinWidth(200);

        btnJoin = new Button(i18n("menu.joinGame.join"));
        btnJoin.setMinWidth(200);
        btnJoin.disableProperty().bindBidirectional(viewModel.isJoinButtonDisabled);

        btnLeave = new Button(i18n("menu.joinGame.back"));
        btnLeave.setMinWidth(200);

        nodeList.add(table);
        nodeList.add(title);
        nodeList.add(btnRefresh);
        nodeList.add(btnJoin);
        nodeList.add(btnLeave);

        return nodeList;
    }

    @Override
    public void initActionsEvents() {
        btnRefresh.setOnAction(event -> viewModel.getRefreshGameListCommand().execute());
        btnJoin.setOnAction(event -> viewModel.getJoinGameCommand(getScene()).execute());
        btnLeave.setOnAction(event -> viewModel.getToMainMenuCommand(getScene()).execute());
    }

    @Override
    public void initViewModel() {
        this.viewModel = new JoinGameViewModel();
    }

    @Override
    public void confugureView() {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(50));
        this.setSpacing(SPACING);
    }
}
