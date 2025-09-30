package com.fxui;

import com.core.Exercise;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * A class that defines what a exercise in a list view should look like.
 */
public class ExerciseListCell<T extends Exercise> extends ListCell<T> {

  private HBox root;
  private ExerciseComponentController controller;

  /**
   * Initializes a single exercise cell in a list view.

   * @param hideRemoveButton hides the remove button if true.
   */
  public ExerciseListCell(boolean hideRemoveButton) {

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("exerciseListComponent.fxml"));
      root = loader.load();
      controller = loader.getController();

      if (hideRemoveButton) {
        controller.exerciseListVersion();
      }

      controller.getRemoveButton().setOnAction(e -> {
        Exercise exercise = getListView().getItems().get(getIndex());

        if (exercise != null) {
          try {
            FXMLLoader load = new FXMLLoader(getClass().getResource("removeOverlay.fxml"));
            Parent roo = load.load();

            RemoveOverlayController controller = load.getController();
            controller.setExercise(exercise);
            controller.setExerciseListView(getListView());

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
  protected void updateItem(T exercise, boolean empty) {
    super.updateItem(exercise, empty);

    if (empty || exercise == null) {
      setText(null);
      setGraphic(null);
    } else {
      controller.setExerciseNameLabel(exercise.getExerciseName());
      controller.setTargetMuscleLabel(exercise.getMuscle());
      setGraphic(root);
    }
  }

}
