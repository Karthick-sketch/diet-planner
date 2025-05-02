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
  private MealKcal mealKcal;
  private String date;
  private String dietPlanId;

  public DietPlanTrack(DietPlan dietPlan, MealKcal mealKcal, String date) {
    this.weight = dietPlan.getWeight();
    this.tdee = dietPlan.getTdee();
    this.deficit = new Calories(dietPlan.getDeficit());
    this.mealKcal = mealKcal;
    this.date = date;
    this.dietPlanId = dietPlan.getId();
  }
}
