package org.karthick.dietplanner.dietplan;

import java.util.List;
import lombok.AllArgsConstructor;
import org.karthick.dietplanner.dietplan.dto.DietPlanOverviewDTO;
import org.karthick.dietplanner.dietplan.dto.DietPlansHistoryDTO;
import org.karthick.dietplanner.dietplan.dto.MetricsDTO;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.karthick.dietplanner.dietplan.entity.DietPlanTrack;
import org.karthick.dietplanner.exception.BadRequestException;
import org.karthick.dietplanner.shared.model.Macros;
import org.karthick.dietplanner.shared.model.MealKcal;
import org.karthick.dietplanner.util.constants.MacrosConstants;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/diet-plan")
public class DietPlanController {

  private final DietPlanService dietPlanService;

  @GetMapping("/all")
  public List<DietPlan> getAllDietPlans() {
    return dietPlanService.findAllDietPlans();
  }

  @GetMapping("/history")
  public List<DietPlansHistoryDTO> getDietPlansHistory() {
    return dietPlanService.findDietPlansHistory();
  }

  @GetMapping
  public DietPlan getDietPlan() {
    return dietPlanService.findDietPlanByUserId();
  }

  @PostMapping
  public DietPlan createDietPlan(@RequestBody DietPlan dietPlan) {
    return dietPlanService.createDietPlan(dietPlan);
  }

  @PostMapping("/add-weight/{dietPlanId}")
  public DietPlanTrack addWeight(
    @PathVariable String dietPlanId,
    @RequestBody double weight
  ) {
    return dietPlanService.addWeight(dietPlanId, weight);
  }

  @GetMapping("/track/{dietPlanTrackId}")
  public DietPlanTrack getDietPlanTrackById(
    @PathVariable @NonNull String dietPlanTrackId
  ) {
    return dietPlanService.findDietPlanTrackById(dietPlanTrackId);
  }

  @GetMapping("/track/plan/{dietPlanId}")
  public DietPlanTrack getDietPlanTrackByDietPlanId(
    @PathVariable String dietPlanId
  ) {
    return dietPlanService.findDietPlanTrackByDietPlanId(dietPlanId);
  }

  @PostMapping("/{category}/{dietPlanId}")
  public DietPlanTrack updateMacros(
    @PathVariable String category,
    @PathVariable String dietPlanId,
    @RequestBody Macros macros
  ) {
    if (MacrosConstants.validateMacro(category)) {
      return dietPlanService.updateMacros(dietPlanId, category, macros);
    }
    throw new BadRequestException("Invalid macro category");
  }

  @GetMapping("/meal-kcal/{id}")
  public MealKcal getMealKcal(@PathVariable("id") String id) {
    return this.dietPlanService.getMealKcal(id);
  }

  @GetMapping("/metrics")
  public MetricsDTO getMetricsByDateRange() {
    return dietPlanService.getMetricsByDateRange();
  }

  @GetMapping("/active-plans")
  public boolean isThereAnyActivePlans() {
    return dietPlanService.isThereAnyActivePlans();
  }

  @GetMapping("/overview/{dietPlanId}")
  public List<DietPlanOverviewDTO> getDietPlansOverview(
    @PathVariable String dietPlanId
  ) {
    return dietPlanService.getDietPlansOverview(dietPlanId);
  }

  @GetMapping("/status")
  public boolean isDietPlanReachedDuration() {
    return dietPlanService.isDietPlanReachedDuration();
  }
}
