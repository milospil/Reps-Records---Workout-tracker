package com.restserver;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.core.enums.Force;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;


/**
 * Spring boot testing of user service.
 */
@SpringBootTest(classes = RestserverApplication.class)
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

  @MockBean
  private UserManager userManager;

  @Autowired
  private UserService userService;

  /**
   * Set up test environment.
   */
  @BeforeEach
  public void setUp() {

    ClassPathResource resource = new ClassPathResource("restserver-users-test.json");

    try {
      userService.getAppPersistence().setTestFile(Paths.get(resource.getURI()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    clearInvocations(userManager);
    List<User> testUsers = new ArrayList<>();
    User testUser = new User("Testing", "Password123!", "Test User", "test@example.com",
        userManager.getExerciseDatabase());
    testUsers.add(testUser);
    
    // Mock the list return
    when(userManager.getUsers()).thenReturn(testUsers);
  }

  @Test
  public void testFindUserByUsernameFailure() {
    assertThrows(NoSuchElementException.class, 
        () -> userService.getUserByUsername("NonExistentUser"));
  }

  @Test
  public void testCreateUserSuccess() {
    User newUser = new User("SuccessUser", "BraBra111!", "Per Arne", "success@success.com",
        userManager.getExerciseDatabase());

    // When the user is created, the mock should not throw any exception

    User createdUser = userService.createUser(newUser);
    assertNotNull(createdUser);
    assertEquals("SuccessUser", createdUser.getUsername());
    assertEquals("success@success.com", createdUser.getEmail());
  }

  @Test
  public void testAddExerciseSuccess() {
    User user = new User("testUser", "Password123!", 
        "Test User", "test@example.com", new ArrayList<>());
    Exercise exercise = new Exercise("Push-ups", Purpose.ARMS, Mechanics.ISOLATING, Force.PULL);

    user.addExercise(exercise);
      
    List<Exercise> exercises = user.getExerciseDatabase();
    assertEquals(1, exercises.size());
    assertEquals(exercise, exercises.get(0));
  }

  @Test
  public void testGetExercisesByUsernameFailure() {
    NoSuchElementException exception = assertThrows(NoSuchElementException.class, 
        () -> userService.getExercisesByUsername("NonExistentUser"));
    assertNotNull(exception);
  }

  @Test
  public void testAddExerciseToUserSuccess() {
    Exercise exercise = new Exercise("Push-ups", Purpose.ARMS, Mechanics.ISOLATING, Force.PULL);

    List<User> testUsers = new ArrayList<>();
    User testUser = new User("Testing", "Password123!", "Test User", "test@example.com",
        userManager.getExerciseDatabase());
    testUsers.add(testUser);
    
    // Clear any previous interactions and set up the mock
    clearInvocations(userManager);
    when(userManager.getUsers()).thenReturn(testUsers);
    
    UserService userServiceTest = new UserService(userManager);
    
    Exercise addedExercise = userServiceTest.addExerciseToUser("Testing", exercise);
    assertNotNull(addedExercise);
    assertEquals("Push-ups", addedExercise.getExerciseName());
  }

  @Test
  public void testAddExerciseToUserFailure() {
    Exercise exercise = new Exercise("Push-ups", Purpose.ARMS, Mechanics.ISOLATING, Force.PULL);
    
    assertThrows(NoSuchElementException.class, 
        () -> userService.addExerciseToUser("NonExistentUser", exercise));
  }

  @Test
  public void testAutoSaveUserManager() {
    // Test successful save - using the same test file setup from setUp()
    ClassPathResource resource = new ClassPathResource("restserver-users-test.json");
    try {
        userService.getAppPersistence().setTestFile(Paths.get(resource.getURI()));
        userService.autoSaveUserManager();
    } catch (IOException e) {
        fail("Should not throw IOException");
    }
    
    // Test failure scenario
    UserService userServiceTest = new UserService(userManager);
    userServiceTest.autoSaveUserManager(); // This will trigger the null path case
  }
}
