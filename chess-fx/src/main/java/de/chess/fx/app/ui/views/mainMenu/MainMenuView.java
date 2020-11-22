package de.chess.fx.app.ui.views.mainMenu;

import de.chess.fx.app.i18n.Internalization;
import de.chess.fx.app.ui.views.UIView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class MainMenuView extends VBox implements Internalization, UIView {
    private static final double FONT_SIZE = 35;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 50;
    private MainMenuViewModel viewModel;
    private final List<Button> buttonList = new ArrayList<>();
    private Label lblTitle;
    private Separator titleSeparator;

    public MainMenuView() {
        initNodes();
        layoutNodes();
        confugureView();
        initActionsEvents();
        initViewModel();


    }

    private void layoutNodes() {
        this.getChildren().add(lblTitle);

        this.getChildren().add(titleSeparator);
        this.getChildren().addAll(buttonList);
    }


    @Override
    public List<Node> initNodes() {
        List<Node> nodeList = new ArrayList<>();
        lblTitle = new Label(i18n("menu.title"));
        lblTitle.setFont(new Font(FONT_SIZE));
        nodeList.add(addButton(i18n("menu.hostGame")));
        nodeList.add(addButton(i18n("menu.joinGame")));
        nodeList.add(addButton(i18n("menu.exitGame")));
        nodeList.add(lblTitle);
        titleSeparator = new Separator();
        titleSeparator.setMaxWidth(200);
        return nodeList;
    }

    private Button addButton(String title) {
        Button newButton = new Button(title);
        buttonList.add(newButton);
        return newButton;
    }


    @Override
    public void initActionsEvents() {
        buttonList.get(0).setOnAction(event -> viewModel.getToHostGameMenuCommand(getScene()).execute());
        buttonList.get(1).setOnAction(event -> viewModel.getToJoinMenuCommand(getScene()).execute());
        buttonList.get(2).setOnAction(event -> viewModel.getExitGameCommand().execute());
    }

    @Override
    public void initViewModel() {
        viewModel = new MainMenuViewModel();
    }

    @Override
    public void confugureView() {
        buttonList.forEach(e -> e.setMinWidth(BUTTON_WIDTH));
        buttonList.forEach(e -> e.setMinHeight(BUTTON_HEIGHT));
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(BUTTON_WIDTH);
    }
}
