package org.karthick.dietplanner.dietplan.repository;

import org.karthick.dietplanner.dietplan.dto.DietPlanOverviewDTO;
import org.karthick.dietplanner.dietplan.entity.DietPlanTrack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DietPlanTrackRepository extends MongoRepository<DietPlanTrack, String> {
  Optional<DietPlanTrack> findByDateAndDietPlanId(LocalDate date, String dietPlanId);

  @Query(value = "{ date:  { $gte: ?0, $lte: ?1 }, dietPlanId: ?2 }", sort = "{ date: 1 }")
  List<DietPlanTrack> findByDateRangeAndDietPlanId(Date from, Date to, String dietPlanId);

  @Query(value = "{ dietPlanId: ?0 }", fields = "{ _id:  1, weight:  1, tdee: 1, deficit:  1, protein:  1, carbs:  1, fat:  1, date:  1 }", sort = "{ date: -1 }")
  List<DietPlanOverviewDTO> findAllDietPlanOverview(String dietPlanId);
}
