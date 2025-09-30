package com.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.core.comparators.ExerciseComparator;
import com.core.comparators.UserComparator;
import com.core.comparators.WorkoutExerciseComparator;
import com.core.comparators.WorkoutSessionComparator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;


/**
 * Class for testing comparators.
 */
public class ComparatorTest {
  
  @Test
  public void testWorkoutComparator() {
    WorkoutSessionComparator comparator = new WorkoutSessionComparator();

    WorkoutSession session1 = new WorkoutSession("Workout", null, null, LocalDateTime.now());
    WorkoutSession session2 = new WorkoutSession("Workout", null, null, LocalDateTime.now());

    assertEquals(0, comparator.compare(session1, session2));

    session1.setDate(LocalDateTime.now().minusDays(1));

    assertNotEquals(0, comparator.compare(session1, session2));

    WorkoutSession session3 = new WorkoutSession();
    WorkoutSession session4 = new WorkoutSession();

    assertEquals(0, comparator.compare(session3, session4));

    session4.setName("Not Null");

    assertEquals(-1, comparator.compare(session3, session4));

    session4.setName(null);
    session3.setName("Not Null");

    assertEquals(1, comparator.compare(session3, session4));

    session4.setName("Not Null");

    assertEquals(0, comparator.compare(session3, session4));

    session3.setDate(LocalDateTime.now());

    assertEquals(1, comparator.compare(session3, session4));

    session3 = new WorkoutSession();

    session4.setDate(LocalDateTime.now());

    assertEquals(-1, comparator.compare(session3, session4));

  }

  @Test
  public void testWorkoutExerciseComparator() {
    WorkoutExerciseComparator comparator = new WorkoutExerciseComparator();

    WorkoutSessionExercise exercise1 = new WorkoutSessionExercise("Push Up", null, null, null);
    WorkoutSessionExercise exercise2 = new WorkoutSessionExercise("Push Up", null, null, null);

    assertEquals(0, comparator.compare(exercise1, exercise2));

    exercise1.setExerciseName(null);
    exercise2.setExerciseName(null);

    assertEquals(0, comparator.compare(exercise1, exercise2));
    
    exercise1.setExerciseName("Not Null");

    assertEquals(1, comparator.compare(exercise1, exercise2));

    exercise1.setExerciseName(null);
    exercise2.setExerciseName("Not Null");

    assertEquals(-1, comparator.compare(exercise1, exercise2));

  }

  @Test
  public void testExerciseComparator() {
    ExerciseComparator comparator = new ExerciseComparator();

    Exercise exercise1 = new Exercise(null, null, null, null);
    Exercise exercise2 = new Exercise(null, null, null, null);

    assertEquals(0, comparator.compare(exercise1, exercise2));

    exercise1.setExerciseName("Not Null");

    assertEquals(1, comparator.compare(exercise1, exercise2));

    exercise1.setExerciseName(null);
    exercise2.setExerciseName("Not Null");

    assertEquals(-1, comparator.compare(exercise1, exercise2));

  }


  @Test
  public void testUserComparator() {
    UserComparator comparator = new UserComparator();

    User user1 = new User(null, null, null, null, null);

    User user2 = new User(null, null, null, null, null);

    assertEquals(0, comparator.compare(user1, user2));

    user2.setUsername("Not Null");

    assertEquals(-1, comparator.compare(user1, user2));

  }
}
