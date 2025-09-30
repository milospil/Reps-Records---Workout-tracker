package com.core;

import com.core.enums.Force;
import com.core.enums.Mechanics;
import com.core.enums.Purpose;

/**
 * Defines what an exercise should look like.
 */
public class Exercise {

  private String exerciseName;
  private Purpose purpose;
  private Mechanics mechanics;
  private Force force;

  /**
   * Constructor for a new exercise object.

   * @param exerciseName name of exercise.
   * @param purpose what muscle the exercise targets.
   * @param mechanics If the exercise is isolating or compound.
   * @param force If the exercise is push, pull or static.
   */
  public Exercise(String exerciseName, 
      Purpose purpose, 
      Mechanics mechanics, 
      Force force) {
    this.exerciseName = exerciseName;
    this.purpose = purpose;
    this.mechanics = mechanics;
    this.force = force;
  }

  /**
   * Empty constructor for serialization.
   */
  public Exercise() {}

  public void setExerciseName(String exerciseName) {
    this.exerciseName = exerciseName;
  }

  public void setPurpose(Purpose purpose) {
    this.purpose = purpose;
  }

  public void setMechanics(Mechanics mechanics) {
    this.mechanics = mechanics;
  }

  public void setForce(Force force) {
    this.force = force;
  }

  public String getExerciseName() {
    return exerciseName;
  }

  public String getMuscle() {
    return purpose.name();
  }

  public Purpose getPurpose() {
    return purpose;
  }

  public Mechanics getMechanics() {
    return mechanics;
  }

  public Force getForce() {
    return force;
  }

}
