package com.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Tests the workout component.
 */
public class WorkoutComponentControllerTest extends ApplicationTest {

  private WorkoutComponentController controller;

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = 
        new FXMLLoader(getClass().getResource("/com/fxui/workoutListComponent.fxml"));

    loader.setControllerFactory(c -> {
      controller = new WorkoutComponentController();
      return controller;
    });
    stage.setScene(new Scene(loader.load()));
    stage.show();
  }

  @Test
  public void testSetWorkoutNameLabel() {
    Platform.runLater(() -> {
      controller.setWorkoutNameLabel("Workout 1");
      Label workoutNameLabel = controller.getWorkoutNameLabel();
      assertEquals("Workout 1", workoutNameLabel.getText());
    });
  }

  @Test
  public void testSetDateLabel() {
    Platform.runLater(() -> {
      controller.setDateLabel("2024-11-05");
      Label dateLabel = controller.getDateLabel();
      assertEquals("2024-11-05", dateLabel.getText());
    });
  }

  @Test
  public void testSetHoursAndMinutesLabel() {
    Platform.runLater(() -> {
      controller.setHoursAndMinutesLabel("1h 30m");
      Label hoursAndMinutesLabel = controller.getHoursAndMinutesLabel();
      assertEquals("1h 30m", hoursAndMinutesLabel.getText());
    });
  }
}