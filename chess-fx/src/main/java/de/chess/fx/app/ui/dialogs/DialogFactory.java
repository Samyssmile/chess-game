package de.chess.fx.app.ui.dialogs;

import javafx.scene.control.Alert;

public class DialogFactory implements IDialogFactory {

    private static IDialogFactory instance = null;

    /**
     * C'Tor
     */
    private DialogFactory() {
    }

    public static IDialogFactory instance() {
        if (instance == null) {
            instance = new DialogFactory();
        }
        return instance;
    }

    @Override
    public Alert getDialog(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
        return alert;
    }

    @Override
    public Alert getErrorDialog(String title, String header, String message) {
        return getDialog(Alert.AlertType.ERROR, title, header, message);
    }

    @Override
    public Alert getInfoDialog(String title, String header, String message) {
        return getDialog(Alert.AlertType.INFORMATION, title, header, message);
    }

    @Override
    public Alert getWarnDialog(String title, String header, String message) {
        return getDialog(Alert.AlertType.WARNING, title, header, message);
    }
}
