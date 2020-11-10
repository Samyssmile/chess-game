package de.chess.fx.app.ui.host;

import java.util.ResourceBundle;

public interface Internalization {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("language");

    public default String i18n(String key){
        return resourceBundle.getString(key);
    }
}
