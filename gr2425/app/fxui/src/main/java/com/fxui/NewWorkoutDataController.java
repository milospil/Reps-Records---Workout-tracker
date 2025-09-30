package com.fxui;

import com.core.WorkoutSession;
import com.core.enums.Intensity;
import com.core.enums.Type;
import com.fxui.util.DisplayErrorMessage;
import com.fxui.util.LoadPage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/**
 * Controller class for creating a new workout. Handles the meta data.
 */
public class NewWorkoutDataController {

  @FXML
  private ToggleGroup group1;

  @FXML
  private ToggleGroup group2;

  @FXML
  private TextField newWorkoutName;

  @FXML
  private Button finishButton;

  @FXML
  private Hyperlink returnButton;

  @FXML
  void handleFinishedButton() {

    List<String> workoutMetaData = getWorkoutInformation();

    if (workoutMetaData != null) {
      WorkoutSession newWorkoutSession =
          new WorkoutSession(
            workoutMetaData.get(0), 
            Enum.valueOf(Type.class, 
            workoutMetaData.get(1)),
            Enum.valueOf(Intensity.class, workoutMetaData.get(2)), LocalDateTime.now());

      UserManagerAccessProvider.getInstance().setCurrentWorkoutSession(newWorkoutSession);

      LoadPage.loadPage("newWorkout");
    } else {
      DisplayErrorMessage
        .showError("Fill in all the data", "Empty Field Error", "One or more fields are empty");
    }
  }

  @FXML
  void handleReturnButton() {
    LoadPage.loadPage("mainPage");
  }

  /**
   * Retrieves the information from the GUI and converts it to data.

   * @return workout data.
   */
  public List<String> getWorkoutInformation() {

    List<String> exerciseData = new ArrayList<>();

    ToggleButton buttonGroupOne = (ToggleButton) group1.getSelectedToggle();
    ToggleButton buttonGroupTwo = (ToggleButton) group2.getSelectedToggle();

    if (!newWorkoutName.getText().isEmpty() && buttonGroupOne != null && buttonGroupTwo != null) {
      exerciseData.add(newWorkoutName.getText());
      exerciseData.add(buttonGroupOne.getText());
      exerciseData.add(buttonGroupTwo.getText());
    } else {
      exerciseData = null;
    }

    return exerciseData;
  }
}
