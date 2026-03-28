package org.karthick.dietplanner.dietplan.dto;

import lombok.Getter;
import lombok.Setter;
import org.karthick.dietplanner.dietplan.enums.Plan;

@Getter
@Setter
public class DietPlansHistoryDTO {

  private String id;
  private String title;
  private Plan plan;
  private double weight;
  private double targetWeight;
  private int duration;
  private boolean active;
}
