package com.fxui;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.core.WorkoutSession;
import com.json.AppPersistence;

/**
 * Interface for data access across controllers.
 */
public interface UserManagerAccess {

  /**
   * Get method for user manager object.

   * @return usermanager.
   */
  public UserManager getUserManager();

  /**
   * Sets the user manager object.

   * @param userManager usermanager to set.
   */
  public void setUserManager(UserManager userManager);

  /**
   * Returns the current username.

   * @return username.
   */
  public String getUsername();

  /**
   * Sets the current username in focus.

   * @param username username to focus.
   */
  public void setUsername(String username);

  /**
   * Gets the current workout session for a given user.

   * @return current workout session.
   */
  public WorkoutSession getCurrentWorkoutSession();

  /**
   * Sets the current workout session for a given user.

   * @param workoutSession workout session to set.
   */
  public void setCurrentWorkoutSession(WorkoutSession workoutSession);

  /**
   * Automatically saves the user manager to file.
   */
  public void autoSaveUserManager();

  /**
   * Returns the persistence logic.

   * @return app persistence.
   */
  public AppPersistence getAppPersistence();

  /**
   * Creates a new user and saves it to the user manager.

   * @param user user to add to user manager.
   * @return the same user.
   */
  public User createUser(User user);

  /**
   * Sets the persistence logic.

   * @param appPersistence persistence.
   */
  public void setAppPersistence(AppPersistence appPersistence);

  /**
   * Adds a workout session to the given user.

   * @param user user to add session to.
   * @param workoutSession session to add.
   */
  public void addWorkoutSessionToUser(User user, WorkoutSession workoutSession);

  /**
   * Creates and saves Exercise for User.
   *
   * @param user of the user to create exercise for
   * @param exercise created for user
   */
  public void addExerciseToUser(User user, Exercise exercise);
}
