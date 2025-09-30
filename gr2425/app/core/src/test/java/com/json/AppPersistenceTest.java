package com.json;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import com.core.WorkoutSet;
import com.core.enums.Force;
import com.core.enums.Intensity;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;
import com.core.enums.Type;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests file persistence.
 */
public class AppPersistenceTest {

  private static AppPersistence persistence;
  private static UserManager userManager;

  /**
   * Test setup.
   */
  @BeforeEach
  public void setUp() {

    // SETTING UP TEST DATA FOR FILE:

    userManager = new UserManager();
    WorkoutSet workoutSet = new WorkoutSet(100, 10);
    WorkoutSet workoutSet2 = new WorkoutSet(90, 8);

    WorkoutSessionExercise exercise1 =
        new WorkoutSessionExercise("Benchpress", Purpose.CHEST, Mechanics.COMPOUND, Force.PUSH);

    exercise1.addWorkoutSet(workoutSet);
    exercise1.addWorkoutSet(workoutSet2);
    exercise1.addWarmupSet(workoutSet2);

    WorkoutSet workoutSet3 = new WorkoutSet(100, 5);
    WorkoutSessionExercise exercise2 =
        new WorkoutSessionExercise("Incline Benchpress", 
            Purpose.CHEST, 
            Mechanics.COMPOUND, 
            Force.PUSH);
    exercise2.addWorkoutSet(workoutSet3);
    exercise2.addWarmupSet(workoutSet3);

    WorkoutSession session1 =
        new WorkoutSession("Early Morning Workout", 
            Type.STRENGTH, 
            Intensity.HIGH, 
            LocalDateTime.now());

    WorkoutSession session2 =
        new WorkoutSession("Late Workout", 
            Type.STRENGTH, 
            Intensity.HIGH, 
            LocalDateTime.of(2023, 10, 5, 10, 10));

    session2.addExerciseToWorkout(exercise2);
    session1.addExerciseToWorkout(exercise1);
    session1.addExerciseToWorkout(exercise2);

    User user1 = 
        userManager.createNewUser("chripaul", "Voff123!", "christoffer p", "chris@gmail.com");

    user1.addSession(session1);
    user1.addSession(session2);

    User user2 = userManager.createNewUser("tester", "Test123!", "Test mcTest", "test@gmail.com");

    user2.addExercise(new Exercise("test", null, null, null));

    persistence = new AppPersistence();
    persistence.setSaveFile("usermanager.json");

    try {
      persistence.saveUserManager(userManager);
    } catch (IllegalStateException | IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Remove temp files.
   */
  @AfterEach
  public void initialize() {

    try {
      Files.deleteIfExists(Paths.get(System.getProperty("user.home"), "usermanager.json"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Test
  public void testFilePath() {
    // Has to set the file to a json file
    persistence.setSaveFile("usermanager.json");

    Path filePath = persistence.getFilePath();

    // name of file should be "usermanager.json"

    File file = filePath.toFile();

    assertEquals("usermanager.json", file.getName());

  }

  @Test
  public void testDefensiveConstructor() {
    AppPersistence appPersistence = new AppPersistence();

    appPersistence.setSaveFile("TESTFILEPATH");

    AppPersistence copyPersistence = new AppPersistence(appPersistence);

    assertEquals(appPersistence.getFilePath(), copyPersistence.getFilePath());
  }

  @Test
  public void testSave() {

    // should now not throw any exceptions

    assertDoesNotThrow(() -> persistence.saveUserManager(userManager));

    try {
      persistence.saveUserManager(userManager);
    } catch (IllegalStateException | IOException e) {
      e.printStackTrace();
    }

    File file = persistence.getFilePath().toFile();

    // File should be identical on the same filePath

    assertEquals(new File(System.getProperty("user.home"), "usermanager.json"), file);
  }

  @Test
  public void testLoad() {

    try {

      persistence.setSaveFile("usermanager.json");
      UserManager userManager = persistence.loadUserManager();

      // UserManager object should not be null if loading works properly.

      assertNotNull(userManager);

    } catch (IllegalStateException | IOException e) {
      e.printStackTrace();
    }
  }
}
