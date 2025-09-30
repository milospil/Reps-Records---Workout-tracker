package com.restserver;

import com.core.User;
import com.core.WorkoutSession;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Spring Boot Service for handling workout logic.
 */
@Service
public class WorkoutSessionService {

  private User currentUser;

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  /**
   * Returns all workout sessions for the current user.

   * @return list of workout sessions.
   */
  public List<WorkoutSession> getAllSessions() {
    if (currentUser == null) {
      throw new IllegalStateException("User must be set before retrieving all sessions");
    }
    return currentUser.getSessions();
  }

  /**
   * Adds a workout session to the current user.

   * @param workoutSession workout session.
   */
  public void addWorkoutSession(String username, WorkoutSession workoutSession) 
      throws IllegalArgumentException, IllegalStateException {
    if (currentUser == null) {
      throw new IllegalStateException("User must be set before retrieving all sessions");
    }
    currentUser.addSession(workoutSession);
  }

  /**
   * Gets a workout session based on its name.

   * @param name workout session name.
   * @return workout session.
   */
  public WorkoutSession getSessionByName(String name) {
    return currentUser
        .getSessions().stream().filter(session -> session.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException());
  }

  /**
   * Updates a given workout session.

   * @param workoutSession new workout session.
   */
  public void updateWorkoutSession(WorkoutSession workoutSession) {
    if (currentUser == null) {
      throw new IllegalStateException("User must be set before attempting to add a session");
    }
    currentUser.getSessions().stream()
        .filter(session -> session.getName().equals(workoutSession.getName()))
        .findFirst()
        .ifPresentOrElse(existingSession -> {
          existingSession.setType(workoutSession.getType());
          existingSession.setIntensity(workoutSession.getIntensity());
          existingSession.setDurationTime(workoutSession.getDurationTime());
          existingSession.setExerciseList(workoutSession.getExerciseList());
        }, () -> {
          throw new IllegalArgumentException(
              "Workout Session was not found with the name: " + workoutSession.getName());
        });
  }

  /**
   * Removes a workout session.

   * @param workoutSession workout session to remove.
   */
  public void removeWorkoutSession(WorkoutSession workoutSession) {
    if (currentUser == null) {
      throw new IllegalStateException("User must be set before attempting to remove a session");
    }

    currentUser.removeWorkout(workoutSession);
  }
}
