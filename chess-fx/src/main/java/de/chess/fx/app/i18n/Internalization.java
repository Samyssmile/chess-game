package de.chess.fx.app.i18n;

import java.util.ResourceBundle;

public interface Internalization {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("language");

    public default String i18n(String key){
        return resourceBundle.getString(key);
    }
}
