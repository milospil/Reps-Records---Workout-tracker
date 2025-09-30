package com.core;

import com.core.comparators.ExerciseComparator;
import com.core.comparators.WorkoutSessionComparator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User Class. A user has a username and password. A user also has an email and
 * a full name. A user
 * has a workout list that includes all the workout sessions and an exercise
 * database that can be
 * modified.
 */
public class User {
  private String username;
  private String password;
  private String eMail;
  private String fullName;

  private List<WorkoutSession> workoutList = new ArrayList<>();

  private List<Exercise> exerciseDatabase = new ArrayList<>();

  /**
   * Constructor initialization of a user object.

   * @param username         username.
   * @param password         password.
   * @param fullName         fullname.
   * @param eMail            email address,
   * @param exerciseDatabase exercise database.
   */
  public User(String username,
      String password, 
      String fullName, 
      String eMail,
      List<Exercise> exerciseDatabase) {
    this.username = username;
    this.password = password;
    this.eMail = eMail;
    this.fullName = fullName;
    this.exerciseDatabase = exerciseDatabase;
  }

  /**
   * Empty Constructor for Serialization.
   */
  public User() {
  }

  /**
   * Defensive copy constructor for User.

   * @param user a user object.
   */
  public User(User user) {
    this.username = user.username;
    this.password = user.password;
    this.eMail = user.eMail;
    this.fullName = user.fullName;
    this.workoutList = new ArrayList<WorkoutSession>(user.workoutList);
    this.exerciseDatabase = new ArrayList<Exercise>(user.exerciseDatabase);
  }

  /**
   * Collects workout data for last 30 days, makes and returns HashMap with date
   * as key and workout
   * duration as value.

   * @return workoutData object
   */
  public HashMap<LocalDate, Integer> getLast30DaysWorkouts() {
    HashMap<LocalDate, Integer> workoutData = new HashMap<>();
    LocalDate today = LocalDate.now();

    for (int i = 0; i < 30; i++) {
      LocalDate date = today.minusDays(i);
      workoutData.put(date, 0);
    }

    for (WorkoutSession session : workoutList) {
      LocalDate sessionDate = session.getDate().toLocalDate();
      Integer sessionDuration = (int) session.getDuration().toMinutes();

      if (workoutData.containsKey(sessionDate)) {
        workoutData.put(sessionDate, workoutData.get(sessionDate) + sessionDuration);
      }
    }
    return workoutData;
  }

  public void setExercises(List<Exercise> exerciseDatabase) {
    this.exerciseDatabase = exerciseDatabase;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Sets a user's password. Password must be a valid password to be set.

   * @param password valid password.
   */
  public void setPassword(String password) {
    if (!PasswordValidation.validatePassword(password)) {
      throw new IllegalArgumentException("Invalid password");
    }

    this.password = password;
  }

  /**
   * Setter method for eMail field.

   * @param eMail eMail.
   */
  public void setEMail(String eMail) {
    if (!EmailValidation.validateEmail(eMail)) {
      throw new IllegalArgumentException("Invalid Email!");
    }

    this.eMail = eMail;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * Method that adds exercises to a users exercise database.

   * @param exercise is an exercise object.
   * @throws IllegalArgumentException if the exercise already exists in the
   *                                  database
   * 
   */

  public void addExercise(Exercise exercise) throws IllegalArgumentException {
    ExerciseComparator comparator = new ExerciseComparator();

    for (Exercise existingExercise : exerciseDatabase) {
      if (comparator.compare(existingExercise, exercise) == 0) {
        throw new IllegalArgumentException("Exercise already exists in the database");
      }
    }

    exerciseDatabase.add(exercise);
  }

  /**
   * Method that adds Workout Sessions to the users database.

   * @param wsession workout session.
   * @throws IllegalArgumentException if an existing workout session has the same
   *                                  name
   */
  public void addSession(WorkoutSession wsession) throws IllegalArgumentException {
    WorkoutSessionComparator comparator = new WorkoutSessionComparator();

    for (WorkoutSession session : workoutList) {
      if (comparator.compare(session, wsession) == 0) {
        System.out.println(session.getDate());
        System.out.println(wsession.getDate());
        throw new IllegalArgumentException(
          "Workout Sessions can not have the same name, session1: " 
            + wsession.getDate() + " session2: " + session.getDate());
      }
    }

    workoutList.add(wsession);
  }

  public void setWorkoutList(List<WorkoutSession> workoutList) {
    this.workoutList = workoutList;
  }

  /**
   * Removes a workoutsession from the user's workout list.

   * @param wsession workout session
   */
  public void removeWorkout(WorkoutSession wsession) {
    workoutList.remove(wsession);
  }

  public List<WorkoutSession> getSessions() {
    return new ArrayList<WorkoutSession>(workoutList);
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return eMail;
  }

  public String getFullName() {
    return fullName;
  }

  public List<Exercise> getExerciseDatabase() {
    return new ArrayList<>(exerciseDatabase);
  }

  @Override
  public String toString() {
    return username;
  }
}
