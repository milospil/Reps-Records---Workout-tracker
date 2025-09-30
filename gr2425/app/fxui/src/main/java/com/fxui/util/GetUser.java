package com.fxui.util;

import com.core.User;
import com.fxui.UserManagerAccessProvider;
import java.util.Optional;

/**
 * Class with static method to get user.
 */
public class GetUser {

  /**
   * Static class to get user across other classes.

   * @return current user logged in.
   */
  public static User getUser() {
    Optional<User> user = 
        UserManagerAccessProvider.getInstance().getUserManager().getUsers().stream()
          .filter((u) -> 
              u.getUsername().equals(UserManagerAccessProvider.getInstance().getUsername()))
          .findFirst();

    if (!user.isPresent()) {
      throw new IllegalStateException("Something went wrong in locating the user");
    }

    return user.get();
  }
}
