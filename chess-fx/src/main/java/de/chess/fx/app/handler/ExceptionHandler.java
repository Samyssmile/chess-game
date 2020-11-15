package de.chess.fx.app.handler;

import de.chess.fx.app.client.GameClient;
import de.chess.fx.app.ui.dialogs.DialogFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler {
    public static final Logger LOGGER = Logger.getGlobal();

    public static  void handle(Exception e){
        DialogFactory.instance().getErrorDialog("Error", "Unexpected Error in Application", e.getLocalizedMessage());
        LOGGER.log(Level.SEVERE, e.toString());
    }
}
