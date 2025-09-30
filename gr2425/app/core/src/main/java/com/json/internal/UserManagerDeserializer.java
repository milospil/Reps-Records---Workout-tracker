package com.json.internal;


import com.core.User;
import com.core.UserManager;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

/**
 * Deserializer class for user managers.
 */
public class UserManagerDeserializer extends JsonDeserializer<UserManager> {

  UserDeserializer userDeserializer = new UserDeserializer();

  @Override
  public UserManager deserialize(JsonParser p, 
        DeserializationContext ctxt) throws IOException, JacksonException {
    TreeNode treeNode = p.getCodec().readTree(p);
    if (treeNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) treeNode;
      UserManager userManager = new UserManager();

      JsonNode userListNode = objectNode.get("userList");
      if (userListNode instanceof ArrayNode) {
        for (JsonNode elemntNode : (ArrayNode) userListNode) {
          User user = userDeserializer.deserialize(elemntNode);
          if (user != null) {
            userManager.addUser(user);
          }
        }
      }
      return userManager;
    }
    return null;
  }
}
