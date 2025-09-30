package com.fxui;

import com.core.Exercise;
import com.core.User;
import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import com.fxui.util.GetUser;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * Controller for a remove overlay.
 * Used when attempting to remove exercises or workout sessions.
 */
public class RemoveOverlayController {

  @FXML
  private Button closeButton;

  private WorkoutSession workoutSession;

  private Exercise exercise;

  private ListView<Exercise> exerciseListView;

  private ListView<WorkoutSession> workoutSessionListView;

  @FXML
  void handleAcceptButton() {
    if (exercise != null && exerciseListView != null) {
      String name = exercise.getExerciseName();
      User user = GetUser.getUser();

      if (exercise.getClass().equals(WorkoutSessionExercise.class)) {

        WorkoutSession currentSession = 
            UserManagerAccessProvider.getInstance().getCurrentWorkoutSession();

        List<WorkoutSessionExercise> newWorkoutExerciseList = 
            currentSession.getExerciseList().stream()
            .filter(event -> !event.getExerciseName().equals(name)).collect(Collectors.toList());

        currentSession.setExerciseList(newWorkoutExerciseList);

      } else {

        List<Exercise> newExerciseList = user.getExerciseDatabase().stream()
            .filter(event -> !event.getExerciseName().equals(name)).collect(Collectors.toList());
        user.setExercises(newExerciseList);
      }

      exerciseListView.getItems().remove(exercise);
    }

    if (workoutSession != null && workoutSessionListView != null) {
      User user = GetUser.getUser();
      user.removeWorkout(workoutSession);

      workoutSessionListView.getItems().remove(workoutSession);
    }


    UserManagerAccessProvider.getInstance().autoSaveUserManager();
    closeWindow();
  }

  @FXML
  void handleCloseButton() {
    closeWindow();
  }

  public void setExercise(Exercise exercise) {
    this.exercise = exercise;
  }

  public void setWorkoutSession(WorkoutSession workoutSession) {
    this.workoutSession = workoutSession;
  }

  @SuppressWarnings("unchecked")
  public void setExerciseListView(ListView<? extends Exercise> exerciseListView) {
    this.exerciseListView = (ListView<Exercise>) exerciseListView;
  }

  public void setWorkoutSessionListView(ListView<WorkoutSession> workoutSessionListView) {
    this.workoutSessionListView = workoutSessionListView;
  }

  private void closeWindow() {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  public void setCloseButton(Button closeButton) {
    this.closeButton = closeButton;
  }

  public Button getCloseButton() {
    return closeButton;
  }
}
