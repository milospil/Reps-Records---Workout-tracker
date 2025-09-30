package com.fxui;

import com.core.WorkoutSet;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * Controller for exercise sets page.
 */
public class ExerciseSetsController {

  @FXML
  private ListView<WorkoutSet> warmupListView;

  @FXML
  private ListView<WorkoutSet> worksetListView;

  @FXML
  private Label exerciseLabel;

  @FXML
  private Label clockLabel;

  private NewWorkoutController newWorkoutController;

  private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  private LocalTime currentTime = LocalTime.of(0, 0, 0);

  /**
   * Initializes the exercise sets inside an exercise.
   */
  @FXML
  public void initialize() {

    warmupListView.prefHeightProperty()
        .bind(Bindings.size(warmupListView.getItems()).multiply(60));

    worksetListView.prefHeightProperty()
        .bind(Bindings.size(worksetListView.getItems()).multiply(60));

    // DENNE KODEBLOKKEN BLE GENERERT VED HJELP AV CHATGPT

    warmupListView.setCellFactory(new Callback<ListView<WorkoutSet>, ListCell<WorkoutSet>>() {
      @Override
      public ListCell<WorkoutSet> call(ListView<WorkoutSet> listView) {
        return new ExerciseRepsListCell();
      }
    });

    // SLUTT PÅ GENERERT KODEBLOKK

    warmupListView.setMaxWidth(320);
    warmupListView.setPrefWidth(320);

    // DENNE KODEBLOKKEN BLE GENERERT VED HJELP AV CHATGPT

    worksetListView.setCellFactory(new Callback<ListView<WorkoutSet>, ListCell<WorkoutSet>>() {
      @Override
      public ListCell<WorkoutSet> call(ListView<WorkoutSet> listView) {
        return new ExerciseRepsListCell();
      }
    });

    // SLUTT PÅ GENERERT KODEBLOKK

    worksetListView.setMaxWidth(320);
    worksetListView.setPrefWidth(320);

    startClock();
  }

  /**
   * Completes the initialization process.
   */
  public void completeInitialization() {
    if (newWorkoutController != null) {
      worksetListView.setItems(getExerciseWorkoutSets());
      warmupListView.setItems(getExerciseWarmupSets());
    }
  }

  @FXML
  void handleAddWarmupSet() {
    newWorkoutController.getSelectedExercise().addWarmupSet(new WorkoutSet());
    warmupListView.setItems(getExerciseWarmupSets());
  }

  @FXML
  void handleAddWorkoutSet() {
    newWorkoutController.getSelectedExercise().addWorkoutSet(new WorkoutSet());
    worksetListView.setItems(getExerciseWorkoutSets());
  }

  @FXML
  void handleReturnButton() {

    Stage stage = (Stage) warmupListView.getScene().getWindow();
    stage.close();
  }

  /**
   * Sets the exercise label.

   * @param name exercise name.
   */
  public void setExerciseName(String name) {
    this.exerciseLabel.setText(name);
  }

  private void startClock() { // Denne metoden er lik som i [NewWorkoutController]
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      currentTime = currentTime.plusSeconds(1);
      clockLabel.setText(currentTime.format(timeFormatter));
    }));

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  public void setNewWorkoutController(NewWorkoutController newWorkoutController) {
    this.newWorkoutController = newWorkoutController;
  }

  private ObservableList<WorkoutSet> getExerciseWorkoutSets() {
    return FXCollections
      .observableArrayList(newWorkoutController.getSelectedExercise().getWorkoutSets());
  }

  private ObservableList<WorkoutSet> getExerciseWarmupSets() {
    return FXCollections
      .observableArrayList(newWorkoutController.getSelectedExercise().getWarmupSets());
  }
}
