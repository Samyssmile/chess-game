package de.chess.fx.app.ui.views;

import javafx.scene.Node;

import java.util.List;

public interface UIView {

    /**
     * Init all UI Component Node and return them as list.
     * @return Node List
     */
    public List<Node> initNodes();

    /**
     * Initialize Action Events
     */
    public  void initActionsEvents();

    public void initViewModel();

    /**
     * Configure things like width, hieight, colors, fonts here.
     */
    public void confugureView();
}
