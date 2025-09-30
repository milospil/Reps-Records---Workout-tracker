package com.core;

import com.core.comparators.UserComparator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.internal.AppModule;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Class that manages all users.
 */
public class UserManager {

  private List<User> userList = new ArrayList<>();
  private List<Exercise> exerciseDatabase;

  /**
   * Empty constructor instantiates a new usermanager.
   */
  public UserManager() {
    loadExercises();
  }

  /**
   * Defensive copy constructor.

   * @param userManager usermanager to copy.
   */
  public UserManager(UserManager userManager) {
    this.userList = new ArrayList<>(userManager.userList);
    this.exerciseDatabase = new ArrayList<>(userManager.exerciseDatabase);
  }

  private void loadExercises() {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new AppModule());

    try (InputStream inputStream = 
        getClass().getResourceAsStream("/com/json/defaultExercises.json")) {
      if (inputStream == null) {
        throw new FileNotFoundException("Resource not found: com/json/defaultExercises.json");
      }

      exerciseDatabase = 
          objectMapper.readValue(inputStream, new TypeReference<List<Exercise>>() {});
    } catch (IOException e) {
      e.printStackTrace();
      exerciseDatabase = new ArrayList<>(); // Initialize to an empty list on failure
    }
  }

  public List<Exercise> getExerciseDatabase() {
    return new ArrayList<Exercise>(exerciseDatabase);
  }

  /**
   * Creates a new user.

   * @param username username
   * @param password password
   * @param fullName fullname
   * @param eMail email
   * @return user object.
   */
  public User createNewUser(String username, String password, String fullName, String eMail) {
    User newUser = new User(username, password, fullName, eMail, new ArrayList<>(exerciseDatabase));

    UserComparator userComparator = new UserComparator();

    for (User userInList : userList) {
      if (userComparator.compare(userInList, newUser) == 0) {
        throw new IllegalArgumentException("A user with that name already exists!");
      }
    }

    addUser(newUser);

    return newUser;
  }

  /**
   * Adds a user to the user manager.

   * @param user user object.
   * @throws IllegalArgumentException if user is null or if the user already exist.
   */
  public void addUser(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User can not be null");
    }

    UserComparator comparator = new UserComparator();

    for (User userInList : userList) {
      if (comparator.compare(userInList, user) == 0) {
        throw new IllegalArgumentException("Such a user already exists!");
      }
    }
    userList.add(user);
  }

  /**
   * Removes a user from the user manager.

   * @param username user
   * @throws NoSuchElementException if no such user exist.
   */
  public boolean removeUser(String username) {
    boolean isDeleted = false;

    Optional<User> user = 
        userList.stream().filter((u) -> u.getUsername().equals(username)).findAny();

    if (user.isPresent()) {
      userList.remove(user.get());
      isDeleted = true;
    }

    return isDeleted;
  }

  public List<User> getUsers() {
    return userList;
  }

  /**
   * Validates user login. Checks all users in the user manger for any that match both username and
   * password.

   * @param username username
   * @param password password
   * @return true if user is found and username and password is correct.
   */
  public boolean validateUser(String username, String password) {
    boolean userValidated = false;
    for (User user : userList) {
      if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
        userValidated = true;
      }
    }

    return userValidated;
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    string.append("users: [");
    for (User user : userList) {
      string.append(user + ", ");
    }

    if (!userList.isEmpty()) {
      string.deleteCharAt(string.lastIndexOf(","));
      string.deleteCharAt(string.lastIndexOf(" "));
    }

    string.append("]");
    return string.toString();
  }
}
