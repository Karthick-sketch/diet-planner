package org.karthick.dietplanner.aifoodsuggest.entity;

import lombok.Getter;
import lombok.Setter;
import org.karthick.dietplanner.shared.model.Calories;
import org.karthick.dietplanner.shared.model.Macros;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "ai_food_suggests")
public class AIFoodSuggest {
  private @Id String id;
  private String name;
  private Calories calories;
  private Macros macronutrients;
  private String description;
}
