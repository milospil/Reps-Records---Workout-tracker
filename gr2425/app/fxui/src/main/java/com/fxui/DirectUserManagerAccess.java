package com.fxui;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.core.WorkoutSession;
import com.json.AppPersistence;
import java.io.IOException;

/**
 * Singleton Pattern that allow all classes in the folder to access information.
 */
public class DirectUserManagerAccess implements UserManagerAccess {

  private UserManager userManager;

  private AppPersistence appPersistence;

  private String username;

  private WorkoutSession currentWorkoutSession;

  public AppPersistence getAppPersistence() {
    return appPersistence;
  }

  public void setAppPersistence(AppPersistence appPersistence) {
    this.appPersistence = appPersistence;
  }

  public UserManager getUserManager() {
    return userManager;
  }

  public void setUserManager(UserManager userManager) {
    this.userManager = userManager;
  }

  /**
   * Creates a new user and saves the usermanager.
   */
  public User createUser(User user) {
    userManager.addUser(user);
    autoSaveUserManager();
    return user;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public WorkoutSession getCurrentWorkoutSession() {
    return currentWorkoutSession;
  }

  public void setCurrentWorkoutSession(WorkoutSession workoutSession) {
    this.currentWorkoutSession = workoutSession;
  }

  /**
   * Creates and saves Exercise for User.
   *
   * @param user of the user to create exercise for
   * @param exercise created for user
   */
  public void addExerciseToUser(User user, Exercise exercise) {
    user.addExercise(exercise);
  }

  /**
   * Adds a workout session to the given user.

   * @param user user to add session to.
   * @param workoutSession session to add.
   */
  public void addWorkoutSessionToUser(User user, WorkoutSession workoutSession) {
    user.addSession(workoutSession);
  }

  /**
   * Automatically save the UserManager to file.
   */
  public void autoSaveUserManager() {
    if (appPersistence != null) {
      try {
        appPersistence.saveUserManager(userManager);
      } catch (IllegalStateException | IOException e) {
        e.printStackTrace();
      }
    }
  }

}
