package com.restserver;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.core.User;
import com.core.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Spring Boot testing of user manager controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserManagerControllerIntTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  @MockBean
  private UserManager testUserManager;

  private List<User> testUserList;

  /**
   * Set up test enviromnent.
   */
  @BeforeEach
  public void setUp() {

    testUserList = new ArrayList<>();
    testUserList.add(new User("testuser", 
        "Password123!", 
        "Test User", 
        "test@example.com", 
        new ArrayList<>()));

    when(userService.getUserManager()).thenReturn(testUserManager);
    // Mock UserManager to return the list of users
    when(testUserManager.getUsers()).thenReturn(testUserList);

    doNothing().when(userService).autoSaveUserManager();
  }

  /**
   * Test for the getUserManager() method.
   */
  @Test
  public void testGetUserManager() throws Exception {
    // Convert the entire UserManager (with users list) to JSON
    String expectedJson = objectMapper.writeValueAsString(testUserManager);

    mockMvc.perform(get("/usermanager"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(expectedJson));
  }

  /**
   * Test for the setUserManager() method.
   */
  @Test
  public void testSetUserManager() throws Exception {
    // Convert the UserManager to JSON
    String userManagerJson = objectMapper.writeValueAsString(testUserManager);

    mockMvc.perform(put("/usermanager")
        .contentType(MediaType.APPLICATION_JSON)
        .content(userManagerJson))
        .andExpect(status().isOk());
  }
}
