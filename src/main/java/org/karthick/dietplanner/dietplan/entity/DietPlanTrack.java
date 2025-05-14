package org.karthick.dietplanner.dietplan.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.karthick.dietplanner.shared.model.Calories;
import org.karthick.dietplanner.shared.model.MealKcal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "diet_plan_tracks")
public class DietPlanTrack {
  @Id private String id;
  private double weight;
  private double tdee;
  private Calories deficit;
  private Calories protein;
  private Calories carbs;
  private Calories fat;
  private MealKcal mealKcal;
  private String date;
  private String dietPlanId;

  public DietPlanTrack(double weight, String date, String dietPlanId) {
    this.weight = weight;
    this.date = date;
    this.dietPlanId = dietPlanId;
  }
}
