package de.chess.fx.app.ui.views.mainMenu;

import de.chess.fx.app.ui.command.ExitGameCommand;
import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.command.menu.ToHostGameMenuCommand;
import de.chess.fx.app.ui.command.menu.ToJoinMenuCommand;
import javafx.scene.Scene;

public class MainMenuViewModel {



    public ICommando getToHostGameMenuCommand(Scene scene) {
        return new ToHostGameMenuCommand(scene);
    }

    public ICommando getToJoinMenuCommand(Scene scene) {
        return new ToJoinMenuCommand(scene);
    }

    public ICommando getExitGameCommand() {
        return new ExitGameCommand();
    }
}
