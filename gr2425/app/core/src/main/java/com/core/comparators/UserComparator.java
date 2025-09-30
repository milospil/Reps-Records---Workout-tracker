package com.core.comparators;

import com.core.User;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator for user objects.
 */
public class UserComparator implements Comparator<User>, Serializable {

  @Override
  public int compare(User o1, User o2) {
    String name1 = o1.getUsername();
    String name2 = o2.getUsername();

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
