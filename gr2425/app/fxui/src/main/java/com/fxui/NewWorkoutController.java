package com.fxui;

import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * Controller for a new workout session.
 */
public class NewWorkoutController {

  @FXML
  private ListView<WorkoutSessionExercise> exerciseListView;

  @FXML
  private Label clockLabel;

  @FXML
  private Button newWorkoutButton;

  @FXML
  private Hyperlink workoutCloseButton;

  private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  private LocalTime currentTimeTotal = LocalTime.of(0, 0, 0);

  private WorkoutSessionExercise selectedExercise;


  @FXML
  private void initialize() {



    // DENNE KODEBLOKKEN BLE GENERERT VED HJELP AV CHATGPT

    exerciseListView.setCellFactory(
        new Callback<ListView<WorkoutSessionExercise>, 
        ListCell<WorkoutSessionExercise>>() {

        @Override
        public ListCell<WorkoutSessionExercise> call(ListView<WorkoutSessionExercise> listView) {
          return new ExerciseListCell<WorkoutSessionExercise>(false);
        }
      });

    exerciseListView.setPlaceholder(new Label("Add a workout!"));

    // SLUTT PÅ GENERERT KODEBLOKK


    exerciseListView.setItems(getWorkoutSessionExercises());

    startClock();

    exerciseListView.setOnMouseClicked(event -> {
      selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();
      if (selectedExercise != null) {

        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("exerciseSets.fxml"));
          Parent root = loader.load();
          
          ExerciseSetsController exerciseSetsController = loader.getController();
          exerciseSetsController.setNewWorkoutController(this);
          exerciseSetsController.setExerciseName(selectedExercise.getExerciseName());
          exerciseSetsController.completeInitialization();
          
          Stage stage = new Stage();
          stage.setScene(new Scene(root));
          stage.initModality(Modality.NONE);
          stage.show();

        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

  }

  @FXML
  private void handleAddExercise() throws IOException {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("exerciseList.fxml"));
    Parent root = loader.load();

    ExerciseListController exerciseListController = loader.getController();
    exerciseListController.setNewWorkoutController(this);

    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();

  }

  @FXML
  void handleHelpClick() {

  }

  @FXML
  void handleCloseButton() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("closeOverlay.fxml"));
      Parent root = loader.load();

      CloseOverlayController controller = loader.getController();
      controller.setSessionDurationTime(this.currentTimeTotal);


      Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Adds a exercise to the current workout session.

   * @param exercise a workout session exercise.
   */
  public void addExerciseToWorkoutSession(WorkoutSessionExercise exercise) {
    WorkoutSession session = UserManagerAccessProvider.getInstance().getCurrentWorkoutSession();
    session.addExerciseToWorkout(exercise);

    refreshListView();
  }

  public WorkoutSessionExercise getSelectedExercise() {
    return selectedExercise;
  }

  /**
   * Refreshes the list view so the user sees updated logic.
   */
  public void refreshListView() {
    exerciseListView.setItems(getWorkoutSessionExercises());
  }

  /**
   * Starts a workout session clock. Tracks the amount of time a user works out in a session.
   */
  private void startClock() { // DENNE ER GENERERT VED HJELP AV CHATGPT
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      currentTimeTotal = currentTimeTotal.plusSeconds(1);
      clockLabel.setText(currentTimeTotal.format(timeFormatter));
    }));

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  /**
   * Converts List to ObservableList.

   * @return a list of workout session exercises, that javafx understands.
   */
  private ObservableList<WorkoutSessionExercise> getWorkoutSessionExercises() {
    return FXCollections
        .observableArrayList(
          UserManagerAccessProvider.getInstance().getCurrentWorkoutSession().getExerciseList());
  }
}
