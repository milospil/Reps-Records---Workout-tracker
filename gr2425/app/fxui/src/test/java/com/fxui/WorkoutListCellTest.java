package com.fxui;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.core.WorkoutSession;
import com.core.enums.Intensity;
import com.core.enums.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test class for workout cells.
 */
public class WorkoutListCellTest {

  /**
   * Sets up test environment.
   */
  @BeforeAll
  public static void initToolkit() {
    // Initialize JavaFX toolkit
    new JFXPanel();
  }

  @Test
  public void testUpdateItem_withValidWorkoutSession() throws InterruptedException {
    WorkoutComponentController controller = new WorkoutComponentController();

    // Create JavaFX Labels directly
    Label nameLabel = new Label();
    Label dateLabel = new Label();
    Label timeLabel = new Label();
    // Button removeButton = new Button();

    // Use reflection to set these labels on the controller
    try {
      java.lang.reflect.Field nameLabelField = 
          WorkoutComponentController.class.getDeclaredField("workoutNameLabel");
      nameLabelField.setAccessible(true);
      nameLabelField.set(controller, nameLabel);

      java.lang.reflect.Field dateLabelField = 
          WorkoutComponentController.class.getDeclaredField("dateLabel");
      dateLabelField.setAccessible(true);
      dateLabelField.set(controller, dateLabel);

      java.lang.reflect.Field timeLabelField = WorkoutComponentController.class
          .getDeclaredField("hoursAndMinutesLabel");
      timeLabelField.setAccessible(true);
      timeLabelField.set(controller, timeLabel);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to set labels via reflection: " + e.getMessage());
    }

    WorkoutListCell cell = new WorkoutListCell(false);

    // Inject the controller into the cell using reflection
    try {
      java.lang.reflect.Field controllerField = 
          WorkoutListCell.class.getDeclaredField("controller");
      controllerField.setAccessible(true);
      controllerField.set(cell, controller);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to set controller via reflection: " + e.getMessage());
    }

    WorkoutSession session = 
        new WorkoutSession("Morning Workout", Type.CARDIO, Intensity.HIGH, LocalDateTime.now());

    // Use CountDownLatch to wait for JavaFX updates
    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      cell.updateItem(session, false);
      latch.countDown();
    });
    latch.await(1, TimeUnit.SECONDS);

    // Verify the expected values
    assertEquals("Morning Workout", nameLabel.getText());

    assertEquals(session.getDate()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), dateLabel.getText());

    assertEquals(session.getDate()
        .format(DateTimeFormatter.ofPattern("HH:mm")), timeLabel.getText());

  }

}
