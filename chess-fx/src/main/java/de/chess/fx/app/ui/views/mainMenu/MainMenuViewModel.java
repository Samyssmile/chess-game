package de.chess.fx.app.ui.views.mainMenu;

import de.chess.fx.app.ui.command.ExitGameCommand;
import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.command.ToHostGameCommand;
import de.chess.fx.app.ui.command.ToJoinMenuCommand;
import javafx.scene.Scene;

public class MainMenuViewModel {



    public ICommando getToHostGameMenuCommand(Scene scene) {
        return new ToHostGameCommand(scene);
    }

    public ICommando getToJoinMenuCommand(Scene scene) {
        return new ToJoinMenuCommand(scene);
    }

    public ICommando getExitGameCommand() {
        return new ExitGameCommand();
    }
}
