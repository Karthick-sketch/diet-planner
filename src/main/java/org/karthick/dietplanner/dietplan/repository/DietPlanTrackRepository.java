package org.karthick.dietplanner.dietplan.repository;

import org.karthick.dietplanner.dietplan.entity.DietPlanTrack;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DietPlanTrackRepository extends MongoRepository<DietPlanTrack, String> {
  Optional<DietPlanTrack> findByDateAndDietPlanId(String date, String dietPlanId);
}
