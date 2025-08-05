package org.karthick.dietplanner.dietplan.dto;

import org.karthick.dietplanner.dietplan.enums.Plan;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DietPlansHistoryDTO {
  private String id;
  private String title;
  private Plan plan;
  private double finalGoal;
  private int duration;
  private boolean active;
}
