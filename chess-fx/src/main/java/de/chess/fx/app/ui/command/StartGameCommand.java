package de.chess.fx.app.ui.command;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StartGameCommand implements ICommando{
    @Override
    public void execute() {
        LOGGER.log(Level.SEVERE, "Start Game Command...");
    }
}
