package com.json.internal;


import com.core.WorkoutSet;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

/**
 * Deserializes WorkoutSet objects.
 */
public class WorkoutSetDeserializer extends JsonDeserializer<WorkoutSet> {

  @Override
  public WorkoutSet deserialize(JsonParser p, 
      DeserializationContext ctxt) throws IOException, JacksonException {
    TreeNode treeNode = p.getCodec().readTree(p);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Deserializes Workout Set objects.

   * @param jsonNode jsonNode.
   * @return WorkoutSet object.
   */
  public WorkoutSet deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      WorkoutSet workoutSet = new WorkoutSet();

      JsonNode setNumberNode = objectNode.get("setNumber");
      if (setNumberNode instanceof IntNode) {
        workoutSet.setSetNumber(((IntNode) setNumberNode).asInt());
      }
      JsonNode kilosNode = objectNode.get("kilosLifted");
      if (kilosNode instanceof IntNode) {
        workoutSet.setKilosLifted(((IntNode) kilosNode).asInt());
      }
      JsonNode repsNode = objectNode.get("reps");
      if (repsNode instanceof IntNode) {
        workoutSet.setReps(((IntNode) repsNode).asInt());
      }
      return workoutSet;
    }
    return null;
  }

}
