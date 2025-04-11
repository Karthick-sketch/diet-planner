package org.karthick.dietplanner.food;

import org.karthick.dietplanner.food.entity.Food;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepository extends MongoRepository<Food, String> {}
