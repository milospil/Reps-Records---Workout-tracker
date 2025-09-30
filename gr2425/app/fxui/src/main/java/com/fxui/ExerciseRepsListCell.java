package com.fxui;

import com.core.WorkoutSet;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

/**
 * Class that defines what an exercise set should look like.
 */
public class ExerciseRepsListCell extends ListCell<WorkoutSet> {

  private HBox root;
  private RepsAndKilosComponentController controller;

  /**
   * Constructor initializes each workout set in an exercise's listView.
   */
  public ExerciseRepsListCell() {

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("repsAndKilosComponent.fxml"));
      root = loader.load();
      controller = loader.getController();

      controller.getRemoveButton().setOnAction(e -> {
        getListView().getItems().remove(getItem());
      });

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void updateItem(WorkoutSet workoutSet, boolean empty) {
    super.updateItem(workoutSet, empty);

    if (empty || workoutSet == null) {
      setText(null);
      setGraphic(null);
    } else {
      controller.setKilosLabel(String.valueOf(workoutSet.getKilos()));
      controller.setRepsLabel(String.valueOf(workoutSet.getReps()));
      controller.setSetNumber(String.valueOf(workoutSet.getSetNumber()));

      controller.getKilosTextField()
          .textProperty().addListener((observable, oldValue, newValue) -> {
            try {
              workoutSet.setKilosLifted(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
              workoutSet.setKilosLifted(0);
            }
          });

      controller.getRepsTextField().textProperty().addListener((observable, oldValue, newValue) -> {
        try {
          workoutSet.setReps(Integer.parseInt(newValue));
        } catch (NumberFormatException e) {
          workoutSet.setReps(0);
        }
      });

      setGraphic(root);
    }
  }

}
