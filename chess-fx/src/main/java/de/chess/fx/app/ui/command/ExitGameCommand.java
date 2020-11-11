package de.chess.fx.app.ui.command;

import javafx.application.Platform;

import java.util.logging.Level;

public class ExitGameCommand implements ICommando{
    @Override
    public void execute() {
        LOGGER.log(Level.SEVERE, "Close Application Event");
        Platform.exit();
        System.exit(0);
    }
}
