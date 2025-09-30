package com.json.internal;


import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Serializer class for workout sessions.
 */
public class WorkoutSessionSerializer extends JsonSerializer<WorkoutSession> {

  /*
   * Workout Session Format:
   * 
   * "name": "workoutName" "type": Type.STRENGTH "intensity": Intensity.HIGH "date": 2018-10-08
   * "exerciseList": [ { "name": "Benchpress" "workoutSets": [.....] },
   * { "name": "Incline Benchpress" "workoutSets": [.....] } ]
   */
  @Override
  public void serialize(WorkoutSession workoutSession, 
      JsonGenerator jGen, 
      SerializerProvider serializerProvider)
      throws IOException {
    jGen.writeStartObject();

    jGen.writeStringField("name", workoutSession.getName());
    jGen.writeObjectField("type", workoutSession.getType());
    jGen.writeObjectField("intensity", workoutSession.getIntensity());
    jGen.writeStringField("date", workoutSession.getDate().toString());
    jGen.writeStringField("sessionDurationTime", workoutSession.getDurationTime().toString());

    jGen.writeArrayFieldStart("exerciseList");
    for (WorkoutSessionExercise exercise : workoutSession.getExerciseList()) {
      jGen.writeObject(exercise);
    }
    jGen.writeEndArray();

    jGen.writeEndObject();
  }
}
