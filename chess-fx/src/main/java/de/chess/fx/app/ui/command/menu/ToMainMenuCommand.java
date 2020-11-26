package de.chess.fx.app.ui.command.menu;

import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.views.mainMenu.MainMenuView;
import javafx.scene.Scene;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ToMainMenuCommand implements ICommando {
    private final Scene scene;

    public ToMainMenuCommand(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void execute() {
        LOGGER.log(Level.INFO, "Enter Main Menu Command...");
        scene.setRoot(new MainMenuView());
    }
}
