package com.json.internal;


import com.core.WorkoutSessionExercise;
import com.core.WorkoutSet;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Serializer class for workout session exercises.
 */
public class WorkoutSessionExerciseSerializer extends JsonSerializer<WorkoutSessionExercise> {

  /*
   * WorkoutSessionExercise Object Format:
   * 
   * { "name": "exerciseName" "sets": [......] }
   */
  @Override
  public void serialize(WorkoutSessionExercise sessionExercise, JsonGenerator jGen,
      SerializerProvider serializerProvider) throws IOException {
    jGen.writeStartObject();
    jGen.writeStringField("exerciseName", sessionExercise.getExerciseName());
    jGen.writeObjectField("purpose", sessionExercise.getPurpose());
    jGen.writeObjectField("mechanics", sessionExercise.getMechanics());
    jGen.writeObjectField("force", sessionExercise.getForce());

    jGen.writeArrayFieldStart("workoutSets");
    for (WorkoutSet set : sessionExercise.getWorkoutSets()) {
      jGen.writeObject(set);
    }
    jGen.writeEndArray();

    jGen.writeArrayFieldStart("warmupSets");
    for (WorkoutSet set : sessionExercise.getWarmupSets()) {
      jGen.writeObject(set);
    }
    jGen.writeEndArray();

    jGen.writeEndObject();
  }
}
