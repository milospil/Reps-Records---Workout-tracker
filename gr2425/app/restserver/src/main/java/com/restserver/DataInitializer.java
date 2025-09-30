package com.restserver;

import com.core.UserManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.AppPersistence;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * DataInitializer opens previously saved data or creates a new file.
 */
@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private UserService userService;

  @Override
  public void run(String... args) throws Exception {
    String content;
    Path filePath = Paths.get(System.getProperty("user.home"), "restserver-users.json");

    if (Files.exists(filePath)) {
      System.out.println("Populating server with data...");

      List<String> lines = Files.readAllLines(filePath);
      content = String.join("", lines);

    } else {
      System.out.println("No local data... initializing empty file");

      String initialJson = "{\"userList\": []}";
      Files.write(filePath, initialJson.getBytes());
      content = initialJson;
    }

    UserManager userManager = parseJsonToUsers(content);
    userManager.getUsers().forEach(userService::addUser);
  }

  private UserManager parseJsonToUsers(String content) 
      throws JsonMappingException, JsonProcessingException {
    AppPersistence appPersistence = new AppPersistence();
    ObjectMapper objectMapper = appPersistence.getObjectMapper();
    return objectMapper.readValue(content, UserManager.class);
  }
}

