package de.chess.fx.app.ui.command;

import de.chess.fx.app.ui.MainMenu;
import javafx.scene.Scene;

public class ToMainMenuCommand implements ICommando {

    private final Scene scene;

    public ToMainMenuCommand(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void execute() {
        scene.setRoot(new MainMenu());
    }
}
