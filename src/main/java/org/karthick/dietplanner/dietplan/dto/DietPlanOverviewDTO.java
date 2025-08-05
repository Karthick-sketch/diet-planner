package org.karthick.dietplanner.dietplan.dto;

import lombok.Getter;
import lombok.Setter;
import org.karthick.dietplanner.shared.model.Calories;

import java.time.LocalDate;

@Getter
@Setter
public class DietPlanOverviewDTO {
  private String id;
  private double weight;
  private double tdee;
  private Calories intake;
  private Calories protein;
  private Calories carbs;
  private Calories fat;
  private LocalDate createdAt;
}
