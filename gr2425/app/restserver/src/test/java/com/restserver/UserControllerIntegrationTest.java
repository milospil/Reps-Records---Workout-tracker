package com.restserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.core.User;
import com.core.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.AppPersistence;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Spring Boot tests for user controller.
 */
@SpringBootTest(classes = RestserverApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserManager userManager;

  private ObjectMapper objectMapper;

  /**
   * Set up test environment.
   */
  @BeforeEach
  public void setUp() {

    AppPersistence appPersistence = new AppPersistence();
    objectMapper = appPersistence.getObjectMapper();

    // Mock behavior for userService.getUserManager() to return userManager mock
    when(userService.getUserManager()).thenReturn(userManager);

    // Prepare test user
    User testUser = 
        new User("TesterTest",
            "Password123!", 
            "Tester User", 
            "tester@example.com", 
            null);

    // Mock the behavior for createUser and removeUser in userManager
    when(userManager.removeUser("TesterTest")).thenReturn(true);
    when(userService.createUser(any(User.class))).thenReturn(testUser);

    doNothing().when(userService).autoSaveUserManager();
  }

  @Test
  public void testGetUserByUsername() throws Exception {
    User testUser = 
        new User("TesterTest", 
            "Password123!", 
            "Tester User", 
            "tester@example.com",
            new ArrayList<>());

    when(userService.getUserByUsername("TesterTest")).thenReturn(testUser);

    mockMvc.perform(get("/api/users/TesterTest")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.username").value("TesterTest"));
  }

  @Test
  public void testCreateUser() throws Exception {
    User newUser = 
        new User("exampleTest",
            "Example123!", 
            "Example Exampleson", 
            "example@testing.com",
            new ArrayList<>());

    String jsonString = objectMapper.writeValueAsString(newUser);

    when(userService.createUser(any(User.class))).thenReturn(newUser);

    mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(jsonString))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.username").value("exampleTest"))
        .andExpect(jsonPath("$.eMail").value("example@testing.com"));
  }

  @Test
  public void testDeleteUserSuccess() throws Exception {
    String username = "existingUser";

    when(userManager.removeUser(username)).thenReturn(true);

    mockMvc.perform(delete("/api/users/{username}", username)).andExpect(status().isNoContent());

    verify(userManager, times(1)).removeUser(username);
  }

  @Test
  public void testDeleteUserNotFound() throws Exception {
    String username = "nonExistentUser";

    when(userManager.removeUser(username)).thenReturn(false);

    mockMvc.perform(delete("/api/users/{username}", username)).andExpect(status().isNotFound());

    verify(userManager, times(1)).removeUser(username);
  }

  // @Test
  // public void testCreateExercise() throws Exception {
  // String username = "testuser";
  // Exercise newExercise = new Exercise("Push-ups", Purpose.ARMS,
  // Mechanics.COMPOUND, Force.PUSH);
  //
  // // Mock the behavior for addExerciseToUser to return the exercise as expected
  // by the controller
  // when(userService.addExerciseToUser(username,
  // newExercise)).thenReturn(newExercise);
  //
  // // Serialize newExercise to JSON
  // String exerciseJson = objectMapper.writeValueAsString(newExercise);
  //
  // // Execute the test, focusing first on status code
  // mockMvc.perform(post("/api/users/" + username + "/exercises")
  // .contentType(MediaType.APPLICATION_JSON)
  // .content(exerciseJson))
  // .andDo(print())
  // .andExpect(status().isCreated())
  // //.andExpect(jsonPath("$.exerciseName").value("Push-ups"))
  // .andExpect(jsonPath("$.purpose").value("ARMS")) // Adjusted to match the
  // Exercise fields
  // .andExpect(jsonPath("$.mechanics").value("COMPOUND"))
  // .andExpect(jsonPath("$.force").value("PUSH"));
  // }
}
