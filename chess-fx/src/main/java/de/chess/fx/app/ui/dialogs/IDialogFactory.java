package de.chess.fx.app.ui.dialogs;

import javafx.scene.control.Alert;

public interface IDialogFactory {

    public Alert getDialog(Alert.AlertType alertType, String title, String header, String message);
    public Alert getErrorDialog( String title, String header, String message);
    public Alert getInfoDialog( String title, String header, String message);
    public Alert getWarnDialog( String title, String header, String message);

}
