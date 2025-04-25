package org.karthick.dietplanner.shared.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealKcal {
  private Macros breakfast;
  public Macros lunch;
  private Macros snack;
  private Macros dinner;
}
