package org.karthick.dietplanner.dietplan.dto;

import lombok.Getter;
import lombok.Setter;
import org.karthick.dietplanner.dietplan.enums.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DietPlanDTO {
  private UUID id;
  private int age;
  private Gender gender;
  private int height;
  private int weight;
  private Goal goal;
  private int finalGoal;
  private Duration duration;
  private Activity activity;
  private FoodType foodType;
  private List<String> foodFilters;
}
