package org.karthick.dietplanner.dietplan.dto;

import lombok.Getter;
import lombok.Setter;
import org.karthick.dietplanner.dietplan.enums.*;
import org.karthick.dietplanner.shared.model.TimePeriod;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DietPlanDTO {
  private UUID id;
  private String title;
  private String description;
  private int age;
  private Gender gender;
  private int height;
  private int weight;
  private Goal goal;
  private int finalGoal;
  private TimePeriod timePeriod;
  private Activity activity;
  private FoodType foodType;
  private List<String> foodFilters;
}
