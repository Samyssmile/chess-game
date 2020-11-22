package de.chess.fx.app.ui.views.gameboard;

import de.chess.fx.app.ui.views.UIView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ChatView extends VBox implements UIView {
    private static final double SPACING = 10;
    private static final int RIGHT_COL_WIDTH = 600;
    private static final int LEFT_COL_WIDTH = 100;
    private static final int HEIGHT = 20;
    private static final double FONT_SIZE = 35;
    private TextArea textAreaChat;
    private TextField textFieldChatInput;
    private Button buttonSendChatMessage;
    private HBox hBoxMessageInput;


    private List<Node> nodeList;

    public ChatView() {
        initViewModel();
        initNodes();


        hBoxMessageInput =  getConfiguredHBox();
        hBoxMessageInput.getChildren().add(textFieldChatInput);
        hBoxMessageInput.getChildren().add(buttonSendChatMessage);

        this.getChildren().add(textAreaChat);
        this.getChildren().add(hBoxMessageInput);
        nodeList = new ArrayList<>();

        confugureView();
    }

    @Override
    public List<Node> initNodes() {
        textAreaChat = new TextArea();
        textFieldChatInput = new TextField("Type Message here...");
        textFieldChatInput.setAlignment(Pos.CENTER_LEFT);
        buttonSendChatMessage = new Button("Send");

        return nodeList;
    }

    @Override
    public void initActionsEvents() {

    }

    @Override
    public void initViewModel() {

    }

    @Override
    public void confugureView() {
        this.setPadding(new Insets(0,SPACING,0,SPACING));
        this.setAlignment(Pos.TOP_LEFT);
        this.setPrefWidth(RIGHT_COL_WIDTH);
        this.setWidth(RIGHT_COL_WIDTH);
    }

    private HBox getConfiguredHBox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(SPACING,0,0,0));
        hBox.setAlignment(Pos.TOP_LEFT);
        hBox.setPrefWidth(RIGHT_COL_WIDTH);
        hBox.setMinHeight(HEIGHT);

        HBox.setHgrow(textFieldChatInput, Priority.ALWAYS);
        HBox.setHgrow(buttonSendChatMessage, Priority.ALWAYS);
        return hBox;
    }
}
