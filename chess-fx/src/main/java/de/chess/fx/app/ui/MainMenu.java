package de.chess.fx.app.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends VBox {

  private List<Button> buttonList = new ArrayList<>();
  private static final int BUTTON_WIDTH = 120;
  private static final int BUTTON_HEIGHT = 50;

  public MainMenu() {
    this.setPadding(new Insets(10));
    this.setSpacing(10);
    this.setAlignment(Pos.CENTER);
    this.setPrefWidth(BUTTON_WIDTH);

    createMenuButtons();
    configureLayout();
    addButtons();
  }

  private void addButtons() {
    this.getChildren().addAll(buttonList);
  }

  private void configureLayout() {
    buttonList.stream().forEach(e -> e.setMinWidth(BUTTON_WIDTH));
    buttonList.stream().forEach(e -> e.setMinHeight(BUTTON_HEIGHT));
  }

  private void createMenuButtons() {
    addButton("Host Game");
    addButton("Join Game");
    addButton("Exit Game");
  }

  private void addButton(String title) {
    Button newButton = new Button(title);
    buttonList.add(newButton);
  }


}
