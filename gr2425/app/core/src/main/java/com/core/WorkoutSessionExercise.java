package com.core;

import com.core.enums.Force;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.json.internal.WorkoutSetSerializer;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Inheritance extension of Exercise class.
 * Has warmup sets and workout sets.
 */
public class WorkoutSessionExercise extends Exercise {

  @JsonSerialize(using = WorkoutSetSerializer.class)
  private List<WorkoutSet> workoutSets = new ArrayList<>();

  @JsonSerialize(using = WorkoutSetSerializer.class)
  private List<WorkoutSet> warmupSets = new ArrayList<>();

  /**
   * Constructor for creating a new workout session exercise.

   * @param exerciseName name.
   * @param purpose purpose.
   * @param mechanics mechanics.
   * @param force force.
   */
  public WorkoutSessionExercise(String exerciseName, 
      Purpose purpose, 
      Mechanics mechanics, 
      Force force) {
    super(exerciseName, purpose, mechanics, force);
  }

  /**
   * Creates a new WorkoutSessionExercise.

   * @param exercise exercise.
   */
  public WorkoutSessionExercise(Exercise exercise) {
    super(exercise.getExerciseName(), 
          exercise.getPurpose(), 
          exercise.getMechanics(), 
          exercise.getForce());
  }

  /**
   * Empty constructor for serialization.
   */
  public WorkoutSessionExercise() {}

  /**
   * Sets the workout sets for an exercise.

   * @param workoutSets list of workout sets.
   */
  public void setWorkoutSets(List<WorkoutSet> workoutSets) {
    for (WorkoutSet set : workoutSets) {
      set.setSetNumber(workoutSets.size() + 1);
    }
    this.workoutSets = workoutSets;
  }

  public List<WorkoutSet> getWorkoutSets() {
    return workoutSets;
  }

  /**
   * Adds a set to the exercise.

   * @param workoutSet workout set.
   */
  public void addWorkoutSet(WorkoutSet workoutSet) {
    workoutSet.setSetNumber(workoutSets.size() + 1);
    workoutSets.add(workoutSet);
  }

  /**
   * Removes a workout set from the exercise.

   * @param workoutSet workout set
   * @throws NoSuchElementException if no such workout set exists.
   */
  public void removeWorkoutSet(WorkoutSet workoutSet) {
    if (!workoutSets.contains(workoutSet)) {
      throw new NoSuchElementException("No such workoutSet");
    }

    workoutSets.remove(workoutSet);
  }

  /**
   * Sets the list of warmup sets.

   * @param warmupSets list of warmup sets.
   */
  public void setWarmupSets(List<WorkoutSet> warmupSets) {
    for (WorkoutSet set : warmupSets) {
      set.setSetNumber(warmupSets.size() + 1);
    }
    this.warmupSets = warmupSets;
  }

  public List<WorkoutSet> getWarmupSets() {
    return warmupSets;
  }

  /**
   * Adds a warmup set to the exercise.

   * @param warmupSet warmup set.
   */
  public void addWarmupSet(WorkoutSet warmupSet) {
    warmupSet.setSetNumber(warmupSets.size() + 1);
    warmupSets.add(warmupSet);
  }

  /**
   * Removes a warmup set from the exercise.

   * @param warmupSet warmup set.
   * @throws NoSuchElementException if no such warmup set exists.
   */
  public void removeWarmuptSet(WorkoutSet warmupSet) {
    if (!warmupSets.contains(warmupSet)) {
      throw new NoSuchElementException("No such warmupSet");
    }

    warmupSets.remove(warmupSet);
  }
}
