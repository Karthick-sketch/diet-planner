package org.karthick.dietplanner.aifoodsuggest.repository;

import org.karthick.dietplanner.aifoodsuggest.entity.AIFoodSuggest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AIFoodSuggestRepository extends MongoRepository<AIFoodSuggest, String> {}
