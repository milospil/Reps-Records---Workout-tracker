package com.restserver;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring boot controller for users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  public UserManager getUserManager() {
    return userService.getUserManager();
  }

  private void autoSaveUserManager() {
    userService.autoSaveUserManager();
  }

  /**
   * Get all usernames.

   * @return list of usernames.
   */
  @GetMapping
  public ResponseEntity<List<String>> getAllUsernames() {
    List<String> usernames = userService.getAllUsernames();
    return new ResponseEntity<>(usernames, HttpStatus.OK);
  }

  /**
   * Get a user by its username.

   * @param username username to get.
   * @return response entity of user.
   */
  @GetMapping("/{username}")
  public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
    try {
      User user = userService.getUserByUsername(username);
      return new ResponseEntity<>(user, HttpStatus.OK);

    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Get all exercises for a given user.

   * @param username username to get exercises from.
   * @return response entity of exercises.
   */
  @GetMapping("/{username}/exercises")
  public ResponseEntity<List<Exercise>> getExercisesByUsername(@PathVariable String username) {
    List<Exercise> exercises = userService.getExercisesByUsername(username);
    return new ResponseEntity<>(exercises, HttpStatus.OK);
  }

  /**
   * Create a new user. And save the user manager.

   * @param user user json to create.
   * @return response entity of user.
   */
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    User createdUser = userService.createUser(user);
    autoSaveUserManager();
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  /**
   * Create a new excercise for a user.

   * @param username user to add the exercise to.
   * @param exercise exercise to add.
   * @return response entity of exercise.
   */
  @PostMapping("/{username}/exercises")
  public ResponseEntity<Exercise> createExercise(@PathVariable String username, 
      @RequestBody Exercise exercise) {

    Exercise createdExercise = userService.addExerciseToUser(username, exercise);
    autoSaveUserManager();
    return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
  }

  /**
   * Deletes a user from the user list.

   * @param username username to delete.
   * @return void.
   */
  @DeleteMapping("/{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    boolean isDeleted = getUserManager().removeUser(username);
    autoSaveUserManager();

    return isDeleted 
        ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

}
