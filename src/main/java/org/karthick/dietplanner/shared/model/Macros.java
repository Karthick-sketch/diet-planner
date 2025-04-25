package org.karthick.dietplanner.shared.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Macros {
  private double protein;
  private double fat;
  private double carbs;
}
