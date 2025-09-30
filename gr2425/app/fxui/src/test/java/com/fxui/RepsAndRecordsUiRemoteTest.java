package com.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.core.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.AppPersistence;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Remote App testing.
 * RepsAndRecordsUiRemoteTest
 */
public class RepsAndRecordsUiRemoteTest extends ApplicationTest {

  private RepsAndRecordsUiRemote app;
  private Stage primaryStage;
  private RemoteUserManagerAccess mockRemoteAccess;
  private AppPersistence mockAppPersistence;

  @Override
  public void init() throws Exception {
    // Set up mocks before the application starts
    mockAppPersistence = Mockito.mock(AppPersistence.class);
    UserManager mockUserManager = new UserManager();

    // Create a special test version of RemoteUserManagerAccess
    mockRemoteAccess = new RemoteUserManagerAccess(mockAppPersistence) {
      @Override
      public UserManager getUserManager() {
        return mockUserManager;
      }

      @Override
      public void setUserManager(UserManager userManager) {
        // Do nothing, prevents HTTP calls
      }
    };

    UserManagerAccessProvider.setInstance(mockRemoteAccess);

    Mockito.when(mockAppPersistence.getObjectMapper()).thenReturn(new ObjectMapper());
    Mockito.when(mockAppPersistence.loadUserManager()).thenReturn(mockUserManager);
  }

  @Override
  public void start(Stage stage) throws Exception {
    this.primaryStage = stage;
    app = new RepsAndRecordsUiRemote();
    app.start(stage);
  }

  @Test
  public void testStartInitializesScene() {
    // Verify the scene is set
    Scene scene = primaryStage.getScene();
    assertNotNull(scene, "Scene should not be null");

    assertEquals(320, scene.getWidth(), "Scene width should be 320");
    assertEquals(568, scene.getHeight(), "Scene height should be 568");

    assertNotNull(scene.getStylesheets(), "Stylesheets should not be null");
    assertEquals(1, scene.getStylesheets().size(), "There should be one stylesheet");
    assertTrue(scene.getStylesheets()
        .get(0).contains("styles.css"), "Stylesheet should be 'styles.css'");
  }

  /**
   * Clean up.
   */
  @AfterEach
  public void cleanup() {
    // Clean up system properties after each test
    System.clearProperty("headless");
    System.clearProperty("testfx.robot");
    System.clearProperty("testfx.headless");
    System.clearProperty("prism.order");
    System.clearProperty("prism.text");
    System.clearProperty("java.awt.headless");
  }

  @Test
  public void testSupportHeadless() {
    // Test when headless is true
    System.setProperty("headless", "true");
    RepsAndRecordsUiRemote.supportHeadless();

    assertEquals("glass", System.getProperty("testfx.robot"),
        "TestFX robot should be set to glass when headless is true");
    assertEquals("true", System.getProperty("testfx.headless"),
        "TestFX headless should be true when headless is true");
    assertEquals("sw", System.getProperty("prism.order"),
        "Prism order should be sw when headless is true");
    assertEquals("t2k", System.getProperty("prism.text"),
        "Prism text should be t2k when headless is true");
    assertEquals("true", System.getProperty("java.awt.headless"),
        "Java AWT headless should be true when headless is true");

    cleanup();

    // Test when headless is false
    System.setProperty("headless", "false");
    RepsAndRecordsUiRemote.supportHeadless();

    assertNull(System.getProperty("testfx.robot"),
        "TestFX robot should not be set when headless is false");
    assertNull(System.getProperty("testfx.headless"),
        "TestFX headless should not be set when headless is false");
    assertNull(System.getProperty("prism.order"),
        "Prism order should not be set when headless is false");
    assertNull(System.getProperty("prism.text"),
        "Prism text should not be set when headless is false");
    assertNull(System.getProperty("java.awt.headless"),
        "Java AWT headless should not be set when headless is false");
  }
}