package de.chess.fx.app.ui.handler;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CloseApplicationHandler implements EventHandler<ActionEvent> {
    private static final Logger LOGGER = Logger.getLogger(CloseApplicationHandler.class.getName());
    @Override
    public void handle(ActionEvent event) {
        LOGGER.log(Level.SEVERE, "Close Application Event");
        Platform.exit();
        System.exit(0);
    }
}
