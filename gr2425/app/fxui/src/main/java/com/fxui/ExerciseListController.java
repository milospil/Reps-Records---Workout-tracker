package com.fxui;

import com.core.Exercise;
import com.core.WorkoutSessionExercise;
import com.fxui.util.GetUser;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controller for exercise database view.
 */
public class ExerciseListController {

  @FXML
  private ListView<Exercise> exerciseListView;

  @FXML
  private Button addNewExerciseButton;

  @FXML
  private Hyperlink closeButton;

  private NewWorkoutController newWorkoutController;

  /**
   * Initializes the ListView of the exercise database a user has.
   */
  @FXML
  public void initialize() {

    // DENNE KODEBLOKKEN BLE GENERERT VED HJELP AV CHATGPT

    exerciseListView.setCellFactory(new Callback<ListView<Exercise>, ListCell<Exercise>>() {
      @Override
      public ListCell<Exercise> call(ListView<Exercise> listView) {
        return new ExerciseListCell<Exercise>(true);
      }
    });

    exerciseListView.setMaxWidth(320);
    exerciseListView.setPrefWidth(320);

    // SLUTT PÅ GENERERT KODEBLOKK

    exerciseListView.setItems(getExerciseDatabase());

    exerciseListView.setOnMouseClicked(event -> {
      Exercise selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();
      if (selectedExercise != null) {
        WorkoutSessionExercise exercise = new WorkoutSessionExercise(selectedExercise);
        newWorkoutController.addExerciseToWorkoutSession(exercise);
        closeWindow();
      }
    });
  }

  @FXML
  void handleCloseButton() {
    closeWindow();
  }

  @FXML
  void handleAddNewExercise() {

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("newExercise.fxml"));
      Parent root = loader.load();

      NewExerciseController controller = loader.getController();
      controller.setExerciseListController(this);

      Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.initModality(Modality.NONE);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setNewWorkoutController(NewWorkoutController newWorkoutController) {
    this.newWorkoutController = newWorkoutController;
  }

  private void closeWindow() {
    Stage stage = (Stage) exerciseListView.getScene().getWindow();
    stage.close();
  }

  /**
   * Method for loading all exercises.

   * @return exercise database
   */

  private ObservableList<Exercise> getExerciseDatabase() {
    return FXCollections.observableArrayList(GetUser.getUser().getExerciseDatabase());
  }

  public ListView<Exercise> getExerciseListView() {
    return exerciseListView;
  }
}
