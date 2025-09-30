package com.fxui;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.core.WorkoutSession;
import com.core.enums.Force;
import com.core.enums.Intensity;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;
import com.core.enums.Type;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.json.AppPersistence;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for remote access model.
 * RemoteUserManagerAccessTest
 */
public class RemoteUserManagerAccessTest {

  private WireMockServer mockServer = new WireMockServer(options().port(8089));

  private RemoteUserManagerAccess remoteUserManagerAccess;
  private static final String BASE_URL = "http://localhost:8089";

  /**
   * Start server.
   */
  @BeforeEach
  public void setUp() {
    mockServer.start();
    remoteUserManagerAccess = 
        new RemoteUserManagerAccess(new AppPersistence(), URI.create(BASE_URL));
  }

  /**
   * Stops server.
   */
  @AfterEach
  public void tearDown() {
    mockServer.stop();
  }

  @Test
  public void testCreateUser() throws Exception {
    User user = 
        new User("testuser", "Password123!", "Test User", "test@example.com", new ArrayList<>());

    String userJson = """
        {
            "username": "testuser",
            "password": "Password123!",
            "eMail": "test@example.com",
            "fullName": "Test User",
            "workoutList": [],
            "exerciseDatabase": []
        }""";

    mockServer.stubFor(get(urlEqualTo("/usermanager"))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"userList\": []}")
        )
    );

    remoteUserManagerAccess.getUserManager();

    mockServer.stubFor(post(urlEqualTo("/api/users"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(userJson)
        )
    );

    User createdUser = remoteUserManagerAccess.createUser(user);

    assertNotNull(createdUser, "Created user should not be null");
    assertEquals(user.getUsername(), createdUser.getUsername());
    assertEquals(user.getEmail(), createdUser.getEmail());
    assertEquals(user.getFullName(), createdUser.getFullName());

  }

  @Test
  public void testCreateUserConflict() throws Exception {
    User user = 
        new User("existinguser", "password123", "test@example.com", "Test User", new ArrayList<>());

    mockServer.stubFor(post(urlEqualTo("/api/users"))
        .willReturn(aResponse()
            .withStatus(404)
      )
    );

    assertThrows(RuntimeException.class, () -> remoteUserManagerAccess.createUser(user),
        "Should throw exception when user already exists");
  }

  @Test
  public void testGetUserManager() {

    mockServer.stubFor(get(urlEqualTo("/usermanager"))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"userList\": []}")
        )
    );

    UserManager result = remoteUserManagerAccess.getUserManager();

    assertNotNull(result, "UserManager should not be null");
    assertEquals(0, result.getUsers().size(), "Should return the mocked UserManager");
  }

  @Test
  public void testGetUserByUsernameSuccess() {
    String username = "testuser";

    String userJson = """
        {
            "username": "testuser",
            "password": "Password123!",
            "eMail": "test@example.com",
            "fullName": "Test User",
            "workoutList": [],
            "exerciseDatabase": []
        }""";

    mockServer.stubFor(get(urlEqualTo("/api/users/" + username))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(userJson)
        )
    );

    User actualUser = remoteUserManagerAccess.getUserByUsername(username);

    assertNotNull(actualUser, "User should not be null");
    assertEquals("testuser", actualUser.getUsername());
    assertEquals("Test User", actualUser.getFullName());
    assertEquals("test@example.com", actualUser.getEmail());
  }

  @Test
  public void testGetUserByUsernameUserNotFound() throws Exception {
    String username = "nonexistentuser";

    mockServer.stubFor(get(urlEqualTo("/api/users/" + username))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(404)
        )
    );

    assertThrows(RuntimeException.class, () -> remoteUserManagerAccess.getUserByUsername(username));
  }

  @Test
  public void testAddExerciseToUser() {
    Exercise exercise = new Exercise("Running", Purpose.STAMINA, Mechanics.COMPOUND, Force.STATIC);

    mockServer.stubFor(get(urlEqualTo("/usermanager"))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"userList\": []}")
        )
    );

    UserManager userManager = remoteUserManagerAccess.getUserManager();

    mockServer.stubFor(get(urlEqualTo("/api/users/testUser"))
        .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody("""
            {
                "username": "testUser",
                "password": "Test123!",
                "fullName": "Test Testing",
                "email": "Test@testing.com",
                "workoutSessions": [],
                "exercises": []
            }
        """)
      )
    );

    User user = remoteUserManagerAccess.getUserByUsername("testUser");

    userManager.addUser(user);

    remoteUserManagerAccess.autoSaveUserManager();

    System.out.println(userManager);

    String exerciseJson = """
        {
            "exerciseName": "Running",
            "purpose": "STAMINA",
            "mechanics": "COMPOUND",
            "force": "STATIC"
        }""";

    mockServer.stubFor(post(urlEqualTo("/api/users/testUser/exercises"))
        .willReturn(aResponse()
            .withStatus(201)
            .withHeader("Content-Type", "application/json")
            .withBody(exerciseJson)
        )
    );

    System.out.println(user);

    remoteUserManagerAccess.addExerciseToUser(user, exercise);

    assertTrue(user.getExerciseDatabase().contains(exercise), 
        "The exercise should be added to the user's exercise database.");
  }

  @Test
  public void testAddWorkoutToUser() {
    WorkoutSession session = new WorkoutSession("Workout", 
        Type.STRENGTH, 
        Intensity.HIGH, 
        LocalDateTime.of(2024, 11, 13, 0, 0, 0));

    mockServer.stubFor(get(urlEqualTo("/usermanager"))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"userList\": []}")
        )
    );

    UserManager userManager = remoteUserManagerAccess.getUserManager();

    mockServer.stubFor(get(urlEqualTo("/api/users/testUser"))
        .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody("""
            {
                "username": "testUser",
                "password": "Test123!",
                "fullName": "Test Testing",
                "email": "Test@testing.com",
                "workoutSessions": [],
                "exercises": []
            }
        """)
      )
    );

    User user = remoteUserManagerAccess.getUserByUsername("testUser");

    userManager.addUser(user);

    remoteUserManagerAccess.autoSaveUserManager();

    System.out.println(userManager);

    String workoutJson = """
        {
            "name": "Workout",
            "type": "STRENGTH",
            "intensity": "HIGH",
            "date": "2024-11-13T0:0"
            "sessionDurationTime": "00:00"
            "exerciseList": []
        }""";

    mockServer.stubFor(post(urlEqualTo("/api/users/testUser/workoutSessions"))
        .willReturn(aResponse()
            .withStatus(201)
            .withHeader("Content-Type", "application/json")
            .withBody(workoutJson)
        )
    );

    remoteUserManagerAccess.addWorkoutSessionToUser(user, session);

    assertTrue(user.getSessions().contains(session), 
        "The exercise should be added to the user's exercise database.");
  }


  @Test
public void testGetAllUsernames() throws Exception {
    List<String> usernames = Arrays.asList("user1", "user2", "user3");

    String usernamesJson = """
        ["user1", "user2", "user3"]
        """;

    mockServer.stubFor(get(urlEqualTo("/api/users"))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(usernamesJson)
        )
    );

    List<String> result = remoteUserManagerAccess.getAllUsernames();

    assertNotNull(result, "Result should not be null");
    assertEquals(3, result.size(), "Should return correct number of usernames");
    assertEquals(usernames, result, "Should return the expected usernames");
  }


  @Test
  public void testCreateExerciseForUserUnexpectedStatusCode() throws Exception {
    Exercise exercise = new Exercise("Morning", Purpose.STAMINA, Mechanics.COMPOUND, Force.STATIC);

    mockServer.stubFor(get(urlEqualTo("/usermanager"))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"userList\": []}")
        )
    );

    UserManager userManager = remoteUserManagerAccess.getUserManager();

    
    mockServer.stubFor(get(urlEqualTo("/api/users/Test"))
        .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody("""
        {
          "username": "Test",
          "password": "Test123!",
          "fullName": "Test Testing",
          "email": "Test@testing.com",
          "workoutSessions": [],
          "exercises": []
          }
          """)
      )
    );
        
    User user = remoteUserManagerAccess.getUserByUsername("Test");
    
    userManager.addUser(user);
    
    mockServer.stubFor(post(urlEqualTo("/api/users/" + "Test" + "/exercises"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withRequestBody(equalToJson("""
            {
                "exerciseName": "Morning",
                "purpose": "STAMINA",
                "mechanics": "COMPOUND",
                "force": "STATIC"
            }
        """))
        .willReturn(aResponse()
            .withStatus(400) // Bad Request, for example
        )
    );

    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> remoteUserManagerAccess.addExerciseToUser(user, exercise));
    assertEquals("Unexpected response code: 400", exception.getMessage());
  }

  @Test
  public void testGetUserManagerFailedResponse() {
    mockServer.stubFor(get(urlEqualTo("/usermanager"))
          .withHeader("Accept", equalTo("application/json"))
          .willReturn(aResponse()
              .withStatus(500)
              .withHeader("Content-Type", "application/json")
          )
    );
  
    RuntimeException exception = assertThrows(RuntimeException.class, 
          () -> remoteUserManagerAccess.getUserManager());
    assertEquals("Failed to fetch UserManager from server: HTTP status 500", 
          exception.getMessage());
  }

  @Test
  public void testNetworkFailureHandling() {

    PrintStream originalErr = System.err;  // Since we are expecting the 
    System.setErr(new PrintStream(new OutputStream() { // requests to throw out ConnectExceptions
        @Override                                       // We temporarily disable the error printing
        public void write(int b) {
            
        }
    }));

    // Create a mock server that simulates network failure
    try {
      mockServer.stop(); // Stop the server to simulate network issues

      // Test getUserManager
      RuntimeException getUserManagerEx = assertThrows(RuntimeException.class, 
          () -> remoteUserManagerAccess.getUserManager());
      assertEquals("Failed to fetch UserManager from server", getUserManagerEx.getMessage());

      // Test getAllUsernames
      RuntimeException getAllUsernamesEx = assertThrows(RuntimeException.class, 
          () -> remoteUserManagerAccess.getAllUsernames());
      assertEquals("Failed to fetch usernames from server", getAllUsernamesEx.getMessage());

      // Test getUserByUsername
      RuntimeException getUserByUsernameEx = assertThrows(RuntimeException.class, 
          () -> remoteUserManagerAccess.getUserByUsername("testUser"));
      assertEquals("Failed to fetch user from server", getUserByUsernameEx.getMessage());

      // Test setUserManager
      UserManager testManager = new UserManager();
      RuntimeException setUserManagerEx = assertThrows(RuntimeException.class, 
          () -> remoteUserManagerAccess.setUserManager(testManager));
      assertEquals("Failed to save UserManager to server", setUserManagerEx.getMessage());

      // Test createUser
      User testUser = new User("test", "pass", "Test User", "test@test.com", new ArrayList<>());
      RuntimeException createUserEx = assertThrows(RuntimeException.class, 
          () -> remoteUserManagerAccess.createUser(testUser));
      assertEquals("Failed to create user on server", createUserEx.getMessage());

      // Test addExerciseToUser
      Exercise testExercise = 
          new Exercise("Test", Purpose.STAMINA, Mechanics.COMPOUND, Force.STATIC);
      RuntimeException addExerciseEx = assertThrows(RuntimeException.class, 
          () -> remoteUserManagerAccess.addExerciseToUser(testUser, testExercise));
      assertEquals("Failed to create exercise for user on server", addExerciseEx.getMessage());

      // Test addWorkoutSessionToUser
      WorkoutSession testSession = new WorkoutSession("Test", Type.STRENGTH, Intensity.HIGH, 
          LocalDateTime.now());
      RuntimeException addWorkoutEx = assertThrows(RuntimeException.class, 
          () -> remoteUserManagerAccess.addWorkoutSessionToUser(testUser, testSession));
      assertEquals("Failed to create exercise for user on server", addWorkoutEx.getMessage());

      // Restart the server for other tests
      mockServer.start();
    } finally {
      System.setErr(originalErr);
    }
  }
}