package com.restserver;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.json.AppPersistence;

/**
 * Spring boot service for users.
 * Controller Logic.
 */
@Service
public class UserService {

  private UserManager userManager;
  private AppPersistence appPersistence;

  /**
   * Constructor for the user service.

   * @param userManager user manager to operate with.
   */
  public UserService(UserManager userManager) {
    this.userManager = userManager;
    this.appPersistence = new AppPersistence();
    appPersistence.setSaveFile("restserver-users.json");
  }

  /**
   * Empty constructor for creating a new user manager.
   */
  public UserService() {
    this(createDefaultUserManager());
  }

  private static UserManager createDefaultUserManager() {
    return new UserManager();
  }

  public UserManager getUserManager() {
    return userManager;
  }

  public List<String> getAllUsernames() {
    return userManager.getUsers().stream().map(User::getUsername).collect(Collectors.toList());
  }

  /**
   * Adds a user to the user manager.

   * @param user user to add.
   */
  public void addUser(User user) {
    userManager.addUser(user);
  }

  /**
   * Sets the user manager and saves it.

   * @param userManager usermanager.
   */
  public void setUserManager(UserManager userManager) {
    this.userManager = userManager;
  }

  /**
   * Creates a new user in the user manager.

   * @param user user to create.
   * @return the same user.
   */
  public User createUser(User user) {
    return userManager.createNewUser(user.getUsername(), 
        user.getPassword(), user.getFullName(), user.getEmail());
  }

  /**
   * Retrieves a user based on its username.

   * @param username username to find.
   * @return user.
   */
  public User getUserByUsername(String username) throws NoSuchElementException {
    return userManager.getUsers().stream()
        .filter((u) -> u.getUsername().equals(username))
        .findAny()
        .orElseThrow(() -> new NoSuchElementException());
  }

  /**
   * Get all exercises for a given username.

   * @param username username to find.
   * @return list of all exercises.
   */
  public List<Exercise> getExercisesByUsername(String username) {
    User user = 
        userManager.getUsers().stream()
            .filter((u) -> u.getUsername().equals(username))
            .findAny()
            .orElseThrow(() -> new NoSuchElementException());
    return user.getExerciseDatabase();
  }

  /**
   * Adds an exercise to a given user's exercise database.

   * @param username user to find.
   * @param exercise exercise to add.
   * @return the exercise to add.
   */
  public Exercise addExerciseToUser(String username, Exercise exercise) {
    System.out.println("Users in manager: " + userManager.getUsers().size());
    userManager.getUsers().forEach(u -> System.out.println("User: " + u.getUsername()));
    System.out.println("Looking for: " + username);
    
    User user = 
        userManager.getUsers().stream()
            .filter((u) -> u.getUsername().equals(username))
            .findAny()
            .orElseThrow(() -> new NoSuchElementException());
    user.addExercise(exercise);
    System.out.println("##############################Added Exercise: " + exercise);
    return exercise;
  }

  /**
   * Saves the user manager.
   */
  public void autoSaveUserManager() {
    if (appPersistence != null) {
      try {
        appPersistence.saveUserManager(userManager);
        System.out.println("User Manager was saved!");
      } catch (IllegalArgumentException | IOException e) {
        System.out.println("Could not save user manager");
      }
    }
  }

  public AppPersistence getAppPersistence() {
    return appPersistence;
  }
}
