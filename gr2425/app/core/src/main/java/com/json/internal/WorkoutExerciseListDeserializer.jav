package com.json.internal;

import java.io.IOException;

import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import com.core.WorkoutSet;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class WorkoutExerciseListDeserializer extends JsonDeserializer<WorkoutExerciseList>{

    private WorkoutExerciseDeserializer workoutExerciseDeserializer = new WorkoutExerciseDeserializer();

    @Override
    public WorkoutExerciseList deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {

        TreeNode treeNode = parser.getCodec().readTree(parser);
        return deserialize((JsonNode) treeNode);
    }

    public WorkoutExerciseList deserialize(JsonNode jsonNode) {

        if (jsonNode instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            WorkoutSessionExercise workoutSessionExercise = new WorkoutSessionExercise();
          
            JsonNode exerciseNode = objectNode.get("exerciseName");
            if (exerciseNameNode instanceof TextNode) {
                workoutSessionExercise.setExerciseName(((TextNode) exerciseNameNode).asText());
            }

            JsonNode setsNode = objectNode.get("workoutSets");
            if (setsNode instanceof ArrayNode) {
                for (JsonNode elementNode : ((ArrayNode) setsNode)) {
                    WorkoutSet workoutSet = workoutSetDeserializer.deserialize(elementNode);
                    if (workoutSet != null) {
                        workoutSessionExercise.addWorkoutSet(workoutSet);
                    }
                    workoutExerciseList.addExercise(workoutSessionExercise);
                }
            }
            return workoutExerciseList;
        }
        return null;
    }
}