package com.json.internal;


import com.core.WorkoutSet;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Serializer class for workout sets.
 */
public class WorkoutSetSerializer extends JsonSerializer<WorkoutSet> {

  /*
   * WorkoutSet Object Format:
   * 
   * { "setNumber": 1 "kilosLifted": 100 "reps": 10 }
   */

  @Override
  public void serialize(WorkoutSet workoutSet, 
      JsonGenerator jGen, 
      SerializerProvider serializerProvider)
      throws IOException {
    jGen.writeStartObject();

    jGen.writeNumberField("setNumber", workoutSet.getSetNumber());
    jGen.writeNumberField("kilosLifted", workoutSet.getKilos());
    jGen.writeNumberField("reps", workoutSet.getReps());

    jGen.writeEndObject();
  }
}
