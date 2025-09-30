package com.fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for a single exercise component.
 */
public class ExerciseComponentController {

  @FXML
  private Label exerciseNameLabel;

  @FXML
  private Label targetMuscleLabel;

  @FXML
  private Button removeButton;

  @FXML
  private Button infoButton;

  @FXML
  private void handleInfoButtonClick() {

  }

  public Button getRemoveButton() {
    return removeButton;
  }

  /**
   * Sets the name label.

   * @param exercise exerciseName.
   */
  public void setExerciseNameLabel(String exercise) {
    exerciseNameLabel.setText(exercise);
  }

  /**
   * Sets the muscle label.

   * @param muscle muscle name.
   */
  public void setTargetMuscleLabel(String muscle) {
    targetMuscleLabel.setText(muscle);
  }

  /**
   * Makes the removeButton invisible if wanted.
   */
  public void exerciseListVersion() {
    removeButton.setVisible(false);
  }

}
