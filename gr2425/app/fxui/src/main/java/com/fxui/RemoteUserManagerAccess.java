package com.fxui;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.core.WorkoutSession;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.AppPersistence;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Remote UserManager access for communicating with a server.
 */
public class RemoteUserManagerAccess implements UserManagerAccess {

  private final URI endpointBaseUri;
  private final HttpClient httpClient;

  private ObjectMapper objectMapper;
  private AppPersistence appPersistence;

  private UserManager userManager;
  private String username;
  private WorkoutSession currentWorkoutSession;

  /**
   * Constructor sets up the app persistence.

   * @param appPersistence persistence.
   */
  public RemoteUserManagerAccess(AppPersistence appPersistence) {
    this.endpointBaseUri = URI.create("http://localhost:8080/");
    this.httpClient = HttpClient.newBuilder().build();
    setAppPersistence(appPersistence);
  }

  /**
   * Sets up a connection to a given endpoint URI.

   * @param appPersistence persistence.
   * @param endpointBaseUri endpoint URI.
   */
  public RemoteUserManagerAccess(AppPersistence appPersistence, URI endpointBaseUri) {
    this.endpointBaseUri = endpointBaseUri;
    this.httpClient = HttpClient.newBuilder().build();
    setAppPersistence(appPersistence);
  }

  /**
   * Retrieves UserManager from the remote server if not already cached.
   *
   * @return the UserManager instance.
   * @throws RuntimeException if there is an error during the HTTP request or if the response cannot
   *         be parsed.
   */
  public UserManager getUserManager() throws RuntimeException {
    if (userManager == null) {
      HttpRequest request = HttpRequest.newBuilder(endpointBaseUri.resolve("/usermanager"))
              .GET()
              .header("Accept", "application/json")
              .build();
      try {
        HttpResponse<String> response = 
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
          throw new RuntimeException("Failed to fetch UserManager from server: HTTP status " 
              + response.statusCode());
        }

        userManager = objectMapper.readValue(response.body(), UserManager.class);

      } catch (IOException | InterruptedException e) {

        throw new RuntimeException("Failed to fetch UserManager from server", e);
      }
    }
    return userManager;
  }


  /**
   * Fetches all usernames from the remote server.

   * @return all usernames in a list.
   * @throws RuntimeException if there is a failure during the HTTP request or parsing of the
   *         response.
   */
  public List<String> getAllUsernames() {
    HttpRequest request = HttpRequest.newBuilder(endpointBaseUri.resolve("/api/users")).GET()
        .header("Accept", "application/json").build();
    try {

      HttpResponse<String> response = 
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      return objectMapper.readValue(response.body(), new TypeReference<List<String>>() {});

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to fetch usernames from server", e);
    }
  }

  /**
   * Retrieves user by username from the remote server.

   * @param username the username of the user that will be retrieved
   * @return the User instance.
   * @throws RuntimeException if the user is not found or if there is a failure during the HTTP
   *         request or response parsing.
   */
  public User getUserByUsername(String username) {
    URI uri = endpointBaseUri.resolve("/api/users/" + username);
    
    HttpRequest request = HttpRequest.newBuilder(uri)
        .GET()
        .header("Accept", "application/json")
        .build();

    try {
      HttpResponse<String> response = 
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.body() == null) {
        throw new RuntimeException("Response body is null");
      }
      
      try {
        return objectMapper.readValue(response.body(), User.class);
      } catch (Exception e) {
        throw new RuntimeException("Failed to parse user JSON", e);
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to fetch user from server", e);
    }
  }

  /**
   * Saves the UserManager to the remote server.

   * @throws RuntimeException if there is an error during the HTTP request.
   *      Or if the server returns an unexpected status code.
   */
  public void setUserManager(UserManager userManager) {
    if (userManager != null) {
      try {

        String json = objectMapper.writeValueAsString(userManager);

        HttpRequest request = 
            HttpRequest.newBuilder(endpointBaseUri.resolve("/usermanager"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json)).build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      } catch (IOException | InterruptedException e) {
        throw new RuntimeException("Failed to save UserManager to server", e);
      }
    }
  }

  /**
   * Creates and saves the User to the remote server.
   *
   * @param user the user object that is going to be created on the server.
   * @return the User instance.
   * @throws RuntimeException if there is a failure during the HTTP request.
   *         Or if the response cannot be parsed.
   */
  public User createUser(User user) {
    try {
      String json = objectMapper.writeValueAsString(user);
      HttpRequest request = 
            HttpRequest.newBuilder(endpointBaseUri.resolve("/api/users"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json)).build();

      HttpResponse<String> response = 
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      userManager.addUser(user);
      autoSaveUserManager();

      return objectMapper.readValue(response.body(), User.class);

    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to create user on server", e);
    }
  }

  /**
   * Creates and saves Exercise for User on remote server .
   *
   * @param user of the user to create exercise for
   * @param exercise created for user.
   * @throws RuntimeException if the server responds with an unexpected status code or if there is a
   *         failure during the HTTP request.
   */
  public void addExerciseToUser(User user, Exercise exercise) {
    try {

      String json = objectMapper.writeValueAsString(exercise);

      HttpRequest request = 
          HttpRequest
          .newBuilder(endpointBaseUri.resolve("/api/users/" + user.getUsername() + "/exercises"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json)).build();

      HttpResponse<String> response = 
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 201) {
        throw new RuntimeException("Unexpected response code: " + response.statusCode());
      }

      User userToAddExercise = userManager.getUsers().stream()
          .filter(u -> u.getUsername().equals(user.getUsername())).findAny().orElseThrow();
      userToAddExercise.addExercise(exercise);

      autoSaveUserManager();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to create exercise for user on server", e);
    }
  }

  /**
   * Adds a workout session to the given user.

   * @param user user to add session to.
   * @param workoutSession session to add.
   */
  public void addWorkoutSessionToUser(User user, WorkoutSession workoutSession) {
    try {
      System.out.println("Made a http request to add session to: " + user.getUsername());
      String json = objectMapper.writeValueAsString(workoutSession);

      HttpRequest request = 
          HttpRequest
          .newBuilder(endpointBaseUri.resolve("/api/users/" 
              + user.getUsername()
              + "/workoutSessions"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json)).build();

          
      HttpResponse<String> response = 
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      
      User userToAddWorkout = userManager.getUsers().stream()
          .filter(u -> u.getUsername().equals(user.getUsername())).findAny().orElseThrow();
      userToAddWorkout.addSession(workoutSession);
      
      autoSaveUserManager();

      if (response.statusCode() != 201) {
        throw new RuntimeException("Unexpected response code: " + response.statusCode());
      }

    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to create exercise for user on server", e);
    }
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public WorkoutSession getCurrentWorkoutSession() {
    return currentWorkoutSession;
  }

  public void setCurrentWorkoutSession(WorkoutSession workoutSession) {
    this.currentWorkoutSession = workoutSession;
  }

  /**
   * Saves the usermanager automatically.
   */
  public void autoSaveUserManager() {
    if (appPersistence != null) {
      try {
        setUserManager(userManager);
      } catch (IllegalStateException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public AppPersistence getAppPersistence() {
    return appPersistence;
  }

  /**
   * Sets the app persistence and objectmapper.
   */
  public void setAppPersistence(AppPersistence appPersistence) {
    this.appPersistence = appPersistence;
    this.objectMapper = appPersistence.getObjectMapper();
  }


}
