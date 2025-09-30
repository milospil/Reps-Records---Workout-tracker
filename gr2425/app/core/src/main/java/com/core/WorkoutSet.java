package com.core;

/**
 * WorkoutSet Class. A workout set has a setNumber, amount of kilos lifted and the amount of reps.
 */
public class WorkoutSet {

  private int setNumber = 0;
  private int kilosLifted;
  private int reps;

  /**
   * Constructor for instantiating a new workout set.

   * @param kilosLifted kilos.
   * @param reps reps.
   */
  public WorkoutSet(int kilosLifted, int reps) {
    this.kilosLifted = kilosLifted;
    this.reps = reps;
  }

  /**
   * Empty constructor for serialization.
   */
  public WorkoutSet() {}

  public void setSetNumber(int setNumber) {
    this.setNumber = setNumber;
  }

  public void setKilosLifted(int kilosLifted) {
    this.kilosLifted = kilosLifted;
  }

  public void setReps(int reps) {
    this.reps = reps;
  }

  public int getKilos() {
    return kilosLifted;
  }

  public int getReps() {
    return reps;
  }

  public int getSetNumber() {
    return setNumber;
  }
}
