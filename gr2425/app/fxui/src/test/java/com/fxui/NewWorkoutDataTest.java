package com.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.core.UserManager;
import com.core.enums.Intensity;
import com.core.enums.Type;
import com.fxui.util.LoadPage;
import com.json.AppPersistence;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

/**
 * Test class for a new workout. Only Meta Data.
 */
public class NewWorkoutDataTest extends ApplicationTest {

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

    // Resetting the workout session list for each test
    UserManagerAccessProvider.getInstance().getUserManager().getUsers().stream()
        .filter(
          (u) -> u.getUsername().equals(UserManagerAccessProvider.getInstance().getUsername()))
        .findFirst().get()
        .setWorkoutList(new ArrayList<>());

    LoadPage.loadPage("newWorkoutData");
  }

  @Test
  void testEmptyFields() {
    clickOn("#finishedButton");

    // Should open an error display:

    DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);

    Assertions.assertThat(dialogPane).isNotNull();
  }

  @Test
  void testFilledFields() {

    clickOn("#newWorkoutName");
    write("New Test Workout");

    clickOn("STRENGTH");

    clickOn("HIGH");

    clickOn("#finishedButton");

    Assertions.assertThat(lookup("#newWorkoutPage")).isNotNull();

    clickOn("#workoutCloseButton");

    clickOn("#closeAndSaveButton");

    assertEquals("New Test Workout", 
        UserManagerAccessProvider.getInstance().getCurrentWorkoutSession().getName());

    assertEquals(Type.STRENGTH, 
        UserManagerAccessProvider.getInstance().getCurrentWorkoutSession().getType());
        
    assertEquals(Intensity.HIGH, 
        UserManagerAccessProvider.getInstance().getCurrentWorkoutSession().getIntensity());

  }

  @Test
  void testReturnbutton() {
    clickOn("#returnButton");

    Assertions.assertThat(lookup("#mainPage")).isNotNull();
  }

}
