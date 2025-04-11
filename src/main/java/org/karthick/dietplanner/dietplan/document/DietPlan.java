package org.karthick.dietplanner.dietplan.document;

import lombok.Getter;
import lombok.Setter;
import org.karthick.dietplanner.dietplan.enums.*;
import org.karthick.dietplanner.dietplan.model.TimePeriod;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "diet_plans")
public class DietPlan {
  @Id private UUID id;
  private int age;
  private Gender gender;
  private double height;
  private double weight;
  private Goal goal;
  private double finalGoal;
  private TimePeriod timePeriod;
  private Activity activity;
  private FoodType foodType;
  private List<String> foodFilters;
  private double tdee;
  private double deficit;
  private double protein;
  private double fat;
  private double carbs;

  public DietPlan() {
    this.id = UUID.randomUUID();
  }
}
