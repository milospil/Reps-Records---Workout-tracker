package com.fxui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Class that displays an error message.
 */
public class DisplayErrorMessage {

  /**
   * Opens an Alert pane with a given title, header and message.

   * @param errorMessage the error message displayed.
   * @param title title of the error.
   * @param header header of the error.
   */
  public static void showError(String errorMessage, String title, String header) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(errorMessage);
    alert.getDialogPane().setMinSize(200, 200);
    alert.getDialogPane().setMaxSize(250, 400);
    alert.showAndWait();
  }
}
