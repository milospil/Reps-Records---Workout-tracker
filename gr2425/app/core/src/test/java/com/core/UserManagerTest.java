package com.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests UserManager logic.
 */
public class UserManagerTest {

  private static UserManager userManager;
  private static User testingUser;

  /**
   * Set up test environment.
   */
  @BeforeAll
  public static void setUp() {
    userManager = new UserManager();
    testingUser = userManager.createNewUser("tester", "Test123!", null, "test@gmail.com");
  }

  @Test
  public void testCopyConstructor() {
    UserManager userManager2 = new UserManager(userManager);
    assertEquals(userManager2.getUsers(), userManager.getUsers());
    assertEquals(userManager2.getExerciseDatabase(), userManager.getExerciseDatabase());
  }

  @Test
  public void testUser() {
    User user = new User();

    try {
      userManager.addUser(user);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

    // After adding a user, the userList of userManager should have a size of 1.
    // Since testing User is already added.

    assertEquals(2, userManager.getUsers().size());

    user.setUsername("test");

    // Adding another user with the same username should throw an error, because usernames should be
    // unique

    assertThrows(IllegalArgumentException.class,
        () -> userManager.addUser(new User("test", "Test123!", null, "test@gmail.com", null)));

    // Now testing to remove the extra user:

    userManager.removeUser(user.getUsername());

    assertEquals(1, userManager.getUsers().size());

    // Testing user verification:

    User testUser = userManager.createNewUser("test1", "Test123!", null, "test@gmail.com");

    assertThrows(IllegalArgumentException.class, 
        () -> userManager.createNewUser("test1", "Test123!", null, null));

    // Attempting to login with wrong password gives false

    assertFalse(userManager.validateUser("test1", "Test"));

    // Attempting to login with wrong username gives false

    assertFalse(userManager.validateUser("test", "Test123!"));

    // Correctly entering both username and password gives true:

    assertTrue(userManager.validateUser("test1", "Test123!"));


    assertEquals("users: [tester, " + testUser.getUsername() + "]", userManager.toString());

    assertThrows(IllegalArgumentException.class, () -> userManager.addUser(null));

  }

  @Test
  public void ExerciseDatabase() {

    // Each user automatically gets a database that consist of an existing database

    assertEquals(17, testingUser.getExerciseDatabase().size());

    // When adding an exercise to the database it should grow

    testingUser.addExercise(new Exercise("test", null, null, null));

    assertEquals(18, testingUser.getExerciseDatabase().size());

    // Attempting to add an existing exercise throws an error, they are compared by name

    assertThrows(IllegalArgumentException.class, 
        () -> testingUser.addExercise(new Exercise("test", null, null, null)));
  }

}
