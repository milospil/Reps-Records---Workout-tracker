package com.json;

import com.core.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.json.internal.AppModule;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for projects persistence logic.
 */
public class AppPersistence {

  private ObjectMapper mapper;

  /**
   * Constructor for new persistence.
   */
  public AppPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new AppModule());
    mapper.registerModule(new JavaTimeModule());
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  /**
   * Defensive copy constructor for AppPersistence.

   * @param appPersistence persistence.
   */
  public AppPersistence(AppPersistence appPersistence) {
    this.mapper = new ObjectMapper();
    this.mapper.registerModule(new AppModule());
    this.mapper.registerModule(new JavaTimeModule());
    this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    this.filePath = appPersistence.filePath;
  }

  /**
   * Creates a new module.

   * @return new AppModule.
   */
  public static SimpleModule createJacksonModule() {
    return new AppModule();
  }

  private Path filePath = null;

  public void setSaveFile(String saveFile) {
    this.filePath = Paths.get(System.getProperty("user.home"), saveFile);
  }

  public void setTestFile(Path path) {
    this.filePath = path;
  }

  public Path getFilePath() {
    return filePath;
  }

  /**
   * Loads the saved user manager into memory.

   * @return UserManager.
   * @throws IOException if something is wrong with the Reader.
   * @throws IllegalStateException if the file path is not set.
   */
  public UserManager loadUserManager() throws IOException, IllegalStateException {

    System.out.println("Loading Data . . . ");

    if (filePath == null) {
      throw new IllegalStateException("No file path has been set yet");
    }

    try (Reader reader = new FileReader(filePath.toFile(), StandardCharsets.UTF_8)) {
      return mapper.readValue(reader, UserManager.class);
    }
  }

  /**
   * Gets the objectmapper so the JSON object will be available for other classes.

   * @return ObjectMapper.
   */
  public ObjectMapper getObjectMapper() {
    return this.mapper;
  }

  /**
   * Saves the user manager to file.

   * @param userManager UserManager.
   * @throws IOException if something goes wrong with the writer.
   * @throws IllegalStateException if the file path is not set.
   */
  public void saveUserManager(UserManager userManager) throws IOException, IllegalStateException {
    if (filePath == null) {
      throw new IllegalStateException("No file path has been set yet");
    }
    try (Writer writer = new FileWriter(filePath.toFile(), StandardCharsets.UTF_8)) {
      mapper.writeValue(writer, userManager);
    }
  }
}
