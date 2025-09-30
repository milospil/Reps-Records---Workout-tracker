package com.restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.core.User;
import com.core.WorkoutSession;
import com.core.enums.Intensity;
import com.core.enums.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


/**
 * Spring Boot Testing of workout service.
 */
@SpringBootTest(classes = RestserverApplication.class)
@ActiveProfiles("test")
public class WOSServiceIntegrationTest {

  private WorkoutSessionService workoutSessionService;
  private User mockUser;
  private WorkoutSession mockSession;

  /**
   * Test setup environment.
   */
  @BeforeEach
  public void setUp() {
    workoutSessionService = new WorkoutSessionService();
    mockUser = mock(User.class);
    mockSession = new WorkoutSession("Session 1", Type.CARDIO, Intensity.HIGH, LocalDateTime.now());

    List<WorkoutSession> sessions = new ArrayList<>();
    sessions.add(mockSession);
    when(mockUser.getSessions()).thenReturn(sessions);
  }

  // @Test
  // public void testSetCurrentUser() {
  // workoutSessionService.setCurrentUser(mockUser);
  // assertEquals(mockUser, workoutSessionService.getCurrentUser());
  // }
  @Test
  public void testAddWorkoutSession() {
    workoutSessionService.setCurrentUser(mockUser);

    WorkoutSession newSession = 
        new WorkoutSession("New Session", Type.STRENGTH, Intensity.MEDIUM, LocalDateTime.now());
    workoutSessionService.addWorkoutSession(mockUser.getUsername(), newSession);

    verify(mockUser, times(1)).addSession(newSession);
  }

  @Test
  public void testAddWorkoutSessionWithoutUserThrowsException() {
    assertThrows(IllegalStateException.class, 
        () -> workoutSessionService.addWorkoutSession(mockUser.getUsername(), mockSession));
  }

  @Test
  public void testGetAllSessions() {
    workoutSessionService.setCurrentUser(mockUser);

    List<WorkoutSession> sessions = workoutSessionService.getAllSessions();
    assertNotNull(sessions);
    assertEquals(1, sessions.size());
  }

  @Test
  public void testGetAllSessionsWithoutUserThrowsException() {
    assertThrows(IllegalStateException.class, () -> workoutSessionService.getAllSessions());
  }

  @Test
  public void testGetSessionByName() {
    workoutSessionService.setCurrentUser(mockUser);

    WorkoutSession session = workoutSessionService.getSessionByName("Session 1");
    assertNotNull(session);
    assertEquals("Session 1", session.getName());
  }

  @Test
  public void testGetSessionByNameNotFound() {
    workoutSessionService.setCurrentUser(mockUser);

    assertThrows(IllegalArgumentException.class, 
        () -> workoutSessionService.getSessionByName("NonExistentSession"));
  }

  @Test
  public void testUpdateWorkoutSession() {
    workoutSessionService.setCurrentUser(mockUser);

    WorkoutSession updatedSession = 
        new WorkoutSession("Session 1", Type.STRENGTH, Intensity.LOW, LocalDateTime.now());
    workoutSessionService.updateWorkoutSession(updatedSession);

    verify(mockUser, times(1)).getSessions();
  }

  @Test
  public void testRemoveWorkoutSession() {
    workoutSessionService.setCurrentUser(mockUser);
    workoutSessionService.removeWorkoutSession(mockSession);
    verify(mockUser, times(1)).removeWorkout(mockSession);
  }

  @Test
  public void testRemoveWorkoutSessionWithoutUserThrowsException() {
    assertThrows(IllegalStateException.class, 
        () -> workoutSessionService.removeWorkoutSession(mockSession));
  }

  @Test
  public void testUpdateWOSessionNoUser() {
    workoutSessionService.setCurrentUser(null);
    IllegalStateException exception = assertThrows(IllegalStateException.class,
        () -> workoutSessionService.updateWorkoutSession(mockSession));
    assertEquals("User must be set before attempting to add a session", exception.getMessage());
  }

  @Test
  public void testUpdateSessionNonExistentName() {
    workoutSessionService.setCurrentUser(mockUser);
    List<WorkoutSession> sessions = new ArrayList<>();
    sessions
        .add(new WorkoutSession("Different Session",
            Type.CARDIO, 
            Intensity.HIGH, 
            LocalDateTime.now().minusHours(1)));

    when(mockUser.getSessions()).thenReturn(sessions);

    WorkoutSession updatedSession = 
        new WorkoutSession("NonExistentSession", Type.STRENGTH, Intensity.LOW,
        LocalDateTime.now());
    assertThrows(IllegalArgumentException.class, 
        () -> workoutSessionService.updateWorkoutSession(updatedSession));
  }
}
