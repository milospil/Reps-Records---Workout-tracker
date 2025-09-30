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
public class RepsAndRecordsUiRemote extends Application {

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
  public void start(Stage stage) throws IOException {

    System.out.println("Loading remote-version");

    UserManagerAccess holder;

    try {
      holder = UserManagerAccessProvider.getInstance();
    } catch (IllegalStateException e) {
      UserManagerAccessProvider.setInstance(new RemoteUserManagerAccess(new AppPersistence()));
      holder = UserManagerAccessProvider.getInstance();
    }

    holder.setUserManager(getInitialUserManager());

    // Can uncomment this if we want to add a shutdown hook save functionality

    // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    // DirectUserManagerAccess.autoSaveUserManager();
    // }));

    initScene(stage);
    stage.show();
  }

  private void initScene(Stage stage) throws IOException {
    scene = new Scene(loadFxml("login"), 320, 568);
    scene.getStylesheets()
        .add(RepsAndRecordsUiRemote.class.getResource("styles.css").toExternalForm());
    stage.setScene(scene);
  }

  /**
   * Used for switching between scenes.

   * @param fxml fxml file.
   * @throws IOException if fxml file is not found.
   */
  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  private static Parent loadFxml(String fxml) throws IOException {
    FXMLLoader fxmlLoader = 
        new FXMLLoader(RepsAndRecordsUiRemote.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  /**
   * Method for setting up test environment.

   * @param appPersistence the app persistence.
   * @param userManager the provided user manager.
   */
  public static void setUpTest(AppPersistence appPersistence, UserManager userManager) {
    UserManagerAccessProvider.setInstance(new RemoteUserManagerAccess(appPersistence));
    UserManagerAccess holder = UserManagerAccessProvider.getInstance();
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
      try {
        userManager = UserManagerAccessProvider.getInstance().getUserManager();
        System.out.println("Loaded user manager from server.");
      } catch (RuntimeException e) {
        e.printStackTrace();
        System.out.println("Could not load the user manager from the server.");
      }
    }

    // If data was not loaded, create a new user manager.

    if (userManager == null) {
      userManager = new UserManager();
    }

    return userManager;
  }

  /**
   * Launches the remoteapp version.

   * @param args args.
   */
  public static void main(String[] args) {
    launch();
  }

}
