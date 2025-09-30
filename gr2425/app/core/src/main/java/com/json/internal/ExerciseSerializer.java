package com.json.internal;


import com.core.Exercise;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Exercise serializer.
 */
public class ExerciseSerializer extends JsonSerializer<Exercise> {

  /*
   * Exercise Object Format:
   * 
   * { "exerciseName": "exerciseName" "purpose": Purpose.CHEST "mechanics": Mechanics.COMPOUND
   * "force": Force.PUSH "sets": [......] }
   */

  /**
   * Serializer for exercises.
   */
  @Override
  public void serialize(Exercise exercise, 
      JsonGenerator jGen, 
      SerializerProvider serializerProvider)
      throws IOException {
    jGen.writeStartObject();
    jGen.writeStringField("exerciseName", exercise.getExerciseName());
    jGen.writeObjectField("purpose", exercise.getPurpose());
    jGen.writeObjectField("mechanics", exercise.getMechanics());
    jGen.writeObjectField("force", exercise.getForce());
    jGen.writeEndObject();
  }
}
