package org.karthick.dietplanner.dietplan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MetricsDTO {
  private String title;
  private double taken;
  private int duration;
  private int currentDay;
  private double currentWeight;
  private double intake;
  private double protein;
  private double carbs;
  private double fat;
  private List<String> days;
  private List<Double> weights;
}
