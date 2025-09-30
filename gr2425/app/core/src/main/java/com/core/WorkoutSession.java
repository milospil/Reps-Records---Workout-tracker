package com.core;

import com.core.enums.Intensity;
import com.core.enums.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Workout Session class. Defines what a workout session should look like.
 */
public class WorkoutSession {
  private String name;
  private Type type;
  private Intensity intensity;
  private LocalDateTime date;
  private LocalTime sessionDurationTime = LocalTime.of(0, 0, 0);

  private List<WorkoutSessionExercise> exerciseList = new ArrayList<>();

  /**
   * Constructor for a new Workout Session.

   * @param name workout name
   * @param type type of workout
   * @param intensity intensity of workout
   * @param date the current date.
   */
  public WorkoutSession(String name, Type type, Intensity intensity, LocalDateTime date) {
    this.name = name;
    this.type = type;
    this.intensity = intensity;
    this.date = date.truncatedTo(ChronoUnit.MINUTES);
  }

  /**
   * Defensive copy constructor for WorkoutSession.

   * @param workoutSession workout session object.
   */
  public WorkoutSession(WorkoutSession workoutSession) {
    this.name = workoutSession.name;
    this.type = workoutSession.type;
    this.intensity = workoutSession.intensity;
    this.date = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

    this.exerciseList = new ArrayList<>();
    for (WorkoutSessionExercise exercise : workoutSession.exerciseList) {
      WorkoutSessionExercise copyExercise = new WorkoutSessionExercise(exercise);
      exerciseList.add(copyExercise);
    }
  }

  /**
   * Empty constructor for serialization.
   */
  public WorkoutSession() {}

  public void setName(String name) {
    this.name = name;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void setIntensity(Intensity intensity) {
    this.intensity = intensity;
  }

  public void setExerciseList(List<WorkoutSessionExercise> exerciseList) {
    this.exerciseList = exerciseList;
  }

  public void setDate(LocalDateTime date) {
    this.date = date.truncatedTo(ChronoUnit.MINUTES);
  }

  public void setDurationTime(LocalTime duration) {
    this.sessionDurationTime = duration;
  }

  /**
   * Adds a workout exercise to the session.

   * @param exercise exercise to be added.
   */
  public void addExerciseToWorkout(WorkoutSessionExercise exercise) {
    exerciseList.add(exercise);
  }

  /**
   * Removes an exercise from the workout session.

   * @param exercise exercise
   * @throws NoSuchElementException if no such exercise exists.
   */
  public void removeExerciseFromWorkout(WorkoutSessionExercise exercise) {
    if (!exerciseList.contains(exercise)) {
      throw new NoSuchElementException("No such element");
    }

    exerciseList.remove(exercise);
  }

  public List<WorkoutSessionExercise> getExerciseList() {
    return new ArrayList<>(exerciseList);
  }

  public String getName() {
    return this.name;
  }

  public Type getType() {
    return this.type;
  }

  public Intensity getIntensity() {
    return this.intensity;
  }

  public LocalDateTime getDate() {
    return this.date;
  }

  public LocalTime getDurationTime() {
    return this.sessionDurationTime;
  }

  /**
   * Gets the session time for the current session.

   * @return total time a user has spent on the session.
   */
  public Duration getDuration() {
    LocalTime startTime = LocalTime.of(0, 0, 0);
    LocalTime nowTime = sessionDurationTime;

    Duration duration = Duration.between(startTime, nowTime);

    return duration;

  }
}
