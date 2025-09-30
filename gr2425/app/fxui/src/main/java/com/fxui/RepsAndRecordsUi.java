package com.fxui;

import com.core.UserManager;
import com.json.AppPersistence;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Main Application class.
 */
public class RepsAndRecordsUi extends Application {

  /**
   * Method for setting up headless tests.
   */
  public static void supportHeadless() {
    if (Boolean.getBoolean("headless")) {
      System.setProperty("testfx.robot", "glass");
      System.setProperty("testfx.headless", "true");
      System.setProperty("prism.order", "sw");
      System.setProperty("prism.text", "t2k");
      System.setProperty("java.awt.headless", "true");
    } else {
      System.out.println("No headless settings are set");
    }
  }

  private static Scene scene;

  private AppPersistence appPersistence;

  @Override
  public final void start(final Stage stage) throws IOException {

    UserManagerAccess holder;

    try {
      holder = UserManagerAccessProvider.getInstance();
    } catch (IllegalStateException e) {
      UserManagerAccessProvider.setInstance(new DirectUserManagerAccess());
      holder = UserManagerAccessProvider.getInstance();
    }

    if (holder.getUserManager() == null || holder.getAppPersistence() == null) {

      this.appPersistence = new AppPersistence();
      appPersistence.setSaveFile("users.json");

      holder.setAppPersistence(appPersistence);
      holder.setUserManager(getInitialUserManager());

    }

    /*
     * Can uncomment this if we want to add a shutdown hook save functionality
     * Runtime.getRuntime().addShutdownHook(new Thread(() -> {
     * DirectUserManagerAccess.autoSaveUserManager(); }));
     */

    initScene(stage);
    stage.show();
  }

  private void initScene(final Stage stage) throws IOException {
    scene = new Scene(loadFxml("login"), 320, 568);
    scene.getStylesheets().add(RepsAndRecordsUi.class.getResource("styles.css").toExternalForm());
    stage.setScene(scene);
  }


  /**
   * Method for changing between scenes.

   * @param fxml fxml document.
   * 
   * @throws IOException if fxml document is not found.
   */

  public static void setRoot(final String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  private static Parent loadFxml(final String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(RepsAndRecordsUi.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  /**
   * Method for setting up test environment.

   * @param appPersistence the app persistence.
   * 
   * @param userManager the provided user manager.
   */
  public static void setUpTest(final AppPersistence appPersistence, final UserManager userManager) {
    UserManagerAccessProvider.setInstance(new DirectUserManagerAccess());
    UserManagerAccess holder = UserManagerAccessProvider.getInstance();
    holder.setAppPersistence(appPersistence);
    holder.setUserManager(userManager);
  }

  private UserManager getInitialUserManager() {
    UserManager userManager = null;

    if (appPersistence != null) {
      try {
        userManager = appPersistence.loadUserManager();
      } catch (IOException | IllegalStateException e) {
        System.out.println("Couldn't load userManager from local file, creating new file. . .");
      }
    }

    if (userManager == null) {
      userManager = new UserManager();
    }

    return userManager;
  }

  /**
   * Launches the locally dependent app.

   * @param args args.
   */
  public static void main(final String[] args) {
    launch();
  }

}
