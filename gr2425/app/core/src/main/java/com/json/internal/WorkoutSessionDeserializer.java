package com.json.internal;

import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import com.core.enums.Intensity;
import com.core.enums.Type;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Deserializer for workout sessions.
 */
public class WorkoutSessionDeserializer extends JsonDeserializer<WorkoutSession> {

  WorkoutSessionExerciseDeserializer workoutSessionExerciseDeserializer = 
      new WorkoutSessionExerciseDeserializer();

  @Override
  public WorkoutSession deserialize(JsonParser p, 
      DeserializationContext ctxt) throws IOException, JacksonException {
    TreeNode treeNode = p.getCodec().readTree(p);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Deserializes workout sessions.

   * @param jsonNode jsonNode.
   * @return WorkoutSession object.
   */
  public WorkoutSession deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      WorkoutSession workoutSession = new WorkoutSession();

      JsonNode nameNode = objectNode.get("name");
      if (nameNode instanceof TextNode) {
        workoutSession.setName(((TextNode) nameNode).asText());
      }

      JsonNode typeNode = objectNode.get("type");
      if (typeNode.isTextual()) {
        Type type = Type.valueOf(typeNode.asText().toUpperCase());
        workoutSession.setType(type);
      }

      JsonNode intensityNode = objectNode.get("intensity");
      if (intensityNode.isTextual()) {
        Intensity intensity = Intensity.valueOf(intensityNode.asText().toUpperCase());
        workoutSession.setIntensity(intensity);
      }

      JsonNode dateNode = objectNode.get("date");
      if (dateNode.isTextual()) {
        LocalDateTime date = LocalDateTime.parse(dateNode.asText());
        workoutSession.setDate(date);
      }

      JsonNode durationNode = objectNode.get("sessionDurationTime");
      if (durationNode.isTextual()) {
        LocalTime duration = LocalTime.parse(durationNode.asText());
        workoutSession.setDurationTime(duration);;
      }

      JsonNode exerciseListNode = objectNode.get("exerciseList");
      if (exerciseListNode instanceof ArrayNode) {
        for (JsonNode elementNode : (ArrayNode) exerciseListNode) {
          WorkoutSessionExercise exercise = 
              workoutSessionExerciseDeserializer.deserialize(elementNode);
          if (exercise != null) {
            workoutSession.addExerciseToWorkout(exercise);
          }
        }
      }


      return workoutSession;

    }
    return null;
  }

}
