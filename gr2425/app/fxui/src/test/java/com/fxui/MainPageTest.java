package com.fxui;

import com.core.UserManager;
import com.fxui.util.LoadPage;
import com.json.AppPersistence;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

/**
 * Test class for the main page.
 */
public class MainPageTest extends ApplicationTest {

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

    // Resetting the exercise database for the user between tests
    UserManagerAccessProvider.getInstance().getUserManager().getUsers().stream()
        .filter(
            (u) -> u.getUsername().equals(UserManagerAccessProvider.getInstance().getUsername()))
        .findFirst().get()
        .setExercises(
          UserManagerAccessProvider.getInstance().getUserManager().getExerciseDatabase());

    LoadPage.loadPage("mainPage");
  }

  @Test
  void testClickExercises() {
    clickOn("#exerciseTab");

    lookup("#exercisesAnchorPane").queryAs(AnchorPane.class);

    ListView<?> listView = lookup("#exerciseDatabaseListView").queryAs(ListView.class);

    // Check to see if the user has its exercise database:

    Assertions.assertThat(listView).isNotNull();
    Assertions.assertThat(listView.getItems()).isNotEmpty();
  }

  @Test
  void handleAddNewExercise() {

    clickOn("#exerciseTab");

    int initialWindowCount = listWindows().size();

    clickOn("#addNewExercise");

    // Clicking that button should add an overlay window:

    Assertions.assertThat(listWindows()).hasSize(initialWindowCount + 1);

    clickOn("#newExerciseName");
    write("Test Exercise");

    clickOn("Arms");

    clickOn("Compound");

    clickOn("Pull");

    clickOn("#saveButton");
  }

  @Test
  void testClickNewExercise() {

    clickOn("#newWorkoutButton");

    // This should open a new page with id: newWorkoutDataPage:

    Assertions.assertThat(lookup("#newWorkoutDataPage")).isNotNull();
  }

  @Test
  void testHandleRepeatWorkoutButton() {
    clickOn("#repeatWorkoutButton");
    
    // Verify that workoutList page is loaded
    Assertions.assertThat(lookup("#workoutListPage")).isNotNull();
  }
}
