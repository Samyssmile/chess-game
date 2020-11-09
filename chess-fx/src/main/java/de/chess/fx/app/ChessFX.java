package de.chess.fx.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class ChessFX extends Application {
  private static final Logger LOGGER = Logger.getLogger(ChessFX.class.getName());

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    LOGGER.info("************* - Starting ChessFX UI - ************");

    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    Label l =
        new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
    Scene scene = new Scene(new StackPane(l), 900, 650);
    stage.setScene(scene);
    stage.show();
  }
}
