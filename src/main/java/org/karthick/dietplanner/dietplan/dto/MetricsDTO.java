package org.karthick.dietplanner.dietplan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MetricsDTO {
  private String title;
  private double taken;
  private double currentWeight;
  private double deficit;
  private double protein;
  private double carbs;
  private double fat;
  private List<String> days;
  private List<Double> weights;
}
