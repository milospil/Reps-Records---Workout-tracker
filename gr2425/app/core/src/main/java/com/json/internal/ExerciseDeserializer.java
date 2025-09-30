package com.json.internal;

import com.core.Exercise;
import com.core.enums.Force;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;

/**
 * Deserializer for exercises.
 */
public class ExerciseDeserializer extends JsonDeserializer<Exercise> {

  @Override
  public Exercise deserialize(JsonParser p, 
      DeserializationContext ctxt) throws IOException, JacksonException {
    TreeNode treeNode = p.getCodec().readTree(p);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Deserializer for exercises.

   * @param jsonNode node.
   * @return exercise object
   */
  public Exercise deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      Exercise exercise = new Exercise();

      JsonNode nameNode = objectNode.get("exerciseName");
      if (nameNode instanceof TextNode) {
        exercise.setExerciseName(((TextNode) nameNode).asText());
      }

      JsonNode purposeNode = objectNode.get("purpose");
      if (purposeNode.isTextual()) {
        Purpose purpose = Purpose.valueOf(purposeNode.asText().toUpperCase());
        exercise.setPurpose(purpose);
      }

      JsonNode mechanicsNode = objectNode.get("mechanics");
      if (mechanicsNode.isTextual()) {
        Mechanics mechanics = Mechanics.valueOf(mechanicsNode.asText().toUpperCase());
        exercise.setMechanics(mechanics);
      }

      JsonNode forceNode = objectNode.get("force");
      if (forceNode.isTextual()) {
        Force force = Force.valueOf(forceNode.asText().toUpperCase());
        exercise.setForce(force);
      }

      return exercise;
    }
    return null;
  }

}
