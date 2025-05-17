package org.karthick.dietplanner.dietplan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MetricsDTO {
  private List<String> days;
  private List<Double> weights;

  public MetricsDTO() {
    this.days = new ArrayList<>();
    this.weights = new ArrayList<>();
  }
}
