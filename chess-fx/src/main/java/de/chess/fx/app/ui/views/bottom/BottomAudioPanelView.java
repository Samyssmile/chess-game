package de.chess.fx.app.ui.views.bottom;

import de.chess.fx.app.i18n.Internalization;
import de.chess.fx.app.ui.views.UIView;
import de.chess.fx.app.ui.views.mainMenu.MainMenuViewModel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class BottomAudioPanelView  extends HBox implements Internalization, UIView {
    private final List<Node> uiElements;
    private BottomAudioPanelViewModel viewModel;
    private Button muteAudioButton;

    public BottomAudioPanelView() {
        this.uiElements = initNodes();
        layoutNodes();
        confugureView();
        initActionsEvents();
        initViewModel();
    }

    private void layoutNodes() {
            this.getChildren().addAll(uiElements);

    }

    @Override
    public List<Node> initNodes() {
        List<Node> nodeList = new ArrayList<>();
        muteAudioButton = new Button(i18n("menu.muteAudio"));
        nodeList.add(muteAudioButton);
        return nodeList;
    }

    @Override
    public void initActionsEvents() {
        muteAudioButton.setOnAction(event -> viewModel.muteAudioCommand().execute());
    }

    @Override
    public void initViewModel() {
            viewModel = new BottomAudioPanelViewModel();
    }

    @Override
    public void confugureView() {

    }
}
