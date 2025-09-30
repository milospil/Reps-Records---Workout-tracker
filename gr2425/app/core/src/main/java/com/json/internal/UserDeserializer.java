package com.json.internal;


import com.core.Exercise;
import com.core.User;
import com.core.WorkoutSession;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializer class for user objects.
 */
public class UserDeserializer extends JsonDeserializer<User> {

  WorkoutSessionDeserializer workoutSessionDeserializer = new WorkoutSessionDeserializer();
  ExerciseDeserializer exerciseDeserializer = new ExerciseDeserializer();

  @Override
  public User deserialize(JsonParser p, 
        DeserializationContext ctxt) throws IOException, JacksonException {
    TreeNode treeNode = p.getCodec().readTree(p);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Deserializes user objects.

   * @param jsonNode jsonNode
   * @return user object.
   */
  public User deserialize(JsonNode jsonNode) {

    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      User user = new User();

      JsonNode usernameNode = objectNode.get("username");
      if (usernameNode instanceof TextNode) {
        user.setUsername(((TextNode) usernameNode).asText());
      }

      JsonNode passwordNode = objectNode.get("password");
      if (passwordNode instanceof TextNode) {
        user.setPassword(((TextNode) passwordNode).asText());
      }

      JsonNode eMailNode = objectNode.get("eMail");
      if (eMailNode instanceof TextNode) {
        user.setEMail(((TextNode) eMailNode).asText());
      }

      JsonNode fullNameNode = objectNode.get("fullName");
      if (fullNameNode instanceof TextNode) {
        user.setFullName(((TextNode) fullNameNode).asText());
      }

      JsonNode workoutListNode = objectNode.get("workoutList");
      if (workoutListNode instanceof ArrayNode) {
        for (JsonNode elemntNode : (ArrayNode) workoutListNode) {
          WorkoutSession workoutSession = workoutSessionDeserializer.deserialize(elemntNode);
          if (workoutSession != null) {
            user.addSession(workoutSession);
          }
        }
      }

      JsonNode exerciseDatabaseNode = objectNode.get("exerciseDatabase");
      List<Exercise> exerciseDatabase = new ArrayList<>();
      if (exerciseDatabaseNode instanceof ArrayNode) {
        for (JsonNode elementNode : (ArrayNode) exerciseDatabaseNode) {
          Exercise exercise = exerciseDeserializer.deserialize(elementNode);
          if (exercise != null) {
            exerciseDatabase.add(exercise);
          }
        }
        user.setExercises(exerciseDatabase);
      }
      return user;
    }
    return null;
  }

}
