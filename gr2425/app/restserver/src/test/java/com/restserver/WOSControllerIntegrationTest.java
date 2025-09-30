package com.restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 * Spring boot testing of workout controller.
 */
@SpringBootTest(classes = RestserverApplication.class)
@ActiveProfiles("test")
public class WOSControllerIntegrationTest {

  @MockBean
  private WorkoutSessionService workoutSessionService;

  @MockBean
  private UserService userService;

  @Autowired
  private WorkoutSessionController workoutSessionController;

  private User mockUser;
  private WorkoutSession mockSession;

  /**
   * Set up test environment.
   */
  @BeforeEach
  public void setUp() {

    mockUser = new User("testuser", "password", "Test User", "test@example.com", null);
    mockSession = new WorkoutSession("Session 1", Type.CARDIO, Intensity.HIGH, LocalDateTime.now());
    doNothing().when(userService).autoSaveUserManager();
  }

  @Test
  public void testAddWorkoutSession() {
    when(userService.getUserByUsername("testuser")).thenReturn(mockUser);
    ResponseEntity<WorkoutSession> response = 
        workoutSessionController.addWorkoutSession("testuser", mockSession);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  // @Test
  // public void testAddWorkoutSessionUserNotFound() {
  //   when(userService.getUserByUsername("nonexistent")).thenThrow(new NoSuchElementException());
    
  //   assertThrows(NoSuchElementException.class, () -> {
  //     workoutSessionController.addWorkoutSession("nonexistent", mockSession);
  //   });
  // }

  @Test
  public void testGetAllSessions() {
    when(userService.getUserByUsername("testuser")).thenReturn(mockUser);
    List<WorkoutSession> sessions = new ArrayList<>();
    sessions.add(mockSession);
    when(workoutSessionService.getAllSessions()).thenReturn(sessions);

    ResponseEntity<List<WorkoutSession>> response = 
        workoutSessionController.getAllSessions("testuser");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }

  @Test
  public void testGetSessionByName() {
    when(userService.getUserByUsername("testuser")).thenReturn(mockUser);
    when(workoutSessionService.getSessionByName("Session 1")).thenReturn(mockSession);

    ResponseEntity<WorkoutSession> response = 
        workoutSessionController.getSessionByName("testuser", "Session 1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Session 1", response.getBody().getName());
  }

  @Test
  public void testUpdateSession() {
    when(userService.getUserByUsername("testuser")).thenReturn(mockUser);

    WorkoutSession updatedSession = 
        new WorkoutSession("Session 1", Type.STRENGTH, Intensity.LOW, LocalDateTime.now());

    ResponseEntity<WorkoutSession> response = 
        workoutSessionController.updateSession("testuser", "Session 1",
        updatedSession);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
