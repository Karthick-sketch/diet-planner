package org.karthick.dietplanner.dietplan.repository;

import java.util.List;
import java.util.Optional;

import org.karthick.dietplanner.dietplan.dto.DietPlansHistoryDTO;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DietPlanRepository extends MongoRepository<DietPlan, String> {
  @Query(
      value = "{ userId:  ?0 }",
      fields = "{ _id: 1, title: 1, plan: 1, finalGoal: 1, duration: 1, active: 1 }",
      sort = "{ createdAt:  -1 }")
  List<DietPlansHistoryDTO> findDietPlansHistoryByUserId(String authenticatedUserId);

  List<DietPlan> findByUserId(String userId);

  Optional<DietPlan> findByIdAndUserId(String id, String userId);

  Optional<DietPlan> findByUserIdAndActiveIsTrue(String userId);

  Optional<DietPlan> findByActiveTrue();
}
