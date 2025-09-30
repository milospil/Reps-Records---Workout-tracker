package com.fxui.util;

import com.fxui.RepsAndRecordsUi;
import com.fxui.RepsAndRecordsUiRemote;

/**
 * Class for reduction of code.
 */
public class LoadPage {

  /**
   * Sets the scene to a new FXML scene.

   * @param file the fxml document to load.
   */
  public static void loadPage(String file) {
    try {
      RepsAndRecordsUi.setRoot(file);
    } catch (Exception e) {
      try {
        RepsAndRecordsUiRemote.setRoot(file);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }
}
