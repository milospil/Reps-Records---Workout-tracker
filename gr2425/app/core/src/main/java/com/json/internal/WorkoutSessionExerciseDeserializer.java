package com.json.internal;


import com.core.WorkoutSessionExercise;
import com.core.WorkoutSet;
import com.core.enums.Force;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;
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

/**
 * Deserializer for workout session exercises.
 */
public class WorkoutSessionExerciseDeserializer extends JsonDeserializer<WorkoutSessionExercise> {

  WorkoutSetDeserializer workoutSetDeserializer = new WorkoutSetDeserializer();

  @Override
  public WorkoutSessionExercise deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JacksonException {
    TreeNode treeNode = p.getCodec().readTree(p);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Deserializes workout session exercises.

   * @param jsonNode jsonNode.
   * @return WorkoutSessionExercise object.
   */
  public WorkoutSessionExercise deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      WorkoutSessionExercise workoutSessionExercise = new WorkoutSessionExercise();

      JsonNode nameNode = objectNode.get("exerciseName");
      if (nameNode instanceof TextNode) {
        workoutSessionExercise.setExerciseName(((TextNode) nameNode).asText());
      }

      JsonNode purposeNode = objectNode.get("purpose");
      if (purposeNode.isTextual()) {
        Purpose purpose = Purpose.valueOf(purposeNode.asText().toUpperCase());
        workoutSessionExercise.setPurpose(purpose);
      }

      JsonNode mechanicsNode = objectNode.get("mechanics");
      if (mechanicsNode.isTextual()) {
        Mechanics mechanics = Mechanics.valueOf(mechanicsNode.asText().toUpperCase());
        workoutSessionExercise.setMechanics(mechanics);
      }

      JsonNode forceNode = objectNode.get("force");
      if (forceNode.isTextual()) {
        Force force = Force.valueOf(forceNode.asText().toUpperCase());
        workoutSessionExercise.setForce(force);
      }

      JsonNode workoutSetsNode = objectNode.get("workoutSets");
      if (workoutSetsNode instanceof ArrayNode) {
        for (JsonNode elementNode : (ArrayNode) workoutSetsNode) {
          WorkoutSet workoutSet = workoutSetDeserializer.deserialize(elementNode);
          if (workoutSet != null) {
            workoutSessionExercise.addWorkoutSet(workoutSet);
          }
        }
      }

      JsonNode warmupSetsNode = objectNode.get("warmupSets");
      if (warmupSetsNode instanceof ArrayNode) {
        for (JsonNode elementNode : (ArrayNode) warmupSetsNode) {
          WorkoutSet workoutSet = workoutSetDeserializer.deserialize(elementNode);
          if (workoutSet != null) {
            workoutSessionExercise.addWarmupSet(workoutSet);
          }
        }
      }

      return workoutSessionExercise;
    }
    return null;
  }

}
