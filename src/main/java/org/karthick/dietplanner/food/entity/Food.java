package org.karthick.dietplanner.food.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "foods")
public class Food {
  @Id private String id;
  private String name;
  private String servingSize;
  private double calories;
  private double gProtein;
  private double gFat;
  private double gCarbs;
  private String category;
  private String[] tags;
  private boolean isCustom;
}
