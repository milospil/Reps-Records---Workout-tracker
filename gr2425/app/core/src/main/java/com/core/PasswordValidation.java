package com.core;

/**
 * Password validator.
 */
public class PasswordValidation {

  private static final String PASSWORD_PATTERN = 
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

  /*
   * PASSWORD REQUIREMENTS:
   * At least 8 characters long
   * Contains at least one uppercase letter
   * Contains at least one lowercase letter
   * Contains at least one digit
   * Contains at least one special character
   */

  /**
   * Validates passwords according to matcher pattern.

   * @param password password to validate.
   * @return true if password is valid.
   */
  public static boolean validatePassword(String password) {
    return password
        .matches(PASSWORD_PATTERN);
  }
}
