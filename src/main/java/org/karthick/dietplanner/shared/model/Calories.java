package org.karthick.dietplanner.shared.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Calories {
  private double taken;
  private double total;

  public Calories(double total) {
    this.total = total;
  }

  public void addTaken(double taken) {
    this.taken += taken;
  }
}
