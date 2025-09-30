package com.fxui;

import com.core.WorkoutSession;
import com.fxui.util.GetUser;
import com.fxui.util.LoadPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Controller for the workout list view.
 */
public class WorkoutListController {
  /**
   * The ListView that displays the list of saved workout sessions.
   */
  @FXML
  private ListView<WorkoutSession> workoutListView;

  /**
   * The close button to return to the main page.
   */
  @FXML
  private Hyperlink closeButton;

  /**
   * Initializes the WorkoutListController. Sets up the list view and handles clicking events in the
   * list view.
   * 
   */
  @FXML
  public void initialize() {
    // DENNE KODEBLOKKEN BLE GENERERT VED HJELP AV CHATGPT

    workoutListView.setCellFactory(
          new Callback<ListView<WorkoutSession>, ListCell<WorkoutSession>>() {

          @Override
          public ListCell<WorkoutSession> call(ListView<WorkoutSession> listView) {
            return new WorkoutListCell(false);
          }

        });

    workoutListView.setMaxWidth(320);
    workoutListView.setPrefWidth(320);

    // SLUTT PÅ GENERERT KODEBLOKK

    workoutListView.setPlaceholder(new Label("No workouts saved yet"));

    workoutListView.setItems(getSessions());

    workoutListView.setOnMouseClicked(event -> {
      WorkoutSession selectedWorkout = workoutListView.getSelectionModel().getSelectedItem();

      if (selectedWorkout != null) {
        WorkoutSession newSession = new WorkoutSession(selectedWorkout);
        UserManagerAccessProvider.getInstance().setCurrentWorkoutSession(newSession);
        LoadPage.loadPage("newWorkout");
      }
    });
  }


  /**
   * Handles the close button click, returns to the main page.
   */
  @FXML
  void handleCloseButton() {
    LoadPage.loadPage("mainPage");
  }

  /**
   * Method that converts a List to ObservableList.

   * @return an observable list containing the user's workout sessions.
   */
  private ObservableList<WorkoutSession> getSessions() {
    return FXCollections.observableArrayList(GetUser.getUser().getSessions());
  }
}

