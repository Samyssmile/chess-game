package de.chess.fx.app.ui.command;

import java.util.logging.Logger;

public interface ICommando {
    public static final Logger LOGGER = Logger.getLogger(ICommando.class.getSimpleName());
    public void execute();
}
