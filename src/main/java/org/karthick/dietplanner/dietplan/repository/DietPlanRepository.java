package org.karthick.dietplanner.dietplan.repository;

import java.util.List;

import org.karthick.dietplanner.dietplan.dto.DietPlanListItemDTO;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DietPlanRepository extends MongoRepository<DietPlan, String> {
    @Query("{}, { _id: 1, title: 1, description: 1, deficit: 1}")
    List<DietPlanListItemDTO> findAllDietPlanList();
}
