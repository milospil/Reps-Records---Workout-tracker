package com.fxui;

/**
 * Singleton class for providing data access.
 */
public class UserManagerAccessProvider {

  private static UserManagerAccess instance;

  private UserManagerAccessProvider() {
    // Private constructor to prevent instantiation
  }

  /**
   * Creates a singleton instance of either DirectUserManagerAccess,
   * or RemoteUserManagerAccess.

   * @return UserManagerAccess.
   */
  public static UserManagerAccess getInstance() {
    if (instance == null) {
      throw new IllegalStateException("UserManagerAccess instance has not been set.");
    }
    return instance;
  }

  public static void setInstance(UserManagerAccess userManagerAccess) {
    instance = userManagerAccess;
  }
}
