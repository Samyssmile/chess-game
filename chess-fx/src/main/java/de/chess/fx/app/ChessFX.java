package de.chess.fx.app;

import de.chess.fx.app.audio.MusicPlayer;
import de.chess.fx.app.client.ClientProperties;
import de.chess.fx.app.ui.MainMenu;
import de.chess.fx.app.ui.host.HostGameUi;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.util.logging.Logger;

public class ChessFX extends Application {
    private static final Logger LOGGER = Logger.getLogger(ChessFX.class.getName());

    private MusicPlayer musicPlayer;

    public ChessFX() throws URISyntaxException {
        musicPlayer = new MusicPlayer();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info("************* - Starting ChessFX UI - ************");

        ClientProperties clientProperties = new ClientProperties();
        MainMenu mainMenu = new MainMenu();
        HostGameUi hostGameUi = new HostGameUi();


        Scene scene = new Scene(new StackPane(hostGameUi), 900, 650);
        stage.setScene(scene);
        stage.show();
        stage.setTitle(clientProperties.getApplicationTitle());


        musicPlayer.playBackgroundMusic();

    }
}
