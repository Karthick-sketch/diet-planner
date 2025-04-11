package org.karthick.dietplanner.dietplan;

import org.karthick.dietplanner.dietplan.document.DietPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface DietPlanRepository extends MongoRepository<DietPlan, UUID> {
}
