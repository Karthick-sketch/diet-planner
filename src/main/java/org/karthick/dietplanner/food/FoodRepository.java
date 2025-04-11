package org.karthick.dietplanner.food;

import org.karthick.dietplanner.food.document.Food;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface FoodRepository extends MongoRepository<Food, UUID> {}
