package com.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.core.enums.Force;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;
import com.core.enums.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test user class.
 */
public class UserTest {

  private static UserManager userManager;
  private static User testUser;

  /**
   * Sets up test environment.
   */
  @BeforeEach
  public void setUp() {
    userManager = new UserManager();
    testUser = 
          new User("Test", 
          "Test123!", 
          "Test Testing", 
          "Test@Testing.com", 
          userManager.getExerciseDatabase());
  }

  @Test
  public void testInvalidPassword() {
    assertThrows(IllegalArgumentException.class, () -> testUser.setPassword("1"));
  }

  @Test
  public void testInvalidEmail() {
    assertThrows(IllegalArgumentException.class, () -> testUser.setEMail("test.com"));
  }

  @Test
  public void testDefensiveConstructor() {
    User user = new User(testUser);
    assertEquals(user.getUsername(), testUser.getUsername());
    assertEquals(user.getExerciseDatabase(), testUser.getExerciseDatabase());
    assertEquals(user.getPassword(), testUser.getPassword());
    assertEquals(user.getEmail(), testUser.getEmail());
  }

  @Test
  public void testAddSameWorkout() {
    WorkoutSession session1 = 
        new WorkoutSession("Evening Workout", null, null, LocalDateTime.now());
    WorkoutSession session2 = 
        new WorkoutSession("Evening Workout", null, null, LocalDateTime.now());

    assertDoesNotThrow(() -> testUser.addSession(session1));

    assertThrows(IllegalArgumentException.class, () -> testUser.addSession(session2));
  }

  @Test
  public void testRemoveWorkout() {
    WorkoutSession session1 = 
        new WorkoutSession("Evening Workout", null, null, LocalDateTime.now());
    testUser.addSession(session1);
    assertEquals(1, testUser.getSessions().size());

    testUser.removeWorkout(session1);
    assertEquals(0, testUser.getSessions().size());
  }


  @Test
  public void testDefensiveConstructorWorkout() {
    WorkoutSession session1 = 
        new WorkoutSession("Evening Workout", Type.STRENGTH, null, LocalDateTime.now());
    WorkoutSessionExercise exercise =
        new WorkoutSessionExercise("Bench Press", Purpose.CHEST, Mechanics.COMPOUND, Force.PUSH);
    session1.addExerciseToWorkout(exercise);

    WorkoutSession sessionCopy = new WorkoutSession(session1);
    assertEquals(session1.getName(), sessionCopy.getName());
    assertEquals(session1.getDate(), sessionCopy.getDate());
    assertEquals(session1.getExerciseList().get(0).getExerciseName(),
        sessionCopy.getExerciseList().get(0).getExerciseName());
    assertEquals(session1.getType(), sessionCopy.getType());
  }

  @Test
  public void testRemoveExerciseFromWorkout() {
    WorkoutSession session1 = 
        new WorkoutSession("Evening Workout", Type.STRENGTH, null, LocalDateTime.now());
    WorkoutSessionExercise exercise =
        new WorkoutSessionExercise("Bench Press", Purpose.CHEST, Mechanics.COMPOUND, Force.PUSH);

    session1.addExerciseToWorkout(exercise);
    assertEquals(1, session1.getExerciseList().size());

    session1.removeExerciseFromWorkout(exercise);
    assertEquals(0, session1.getExerciseList().size());

    assertThrows(NoSuchElementException.class,
        () -> session1.removeExerciseFromWorkout(
          new WorkoutSessionExercise("Not Bench Press", null, null, null)));
  }

  @Test
  public void testWorkoutSessionExercises() {
    WorkoutSessionExercise exercise =
        new WorkoutSessionExercise("Bench Press", Purpose.CHEST, Mechanics.COMPOUND, Force.PUSH);
    List<WorkoutSet> workoutSets = new ArrayList<>();

    WorkoutSet set1 = new WorkoutSet(20, 5);
    WorkoutSet set2 = new WorkoutSet(30, 5);
    WorkoutSet set3 = new WorkoutSet(40, 5);

    workoutSets.addAll(List.of(set1, set2, set3));
    exercise.setWarmupSets(workoutSets);
    assertEquals(3, exercise.getWarmupSets().size());

    exercise.setWorkoutSets(workoutSets);
    assertEquals(3, exercise.getWorkoutSets().size());

    assertThrows(NoSuchElementException.class, 
        () -> exercise.removeWarmuptSet(new WorkoutSet(30, 8)));
    assertThrows(NoSuchElementException.class, 
        () -> exercise.removeWorkoutSet(new WorkoutSet(30, 8)));

    exercise.removeWarmuptSet(set3);
    assertEquals(2, exercise.getWarmupSets().size());

  }


}
