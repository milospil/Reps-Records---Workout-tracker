package com.core.comparators;

import com.core.WorkoutSessionExercise;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator for workout exercises.
 */
public class WorkoutExerciseComparator implements Comparator<WorkoutSessionExercise>, Serializable {

  @Override
  public int compare(WorkoutSessionExercise o1, WorkoutSessionExercise o2) {
    String name1 = o1.getExerciseName();
    String name2 = o2.getExerciseName();

    if (name1 == null && name2 == null) {
      return 0;
    }
    if (name1 == null) {
      return -1;
    }
    if (name2 == null) {
      return 1;
    }


    return name1.compareTo(name2);
  }

}
