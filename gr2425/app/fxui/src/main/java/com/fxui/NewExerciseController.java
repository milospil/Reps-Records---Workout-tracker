package com.fxui;

import com.core.Exercise;
import com.core.User;
import com.core.enums.Force;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;
import com.fxui.util.GetUser;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Controller for adding a new exercise.
 */
public class NewExerciseController {

  @FXML
  private Label newExerciseLabel;

  @FXML
  private TextField newExerciseName;

  @FXML
  private ToggleGroup group1;

  @FXML
  private ToggleGroup group2;

  @FXML
  private ToggleGroup group3;

  private MainPageController mainPageController;

  private ExerciseListController exerciseListController;

  @FXML
  private void initialize() {
    setUpToggleGroups(group1);
    setUpToggleGroups(group2);
    setUpToggleGroups(group3);
  }

  private void setUpToggleGroups(ToggleGroup group) {
    group.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
      if (newVal == null) {
        oldVal.setSelected(true);
      }
    });
  }

  // FXML METHODS

  @FXML
  void handleCloseButton() {
    closeWindow();
  }

  @FXML
  void handleSaveButton() {
    List<String> exerciseInfo = getExerciseInformation();

    Exercise exercise =
        new Exercise(exerciseInfo.get(0), 
            Enum.valueOf(Purpose.class, exerciseInfo.get(1).toUpperCase()),
            Enum.valueOf(Mechanics.class, exerciseInfo.get(2).toUpperCase()),
            Enum.valueOf(Force.class, exerciseInfo.get(3).toUpperCase()));

    User user = GetUser.getUser();

    UserManagerAccess holder = UserManagerAccessProvider.getInstance();

    holder.addExerciseToUser(user, exercise);

    holder.autoSaveUserManager();

    if (mainPageController != null) {
      mainPageController.getExerciseDatabaseListView().setItems(getExerciseDatabase());
    }

    if (exerciseListController != null) {
      exerciseListController.getExerciseListView().setItems(getExerciseDatabase());
    }

    closeWindow();
  }

  // METHODS

  private void closeWindow() {
    Stage stage = (Stage) newExerciseLabel.getScene().getWindow();
    stage.close();
  }

  /**
   * Collects the user data from the GUI and converts to data.

   * @return exercise information.
   */
  public List<String> getExerciseInformation() {

    List<String> exerciseData = new ArrayList<>();

    ToggleButton buttonGroupOne = (ToggleButton) group1.getSelectedToggle();
    ToggleButton buttonGroupTwo = (ToggleButton) group2.getSelectedToggle();
    ToggleButton buttonGroupThree = (ToggleButton) group3.getSelectedToggle();

    if (newExerciseName != null && buttonGroupOne != null 
          && buttonGroupTwo != null && buttonGroupThree != null) {
      exerciseData.add(newExerciseName.getText());
      exerciseData.add(buttonGroupOne.getText());
      exerciseData.add(buttonGroupTwo.getText());
      exerciseData.add(buttonGroupThree.getText());
    } else {
      System.out.println("No button is selected");
    }

    return exerciseData;
  }

  /**
   * Method that converts a List to ObservableList. Needed for JavaFX ListView.

   * @return exercise database.
   */
  private ObservableList<Exercise> getExerciseDatabase() {
    return FXCollections.observableArrayList(GetUser.getUser().getExerciseDatabase());
  }

  public void setMainPageController(MainPageController mainPageController) {
    this.mainPageController = mainPageController;
  }

  public void setExerciseListController(ExerciseListController exerciseListController) {
    this.exerciseListController = exerciseListController;
  }
}
