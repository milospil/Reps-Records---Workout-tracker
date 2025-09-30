package com.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.core.Exercise;
import com.core.User;
import com.core.UserManager;
import com.core.WorkoutSession;
import com.core.WorkoutSessionExercise;
import com.core.WorkoutSet;
import com.core.enums.Force;
import com.core.enums.Intensity;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;
import com.core.enums.Type;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.json.internal.AppModule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests serialization and deserialization.
 */
public class AppModuleTest {
    
  private static ObjectMapper mapper;

  /**
   * Sets up the proper test enviornment.
   */
  @BeforeAll
  public static void setUp() {
    mapper = new ObjectMapper();
    mapper.registerModule(new AppModule());
    mapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void testSerializers() {
    User user1 = new User();
    user1.setUsername("chripaul");
    user1.setPassword("Mjau123!");
    user1.setEMail("ch@gmail.com");
    user1.setFullName("chri paul");
    
    // Creating a user exercise database
    List<Exercise> exerciseDatabase = new ArrayList<>();
    Exercise exercise1 = new Exercise(
        "Benchpress", 
        Purpose.CHEST, 
        Mechanics.COMPOUND, 
        Force.PUSH
        );
    exerciseDatabase.add(exercise1);
    
    // adding database to user
    
    user1.setExercises(exerciseDatabase);
    
    // Adding a workoutsession for user
    
    WorkoutSession session1 = new WorkoutSession(
        "Early Morning Workout", 
        Type.STRENGTH, 
        Intensity.HIGH,
        LocalDateTime.of(2023, 10, 5, 10, 10)
    );
    WorkoutSet workoutSet = new WorkoutSet(100, 10);
    WorkoutSet workoutSet2 = new WorkoutSet(90, 8);
    WorkoutSessionExercise exercise = new WorkoutSessionExercise(
        "Benchpress", 
        Purpose.CHEST, 
        Mechanics.COMPOUND, 
        Force.PUSH
        );
    
    exercise.addWorkoutSet(workoutSet);
    exercise.addWorkoutSet(workoutSet2);
        
    UserManager userManager = new UserManager();
    user1.addSession(session1);

    userManager.addUser(user1);
    
    //Testing to see if saved data is correct:
    
    try {
      assertEquals("{\"setNumber\":1,\"kilosLifted\":100,\"reps\":10}", 
          mapper.writeValueAsString(workoutSet)); //Testing workoutSerializer
      assertEquals("{\"setNumber\":2,\"kilosLifted\":90,\"reps\":8}", 
          mapper.writeValueAsString(workoutSet2)); //Testing workoutSerializer
    } catch (JsonProcessingException e) {
      fail();
    }

    // Testing workoutSession formatting:
    // Remove workoutSets to make string shorter
    
    session1.addExerciseToWorkout(exercise);
    exercise.removeWorkoutSet(workoutSet);
    exercise.removeWorkoutSet(workoutSet2);

    /*
      * Pretty Print Format of Workout Session:
      * 
      * {
      *   "name": "workoutName"
      *   "type": Type.STRENGTH
      *   "intensity": Intensity.HIGH
      *   "exerciseList": [
      *          {
      *             "exerciseName": "Benchpress"
      *             "workoutSets": [.....]
      *          }
      *      ]
      * }
      */

    try {
      assertEquals("{\"name\":\"Early Morning Workout\","
          + "\"type\":\"STRENGTH\",\"intensity\":\"HIGH\",\"date\":\"2023-10-05T10:10\","
            + "\"sessionDurationTime\":\"00:00\","
            + "\"exerciseList\":[{\"exerciseName\":\"Benchpress\","
            + "\"purpose\":\"CHEST\",\"mechanics\":\"COMPOUND\","
            + "\"force\":\"PUSH\",\"workoutSets\":[],"
            + "\"warmupSets\":[]}]}", mapper.writeValueAsString(session1));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    // Eksempel på ny metode med bedre formattering
    /*try {
        assertEquals(
            "{\"name\":\"Early Morning Workout\",\"type\":\"STRENGTH\",\"intensity\":\"HIGH\","
            + "\"exerciseList\":[{\"exerciseName\":\"Benchpress\",\"purpose\":\"CHEST\","
            + "\"mechanics\":\"COMPOUND\",\"force\":\"PUSH\",\"workoutSets\":[],"
            + "\"warmupSets\":[]}]}", 
            mapper.writeValueAsString(session1)
        );
    } catch (JsonProcessingException e) {
        e.printStackTrace();
    }*/


  }

  @Test
  public void testDeserializers() {
    // JSON string representing a User object
    String userJson = "{"
        + "\"username\": \"milospi\","
        + "\"password\": \"Voff123!\","
        + "\"eMail\": \"mp@gmail.com\","
        + "\"fullName\": \"mp mp\","
        + "\"workoutList\": [{"
        + "    \"name\": \"Mid day Workout\","
        + "    \"type\": \"STRENGTH\","
        + "    \"intensity\": \"HIGH\","
        + "    \"date\": \"2023-10-05T10:10\","
        + "    \"sessionDurationTime\":\"00:00\","
        + "    \"exerciseList\": [{"
        + "        \"exerciseName\": \"Benchpress\","
        + "        \"purpose\": \"CHEST\","
        + "        \"mechanics\": \"COMPOUND\","
        + "        \"force\": \"PUSH\","
        + "        \"workoutSets\": [{"
        + "            \"setNumber\": 1,"
        + "            \"kilosLifted\": 50,"
        + "            \"reps\": 10"
        + "        }, {"
        + "            \"setNumber\": 2,"
        + "            \"kilosLifted\": 80,"
        + "            \"reps\": 8"
        + "        }],"
        + "        \"warmupSets\":[]"
        + "    }]"
        + "}],"
        + "\"exerciseDatabase\": [{"
        + "      \"exerciseName\": \"Benchpress\","
        + "      \"purpose\": \"CHEST\","
        + "      \"mechanics\": \"COMPOUND\","
        + "      \"force\": \"PUSH\""
        + "    }]"
        + "}";


    try {
      // Deserialize JSON into User object
      User user = mapper.readValue(userJson, User.class);

      // Assert User properties
      assertEquals("milospi", user.getUsername());
      assertEquals("Voff123!", user.getPassword());
      assertEquals("mp@gmail.com", user.getEmail());
      assertEquals("mp mp", user.getFullName());

      // Assert WorkoutSession properties
      List<WorkoutSession> sessions = user.getSessions();
      assertEquals(1, sessions.size());
      WorkoutSession session = sessions.get(0);
      assertEquals("Mid day Workout", session.getName());
      assertEquals("STRENGTH", session.getType().toString());
      assertEquals("HIGH", session.getIntensity().toString());

      // Assert WorkoutSessionExercise properties
      List<WorkoutSessionExercise> exercises = session.getExerciseList();
      assertEquals(1, exercises.size());
      WorkoutSessionExercise exercise = exercises.get(0);
      assertEquals("Benchpress", exercise.getExerciseName());
      assertEquals("CHEST", exercise.getPurpose().toString());
      assertEquals("COMPOUND", exercise.getMechanics().toString());
      assertEquals("PUSH", exercise.getForce().toString());

      // Assert WorkoutSet properties
      List<WorkoutSet> workoutSets = exercise.getWorkoutSets();
      assertEquals(2, workoutSets.size());
      WorkoutSet set1 = workoutSets.get(0);
      WorkoutSet set2 = workoutSets.get(1);

      assertEquals(1, set1.getSetNumber());
      assertEquals(50, set1.getKilos());
      assertEquals(10, set1.getReps());

      assertEquals(2, set2.getSetNumber());
      assertEquals(80, set2.getKilos());
      assertEquals(8, set2.getReps());

      // Testing if exerciseDatabase is deserialized:

      List<Exercise> exerciseDatabase = user.getExerciseDatabase();

      // Database should be loaded as an object and != null;

      assertNotNull(exerciseDatabase);

      Exercise exerciseFromDatabase = exerciseDatabase.get(0);

      assertEquals("Benchpress", exerciseFromDatabase.getExerciseName());

    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Deserialization failed");
    }
  }

  @Test
    public void testDeserializersWithMultipleUsersAndSessions() {
    // JSON string representing two User objects
    String usersJson = "["
          + "{"
          + "    \"username\": \"milospi\","
          + "    \"password\": \"MipMip00!\","
          + "    \"eMail\": \"mp@gmail.com\","
          + "    \"fullName\": \"Mil Pil\","
          + "    \"workoutList\": [{"
          + "        \"name\": \"Morning Session\","
          + "        \"type\": \"STRENGTH\","
          + "        \"intensity\": \"HIGH\","
          + "        \"date\":\"2023-10-05T10:10\","
          + "        \"sessionDurationTime\":\"00:00\","
          + "        \"exerciseList\": [{"
          + "            \"exerciseName\": \"Benchpress\","
          + "            \"purpose\": \"CHEST\","
          + "            \"mechanics\": \"COMPOUND\","
          + "            \"force\": \"PUSH\","
          + "            \"workoutSets\": [{"
          + "                \"setNumber\": 1,"
          + "                \"kilosLifted\": 100,"
          + "                \"reps\": 10"
          + "            }, {"
          + "                \"setNumber\": 2,"
          + "                \"kilosLifted\": 120,"
          + "                \"reps\": 8"
          + "            }],"
          + "        \"warmupSets\":[]"
          + "        }]"
          + "    },"
          + "    {"
          + "        \"name\": \"Evening Workout\","
          + "        \"type\": \"STRENGTH\","
          + "        \"intensity\": \"MEDIUM\","
          + "        \"date\":\"2023-10-05T10:10\","
          + "        \"sessionDurationTime\":\"00:00\","
          + "        \"exerciseList\": [{"
          + "            \"exerciseName\": \"Squat\","
          + "            \"purpose\": \"LEGS\","
          + "            \"mechanics\": \"COMPOUND\","
          + "            \"force\": \"PUSH\","
          + "            \"workoutSets\": [{"
          + "                \"setNumber\": 1,"
          + "                \"kilosLifted\": 120,"
          + "                \"reps\": 5"
          + "            }, {"
          + "                \"setNumber\": 2,"
          + "                \"kilosLifted\": 110,"
          + "                \"reps\": 6"
          + "            }]"
          + "        }]"
          + "    }]"
          + "},"
          + "{"
          + "    \"username\": \"Petterper\","
          + "    \"password\": \"PerPetter123!\","
          + "    \"eMail\": \"pp@gmail.com\","
          + "    \"fullName\": \"Petter Per\","
          + "    \"workoutList\": [{"
          + "        \"name\": \"Morning Run\","
          + "        \"type\": \"CARDIO\","
          + "        \"intensity\": \"MEDIUM\","
          + "        \"date\":\"2023-10-05T10:10\","
          + "        \"sessionDurationTime\":\"00:00\","
          + "        \"exerciseList\": [{"
          + "            \"exerciseName\": \"Run\","
          + "            \"purpose\": \"STAMINA\","
          + "            \"mechanics\": \"COMPOUND\","
          + "            \"force\": \"PUSH\","
          + "            \"workoutSets\": [{"
          + "                \"setNumber\": 1,"
          + "                \"kilosLifted\": 0,"
          + "                \"reps\": 15"
          + "            }]"
          + "        }, {"
          + "            \"exerciseName\": \"Pushups\","
          + "            \"purpose\": \"CHEST\","
          + "            \"mechanics\": \"COMPOUND\","
          + "            \"force\": \"PUSH\","
          + "            \"workoutSets\": [{"
          + "                \"setNumber\": 1,"
          + "                \"kilosLifted\": 0,"
          + "                \"reps\": 20"
          + "            }, {"
          + "                \"setNumber\": 2,"
          + "                \"kilosLifted\": 0,"
          + "                \"reps\": 20"
          + "            }]"
          + "        }]"
          + "    },"
          + "    {"
          + "        \"name\": \"Evening Workout\","
          + "        \"type\": \"STRENGTH\","
          + "        \"intensity\": \"LOW\","
          + "        \"date\":\"2023-10-05T10:10\","
          + "        \"sessionDurationTime\":\"00:00\","
          + "        \"exerciseList\": [{"
          + "            \"exerciseName\": \"Squat\","
          + "            \"purpose\": \"LEGS\","
          + "            \"mechanics\": \"COMPOUND\","
          + "            \"force\": \"PUSH\","
          + "            \"workoutSets\": [{"
          + "                \"setNumber\": 1,"
          + "                \"kilosLifted\": 120,"
          + "                \"reps\": 5"
          + "            }, {"
          + "                \"setNumber\": 2,"
          + "                \"kilosLifted\": 110,"
          + "                \"reps\": 6"
          + "            }, {"
          + "                \"setNumber\": 3,"
          + "                \"kilosLifted\": 100,"
          + "                \"reps\": 8"
          + "            }]"
          + "        }]"
          + "    }]"
          + "}"
          + "]";

    try {
      // Deserialize JSON into User array
      User[] users = mapper.readValue(usersJson, User[].class);

      // Assert that there are two users
      assertEquals(2, users.length);

      // First user assertions (milospi)
      User user1 = users[0];
      assertEquals("milospi", user1.getUsername());
      assertEquals("MipMip00!", user1.getPassword());
      assertEquals("mp@gmail.com", user1.getEmail());
      assertEquals("Mil Pil", user1.getFullName());

      // Assert that user1 has two workout sessions
      List<WorkoutSession> sessions1 = user1.getSessions();
      assertEquals(2, sessions1.size());

      // First session
      WorkoutSession session1 = sessions1.get(0);
      assertEquals("Morning Session", session1.getName());
      assertEquals("STRENGTH", session1.getType().toString());
      assertEquals("HIGH", session1.getIntensity().toString());

      // Assert WorkoutSessionExercise properties for first session
      List<WorkoutSessionExercise> exercises1 = session1.getExerciseList();
      assertEquals(1, exercises1.size());

      WorkoutSessionExercise exercise1 = exercises1.get(0);
      assertEquals("Benchpress", exercise1.getExerciseName());
      assertEquals("CHEST", exercise1.getPurpose().toString());

      // Assert WorkoutSet properties for first session
      List<WorkoutSet> workoutSets1 = exercise1.getWorkoutSets();
      assertEquals(2, workoutSets1.size());
      WorkoutSet set1_1 = workoutSets1.get(0);
      assertEquals(1, set1_1.getSetNumber());
      assertEquals(100, set1_1.getKilos());
      assertEquals(10, set1_1.getReps());

      WorkoutSet set1_2 = workoutSets1.get(1);
      assertEquals(2, set1_2.getSetNumber());
      assertEquals(120, set1_2.getKilos());
      assertEquals(8, set1_2.getReps());


      // Second session
      WorkoutSession session2 = sessions1.get(1);
      assertEquals("Evening Workout", session2.getName());
      assertEquals("STRENGTH", session2.getType().toString());
      assertEquals("MEDIUM", session2.getIntensity().toString());

      // Assert WorkoutSessionExercise properties for second session
      List<WorkoutSessionExercise> exercises2 = session2.getExerciseList();
      assertEquals(1, exercises2.size());
      WorkoutSessionExercise exercise2 = exercises2.get(0);
      assertEquals("Squat", exercise2.getExerciseName());
      assertEquals("LEGS", exercise2.getPurpose().toString());

      // Assert WorkoutSet properties for second session
      List<WorkoutSet> workoutSets2 = exercise2.getWorkoutSets();
      assertEquals(2, workoutSets2.size());
      WorkoutSet set2_1 = workoutSets2.get(0);
      assertEquals(1, set2_1.getSetNumber());
      assertEquals(120, set2_1.getKilos());
      assertEquals(5, set2_1.getReps());

      WorkoutSet set2_2 = workoutSets2.get(1);
      assertEquals(2, set2_2.getSetNumber());
      assertEquals(110, set2_2.getKilos());
      assertEquals(6, set2_2.getReps());




      // Second user assertions (testUser)
      User user2 = users[1];
      assertEquals("Petterper", user2.getUsername());
      assertEquals("PerPetter123!", user2.getPassword());
      assertEquals("pp@gmail.com", user2.getEmail());
      assertEquals("Petter Per", user2.getFullName());

      // Assert that user2 has 2 workout sessions
      List<WorkoutSession> sessions2 = user2.getSessions();
      assertEquals(2, sessions2.size());

      // First session for user2

      WorkoutSession session2_1 = sessions2.get(0);
      assertEquals("Morning Run", session2_1.getName());
      assertEquals("CARDIO", session2_1.getType().toString());
      assertEquals("MEDIUM", session2_1.getIntensity().toString());

      // Assert WorkoutSessionExercise properties for first session
      List<WorkoutSessionExercise> exercises2_1 = session2_1.getExerciseList();
      assertEquals(2, exercises2_1.size());

      // First exercise in Moring Run
      WorkoutSessionExercise exercise2_1 = exercises2_1.get(0);
      assertEquals("Run", exercise2_1.getExerciseName());
      assertEquals("STAMINA", exercise2_1.getPurpose().toString());
      assertEquals("COMPOUND", exercise2_1.getMechanics().toString());
      assertEquals("PUSH", exercise2_1.getForce().toString());

      // Assert WorkoutSet properties for first exercise in first session
      List<WorkoutSet> runSets = exercise2_1.getWorkoutSets();
      assertEquals(1, runSets.size());
      WorkoutSet runSet = runSets.get(0);
      assertEquals(1, runSet.getSetNumber());
      assertEquals(0, runSet.getKilos());
      assertEquals(15, runSet.getReps());

      // Second exercise in Moring Run
      WorkoutSessionExercise exercise2_2 = exercises2_1.get(1);
      assertEquals("Pushups", exercise2_2.getExerciseName());
      assertEquals("CHEST", exercise2_2.getPurpose().toString());
      assertEquals("COMPOUND", exercise2_2.getMechanics().toString());
      assertEquals("PUSH", exercise2_2.getForce().toString());

      // Assert WorkoutSet properties for second exercise in first session
      List<WorkoutSet> pushupSets = exercise2_2.getWorkoutSets();
      assertEquals(2, pushupSets.size());

      WorkoutSet pushupSet1 = pushupSets.get(0);
      assertEquals(1, pushupSet1.getSetNumber());
      assertEquals(0, pushupSet1.getKilos());
      assertEquals(20, pushupSet1.getReps());
      
      WorkoutSet pushupSet2 = pushupSets.get(1);
      assertEquals(2, pushupSet2.getSetNumber());
      assertEquals(0, pushupSet2.getKilos());
      assertEquals(20, pushupSet2.getReps());


      // Second session for user2 (Evening Workout)
      WorkoutSession session2_2 = sessions2.get(1);
      assertEquals("Evening Workout", session2_2.getName());
      assertEquals("STRENGTH", session2_2.getType().toString());
      assertEquals("LOW", session2_2.getIntensity().toString());

      // Assert WorkoutSessionExercise properties for Evening Workout
      List<WorkoutSessionExercise> exercises2_2 = session2_2.getExerciseList();
      assertEquals(1, exercises2_2.size());  // One exercise: Squat

      // Exercise in Evening Workout (Squat)
      WorkoutSessionExercise squatExercise = exercises2_2.get(0);
      assertEquals("Squat", squatExercise.getExerciseName());
      assertEquals("LEGS", squatExercise.getPurpose().toString());
      assertEquals("COMPOUND", squatExercise.getMechanics().toString());
      assertEquals("PUSH", squatExercise.getForce().toString());

      // Assert WorkoutSet properties for Squat exercise
      List<WorkoutSet> squatSets = squatExercise.getWorkoutSets();
      assertEquals(3, squatSets.size());  // Three sets of squats

      WorkoutSet squatSet1 = squatSets.get(0);
      assertEquals(1, squatSet1.getSetNumber());
      assertEquals(120, squatSet1.getKilos());
      assertEquals(5, squatSet1.getReps());

      WorkoutSet squatSet2 = squatSets.get(1);
      assertEquals(2, squatSet2.getSetNumber());
      assertEquals(110, squatSet2.getKilos());
      assertEquals(6, squatSet2.getReps());

      WorkoutSet squatSet3 = squatSets.get(2);
      assertEquals(3, squatSet3.getSetNumber());
      assertEquals(100, squatSet3.getKilos());
      assertEquals(8, squatSet3.getReps());

    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Deserialization failed");
    }
  }
}
