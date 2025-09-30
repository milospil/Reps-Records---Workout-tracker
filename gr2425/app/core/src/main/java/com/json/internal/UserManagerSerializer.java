package com.json.internal;


import com.core.User;
import com.core.UserManager;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Serializer for user manager objects.
 */
public class UserManagerSerializer extends JsonSerializer<UserManager> {

  /*
   * Exercise Object Format:
   * 
   * { "users": [.....] }
   */

  @Override
  public void serialize(UserManager userManager, 
      JsonGenerator jGen,
      SerializerProvider serializerProvider)
      throws IOException {
    jGen.writeStartObject();

    jGen.writeArrayFieldStart("userList");
    for (User user : userManager.getUsers()) {
      jGen.writeObject(user);
    }
    jGen.writeEndArray();
    jGen.writeEndObject();
  }
}
