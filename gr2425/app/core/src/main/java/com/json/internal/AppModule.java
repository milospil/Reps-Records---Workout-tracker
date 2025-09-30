package com.json.internal;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import com.core.WorkoutSet;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Persistence Module.
 */
public class AppModule extends SimpleModule {
  private static final String NAME = "AppModule";
  private static final VersionUtil VERSION_UTIL = new VersionUtil() {};

  /**
   * Module for persistence serialization and deserialization.
   */
  @SuppressWarnings("deprecation")
  public AppModule() {
    super(NAME, VERSION_UTIL.version());

    addSerializer(UserManager.class, new UserManagerSerializer());
    addDeserializer(UserManager.class, new UserManagerDeserializer());

    addSerializer(User.class, new UserSerializer());
    addDeserializer(User.class, new UserDeserializer());

    addSerializer(Exercise.class, new ExerciseSerializer());
    addDeserializer(Exercise.class, new ExerciseDeserializer());

    addSerializer(WorkoutSession.class, new WorkoutSessionSerializer());
    addDeserializer(WorkoutSession.class, new WorkoutSessionDeserializer());

    addSerializer(WorkoutSet.class, new WorkoutSetSerializer());
    addDeserializer(WorkoutSet.class, new WorkoutSetDeserializer());

    addSerializer(WorkoutSessionExercise.class, new WorkoutSessionExerciseSerializer());
    addDeserializer(WorkoutSessionExercise.class, new WorkoutSessionExerciseDeserializer());
  }
}
