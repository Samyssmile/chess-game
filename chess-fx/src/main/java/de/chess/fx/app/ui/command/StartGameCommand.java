package de.chess.fx.app.ui.command;

import de.chess.fx.app.ui.handler.CloseApplicationHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StartGameCommand implements ICommando{
    private static final Logger LOGGER = Logger.getLogger(StartGameCommand.class.getName());
    @Override
    public void execute() {
        LOGGER.log(Level.SEVERE, "Start Game Command...");
    }
}
