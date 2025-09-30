package com.fxui;

import com.core.User;
import com.core.UserManager;
import com.fxui.util.LoadPage;
import com.json.AppPersistence;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.EmptyNodeQueryException;

/**
 * Test class for Login logic.
 */
public class LoginTest extends ApplicationTest {

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

    Optional<User> user = 
        UserManagerAccessProvider.getInstance().getUserManager().getUsers().stream()
        .filter(u -> u.getUsername().equals("tester")).findFirst();

    if (user.isPresent()) {
      UserManagerAccessProvider.getInstance().getUserManager().removeUser(user.get().getUsername());
    }

    LoadPage.loadPage("login");
  }

  @Test
  void testWrongCredentials() {

    clickOn("#usernameTextField").write("Wrong").clickOn("#passwordField").write("Wrong123!");

    // No such user should be in the database, so login button should display an
    // error message.

    int initialWindowCount = listWindows().size();

    clickOn("#loginButton");

    Assertions.assertThat(listWindows()).hasSize(initialWindowCount + 1);

    DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);

    Assertions.assertThat(dialogPane).isNotNull();
    Assertions.assertThat(dialogPane.getContentText()).contains("Enter your username and password");

    clickOn("OK");

  }

  @Test
  void handleCorrectCredentials() {
    clickOn("#usernameTextField").write("Test");

    Assertions.assertThat(
      lookup("#usernameTextField").queryAs(TextField.class).getText()).isEqualTo("Test");

    clickOn("#passwordField").write("Test123!");

    Assertions.assertThat(
      lookup("#passwordField").queryAs(PasswordField.class).getText()).isEqualTo("Test123!");

    clickOn("#loginButton");

    boolean isValid = 
        UserManagerAccessProvider.getInstance().getUserManager().validateUser("Test", "Test123!");

    System.out.println("ValidateUser result: " + isValid);
    System.out.println(UserManagerAccessProvider.getInstance().getUserManager());

    Assertions.assertThat(lookup("#mainPage")).isNotNull();

    try {
      DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
      Assertions.assertThat(dialogPane).isNull();
    } catch (EmptyNodeQueryException e) {
      System.out.println("DialogPane not found, as expected.");
    }
  }

  @Test
  void testEmptyFields() {
    clickOn("#loginButton");

    DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);

    Assertions.assertThat(dialogPane).isNotNull();
  }

  @Test
  void testHandleNewUser() throws InterruptedException {

    clickOn("#newUserButtonHyperlink");

    Assertions.assertThat(lookup("#newUser")).isNotNull();

    clickOn("#fullNameField").write("Test Testing").clickOn("#eMailField").write("Test@Test.com")
        .clickOn("#userNameField").write("tester").clickOn("#passwordField").write("Test123!");

    Node thumb =
        lookup("#newUserPage")
        .lookup(".scroll-bar:vertical")
        .match(node -> node.isVisible())
        .lookup(".thumb").query();
        
    clickOn(thumb).drag(thumb).dropBy(0, 200);
    sleep(100).clickOn("#confirmPasswordField").write("Test123!");

    clickOn("#newUserTerms").clickOn("#newUserButton");
  }

}
