package org.karthick.dietplanner.dietplan.repository;

import org.karthick.dietplanner.dietplan.entity.DietPlanTrack;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DietPlanTrackRepository extends MongoRepository<DietPlanTrack, String> {
}
