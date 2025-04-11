package org.karthick.dietplanner.dietplan;

import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DietPlanRepository extends MongoRepository<DietPlan, String> {}
