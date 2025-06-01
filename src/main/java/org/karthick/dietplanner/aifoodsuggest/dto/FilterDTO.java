package org.karthick.dietplanner.aifoodsuggest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterDTO {
  private String mealFilter;
  private String foodFilter;
  private String cuisineFilter;
}
