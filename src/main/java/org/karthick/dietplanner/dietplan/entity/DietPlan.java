package org.karthick.dietplanner.dietplan.entity;

import lombok.Getter;
import lombok.Setter;
import org.karthick.dietplanner.dietplan.enums.*;
import org.karthick.dietplanner.shared.model.TimePeriod;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "diet_plans")
public class DietPlan {
  @Id private String id;
  private String title;
  private Plan plan;
  private int age;
  private Gender gender;
  private double height;
  private double weight;
  private double todayWeight;
  private Goal goal;
  private double finalGoal;
  private TimePeriod timePeriod;
  private Activity activity;
  private String userId;
  private boolean active;
}
