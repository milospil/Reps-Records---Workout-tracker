package com.fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller class for each component in a List View.
 */
public class WorkoutComponentController {

  @FXML
  private Label workoutNameLabel;

  @FXML
  private Label dateLabel;

  @FXML
  private Label hoursAndMinutesLabel;

  @FXML
  private Button removeButton;

  @FXML
  private Button infoButton;

  @FXML
  private void handleInfoButtonClick() {

  }

  // METODER

  public Button getRemoveButton() {
    return removeButton;
  }

  /**
   * Sets the workout name label.

   * @param workout name.
   */
  public void setWorkoutNameLabel(String workout) {
    workoutNameLabel.setText(workout);
  }

  /**
   * Sets the date label for a workout.

   * @param date date.
   */
  public void setDateLabel(String date) {
    dateLabel.setText(date);
  }

  /**
   * Sets the time spent workout out label.

   * @param hoursAndMinutes time spent working out.
   */
  public void setHoursAndMinutesLabel(String hoursAndMinutes) {
    hoursAndMinutesLabel.setText(hoursAndMinutes);
  }

  /**
   * Can be used to remove the visibility of the RemoveButton.
   */
  public void workoutListVersion() {
    removeButton.setVisible(false);
  }

  public Label getWorkoutNameLabel() {
    return workoutNameLabel;
  }

  public Label getDateLabel() {
    return dateLabel;
  }

  public Label getHoursAndMinutesLabel() {
    return hoursAndMinutesLabel;
  }


}
