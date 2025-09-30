package com.json.internal;


import com.core.Exercise;
import com.core.User;
import com.core.WorkoutSession;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Serializer for User objects.
 */
public class UserSerializer extends JsonSerializer<User> {

  /*
   * Exercise Object Format:
   * 
   * { "username": "username" "password": "Voff123!" "eMail": "username@gmail.com" "fullName":
   * "firstname lastname" "workoutList": [{ "name": "workoutName" "type": Type.STRENGTH "intensity":
   * Intensity.HIGH "exercises": [{ "name": "Benchpress" "workoutSets": [.....] }] }]
   * "exerciseDatabase": [.....] }
   * 
   */
  @Override
  public void serialize(User user, 
      JsonGenerator jGen, 
      SerializerProvider serializerProvider) throws IOException {
    jGen.writeStartObject();

    jGen.writeStringField("username", user.getUsername());
    jGen.writeStringField("password", user.getPassword());
    jGen.writeStringField("eMail", user.getEmail());
    jGen.writeStringField("fullName", user.getFullName());

    jGen.writeArrayFieldStart("workoutList");
    for (WorkoutSession session : user.getSessions()) {
      jGen.writeObject(session);
    }
    jGen.writeEndArray();

    jGen.writeArrayFieldStart("exerciseDatabase");
    for (Exercise exercise : user.getExerciseDatabase()) {
      jGen.writeObject(exercise);
    }
    jGen.writeEndArray();

    jGen.writeEndObject();
  }
}
