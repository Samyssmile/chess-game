package de.chess.fx.app.ui.command.menu;

import de.chess.dto.request.ReceiveGameListRequest;
import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.views.join.JoinGameView;
import de.chess.io.client.GameClient;
import javafx.scene.Scene;

import java.util.Optional;
import java.util.logging.Level;

public class ToJoinMenuCommand implements ICommando {
    private final Scene scene;

    public ToJoinMenuCommand(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Enter Game Browser Command...");
        scene.setRoot(new JoinGameView());
    }
}
