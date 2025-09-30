package com.fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for each workout set instance.
 */
public class RepsAndKilosComponentController {

  @FXML
  private Button removeButton;

  @FXML
  private TextField kilosTextField;

  @FXML
  private TextField repsTextField;

  @FXML
  private Label setNumber;

  @FXML
  void handleRemoveButton() {

  }

  public Button getRemoveButton() {
    return removeButton;
  }

  /**
   * Sets the kilos label.

   * @param kilos amount of kilos.
   */
  public void setKilosLabel(String kilos) {
    kilosTextField.setText(kilos);
  }

  /**
   * Sets the reps label.

   * @param reps amount of reps.
   */
  public void setRepsLabel(String reps) {
    repsTextField.setText(reps);
  }

  public TextField getKilosTextField() {
    return kilosTextField;
  }

  public TextField getRepsTextField() {
    return repsTextField;
  }

  /**
   * Sets the set-number.

   * @param number set-number.
   */
  public void setSetNumber(String number) {
    setNumber.setText(number);
  }
}
