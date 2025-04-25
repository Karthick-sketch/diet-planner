package org.karthick.dietplanner.shared.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.karthick.dietplanner.food.entity.Food;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MacroFoodItems {
  private List<Food> proteins;
  private List<Food> fats;
  private List<Food> carbs;
}
