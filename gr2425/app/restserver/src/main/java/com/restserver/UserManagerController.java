package com.restserver;

import com.core.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring boot controller for user managers.
 */
@RestController
@RequestMapping("/usermanager")
public class UserManagerController {

  @Autowired
  UserService userService;

  /**
   * Gets the user manager.

   * @return response entity of user manager.
   */
  @GetMapping
  public ResponseEntity<UserManager> getUserManager() {
    UserManager userManager = userService.getUserManager();
    return new ResponseEntity<>(userManager, HttpStatus.OK);
  }

  /**
   * Update the user manager.

   * @param userManager new user manager.
   * @return response entity of user manager.
   */
  @PutMapping
  public ResponseEntity<UserManager> setUserManager(@RequestBody UserManager userManager) {
    userService.setUserManager(userManager);
    return new ResponseEntity<>(userManager, HttpStatus.OK);
  }
}
