package com.core;

/**
 * Validates the eMail according to the EMAIL_PATTERN.
 */
public class EmailValidation {

  private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

  /*
   * EMAIL REQUIREMENTS:
   * Contains at least one letter/number before and after '@'
   * Contains an '@'
   * Contains for example: '.com'
   */

  /**
   * Validates email input.

   * @param eMail email to check.
   * @return true if email is according to matcher pattern.
   */
  public static boolean validateEmail(String eMail) {
    return eMail
        .matches(EMAIL_PATTERN);
  }
}
