package de.chess.fx.app;

import de.chess.fx.app.audio.MusicPlayer;
import de.chess.fx.app.client.IClientProperties;
import de.chess.fx.app.client.LocalDevPClientProperties;
import de.chess.fx.app.client.ResponseAnalyzer;
import de.chess.fx.app.ui.views.mainMenu.MainMenuView;
import de.chess.io.client.GameClient;
import de.chess.io.client.IGameClientApplication;
import de.chess.io.client.IResponseAnalyzer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessFX extends Application implements IGameClientApplication {
    private static final Logger LOGGER = Logger.getGlobal();
    private static final double FRAME_WIDTH = 1300;
    private static final double FRAME_HEIGHT = 850;
    private final MusicPlayer musicPlayer;

    public ChessFX() throws URISyntaxException {
        this.musicPlayer = new MusicPlayer();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info("************* - Starting ChessFX UI - ************");

        IClientProperties clientProperties = LocalDevPClientProperties.instance();
        MainMenuView mainMenu = new MainMenuView();

        StackPane stackPane = new StackPane(mainMenu);
        stackPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(stackPane, FRAME_WIDTH, FRAME_HEIGHT);
        stage.setScene(scene);

        stage.show();
        stage.setTitle(clientProperties.getApplicationTitle());

        this.musicPlayer.playBackgroundMusic();
        connect(clientProperties.getServerAddress(), clientProperties.getServerPort());
    }

    public void connect(String serverAddress, int serverPort) {
            IResponseAnalyzer responseAnalyzer = new ResponseAnalyzer();
            GameClient client = GameClient.getAndIniTInstance(serverAddress, serverPort, responseAnalyzer);
            client.execute();

    }
}
