package com.fxui;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.core.Exercise;
import com.core.UserManager;
import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import com.fxui.util.LoadPage;
import com.json.AppPersistence;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobotException;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

/**
 * Test class for a new workout session.
 */
public class NewWorkoutTest extends ApplicationTest {

  /**
   * Sets headless settings.
   */
  @BeforeAll
  public static void setupHeadless() {
    RepsAndRecordsUi.supportHeadless();
  }

  /**
   * Starts the test environment.
   */
  @Start
  public void start(final Stage stage) throws Exception {

    AppPersistence appPersistence = new AppPersistence();
    Path testFilePath = Paths.get("src/test/resources/com/fxui/test.json");
    appPersistence.setTestFile(testFilePath);

    UserManager userManager = appPersistence.loadUserManager();

    RepsAndRecordsUi.setUpTest(appPersistence, userManager);

    RepsAndRecordsUi mainApp = new RepsAndRecordsUi();
    mainApp.start(stage);

    UserManagerAccessProvider.getInstance().setUsername("Test");

    UserManagerAccessProvider.getInstance().setCurrentWorkoutSession(new WorkoutSession());

    LoadPage.loadPage("newWorkout");
  }

  @Test
  void testAddNewWorkout() {
    clickOn("#newExerciseButton");

    ListView<Exercise> exerciseDatabaseListView =
        lookup("#exerciseListPage").lookup("#exerciseListView").queryListView();

    int itemCount = exerciseDatabaseListView.getItems().size();
    assertNotEquals(0, itemCount);

    moveTo(exerciseDatabaseListView);
    Random random = new Random();
    int randomIndex = random.nextInt(itemCount);

    boolean found = false;
    String exerciseToFind = exerciseDatabaseListView.getItems().get(randomIndex).getExerciseName();
    int scrollAmount = 0;

    while (!found && scrollAmount < itemCount) {
      try {
        clickOn(exerciseToFind);
        found = true;
      } catch (FxRobotException e) {
        sleep(100);
        Node thumb = 
            lookup("#exerciseListView")
            .lookup(".scroll-bar:vertical").match(node -> node.isVisible())
            .lookup(".thumb").query();
        clickOn(thumb).drag(thumb).dropBy(0, 100);
        sleep(100);
      }
    }

    System.out.println(
        UserManagerAccessProvider.getInstance()
            .getCurrentWorkoutSession().getExerciseList().get(0).getExerciseName());

    Assertions.assertThat(
        lookup("#newWorkoutPage")
        .lookup("#exerciseListView").queryListView().getItems().size())
        .isEqualTo(1);

    ListView<WorkoutSessionExercise> exerciseListView =
        lookup("#newWorkoutPage").lookup("#exerciseListView").queryListView();

    clickOn(exerciseListView.getItems().get(0).getExerciseName());

    // Should open the workout set window

    Assertions.assertThat(listWindows().size()).isEqualTo(2);

    clickOn("#addWarmupSetButton");

    Assertions.assertThat(
        lookup("#exerciseSetPage")
        .lookup("#warmupListView").queryListView().getItems().size())
        .isEqualTo(1);

    clickOn("#addWorkoutSetButton");

    Assertions.assertThat(
        lookup("#exerciseSetPage")
        .lookup("#worksetListView").queryListView().getItems().size())
        .isEqualTo(1);

  }

}
