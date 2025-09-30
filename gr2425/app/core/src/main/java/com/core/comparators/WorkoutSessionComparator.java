package com.core.comparators;

import com.core.WorkoutSession;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Comparator for workout sessions.
 */
public class WorkoutSessionComparator implements Comparator<WorkoutSession>, Serializable {

  @Override
  public int compare(WorkoutSession o1, WorkoutSession o2) {
    String name1 = o1.getName();
    String name2 = o2.getName();

    if (name1 == null && name2 == null) {
      return 0;
    }
    if (name1 == null) {
      return -1;
    }
    if (name2 == null) {
      return 1;
    }

    int nameComparison = name1.compareTo(name2);
    if (nameComparison != 0) {
      return nameComparison;
    }

    LocalDateTime date1 = o1.getDate();
    LocalDateTime date2 = o2.getDate();

    // If names are equal, compare dates
    if (date1 == null && date2 == null) {
      return 0;
    }
    if (date1 == null) {
      return -1;
    }
    if (date2 == null) {
      return 1;
    }

    System.out.println(date1);
    System.out.println(date2);

    return date1.compareTo(date2);
  }
}
