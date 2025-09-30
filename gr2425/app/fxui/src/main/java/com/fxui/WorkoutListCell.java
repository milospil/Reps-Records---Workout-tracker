package com.fxui;

import com.core.WorkoutSession;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Class that defines a workout cell.
 */
public class WorkoutListCell extends ListCell<WorkoutSession> {

  private HBox root;
  private WorkoutComponentController controller;

  /**
   * Each list cell is a predefined fxml.

   * @param hideRemoveButton if true then hides the remove button
   */
  public WorkoutListCell(boolean hideRemoveButton) {

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("workoutListComponent.fxml"));
      root = loader.load();
      controller = loader.getController();

      if (hideRemoveButton) {
        controller.workoutListVersion();
      }

      controller.getRemoveButton().setOnAction(e -> {
        WorkoutSession workoutSession = getListView().getItems().get(getIndex());

        if (workoutSession != null) {
          try {
            FXMLLoader load = new FXMLLoader(getClass().getResource("removeOverlay.fxml"));
            Parent roo = load.load();

            RemoveOverlayController controller = load.getController();
            controller.setWorkoutSession(workoutSession);
            controller.setWorkoutSessionListView((ListView<WorkoutSession>) getListView());

            Stage stage = new Stage();
            stage.setScene(new Scene(roo));
            stage.show();

          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }

      });

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void updateItem(WorkoutSession workoutSession, boolean empty) {
    super.updateItem(workoutSession, empty);

    if (empty || workoutSession == null) {
      setText(null);
      setGraphic(null);
    } else {

      controller.setWorkoutNameLabel(workoutSession.getName());

      controller.setDateLabel(
            workoutSession.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString());

      controller.setHoursAndMinutesLabel(
            workoutSession.getDate().format(DateTimeFormatter.ofPattern("HH:mm")).toString());

      setGraphic(root);
    }
  }
}
