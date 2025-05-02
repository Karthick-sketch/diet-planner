package org.karthick.dietplanner.food;

import org.karthick.dietplanner.food.entity.Food;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FoodRepository extends MongoRepository<Food, String> {
  List<Food> findFoodsByCategory(String category);
}
