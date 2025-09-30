package com.restserver;

import com.core.User;
import com.core.WorkoutSession;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot controller for workout sessions.
 */
@RestController
@RequestMapping("api/users/{username}/workoutSessions")
public class WorkoutSessionController {

  @Autowired
  private WorkoutSessionService workoutSessionService;

  @Autowired
  private UserService userService;

  /**
   * Adds a workout session based on the username.

   * @param username username to add workout session to.
   * @param workoutSession workoutsession to add.
   * @return response entity of workout session.
   */
  @PostMapping
  public ResponseEntity<WorkoutSession> addWorkoutSession(@PathVariable String username,
      @RequestBody WorkoutSession workoutSession) {

    try {
      User user = userService.getUserByUsername(username);
      workoutSessionService.setCurrentUser(user);

      workoutSessionService.addWorkoutSession(username, workoutSession);
      userService.autoSaveUserManager();
      
      return new ResponseEntity<>(workoutSession, HttpStatus.CREATED);
    } catch (NoSuchElementException | IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Returns all workout session for a given user.

   * @param username username to retrieve sessions from.
   * @return list of workout sessions.
   */
  @GetMapping
  public ResponseEntity<List<WorkoutSession>> getAllSessions(@PathVariable String username) {
    User user = userService.getUserByUsername(username);
    workoutSessionService.setCurrentUser(user);
    List<WorkoutSession> sessions = workoutSessionService.getAllSessions();
    return new ResponseEntity<>(sessions, HttpStatus.OK);
  }

  /**
   * Get a workout session based on its name.

   * @param username user to retrieve workout session from.
   * @param name name of workout session.
   * @return response entity of workout session.
   */
  @GetMapping("/{name}")
  public ResponseEntity<WorkoutSession> getSessionByName(@PathVariable String username, 
      @PathVariable String name) {
    User user = userService.getUserByUsername(username);
    workoutSessionService.setCurrentUser(user);
    WorkoutSession session = workoutSessionService.getSessionByName(name);
    return new ResponseEntity<>(session, HttpStatus.OK);
  }

  /**
   * Update a workout session with the given name.

   * @param username user to update session for.
   * @param name name of workout session to update.
   * @param workoutSession the new workout session details.
   * @return response entity of workout session.
   */
  @PutMapping("/{name}")
  public ResponseEntity<WorkoutSession> updateSession(@PathVariable String username, 
      @PathVariable String name,
      @RequestBody WorkoutSession workoutSession) {
    User user = userService.getUserByUsername(username);
    workoutSessionService.setCurrentUser(user);
    workoutSessionService.updateWorkoutSession(workoutSession);
    userService.autoSaveUserManager();
    return new ResponseEntity<>(workoutSession, HttpStatus.OK);
  }
}
