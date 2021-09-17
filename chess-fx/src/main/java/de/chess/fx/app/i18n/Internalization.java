package de.chess.fx.app.i18n;

import de.chess.fx.app.ui.dialog.DialogMessageType;

import java.util.ResourceBundle;

public interface Internalization {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("language");

    public default String i18n(String key){
        return resourceBundle.getString(key);
    }
    public default String i18n(DialogMessageType key){

        return resourceBundle.getString(key.getValue());
    }
}
