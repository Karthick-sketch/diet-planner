package org.karthick.dietplanner.dietplan.dto;

import org.karthick.dietplanner.dietplan.enums.Plan;
import org.karthick.dietplanner.shared.model.TimePeriod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DietPlanListItemDTO {
  private String id;
  private String title;
  private Plan plan;
  private double finalGoal;
  private TimePeriod timePeriod;
}
