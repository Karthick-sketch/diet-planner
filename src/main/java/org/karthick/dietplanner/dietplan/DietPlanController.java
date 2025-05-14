package org.karthick.dietplanner.dietplan;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.karthick.dietplanner.dietplan.dto.DietPlanListItemDTO;
import org.karthick.dietplanner.dietplan.entity.DietPlanTrack;
import org.karthick.dietplanner.exception.BadRequestException;
import org.karthick.dietplanner.shared.model.Macros;
import org.karthick.dietplanner.shared.model.MealKcal;
import org.karthick.dietplanner.util.constants.MacrosConstants;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/diet-plan")
public class DietPlanController {
  private DietPlanService dietPlanService;

  @GetMapping("/all")
  public List<DietPlan> getAllDietPlans() {
    return dietPlanService.findAllDietPlans();
  }

  @GetMapping("/list")
  public List<DietPlanListItemDTO> getAllDietPlanList() {
    return dietPlanService.findAllDietPlanList();
  }

  @GetMapping("/{id}")
  public DietPlan getDietPlan(@PathVariable String id) {
    return dietPlanService.findDietPlanById(id);
  }

  @PostMapping
  public DietPlan createDietPlan(@RequestBody() DietPlan dietPlan) {
    return dietPlanService.createDietPlan(dietPlan);
  }

  @PostMapping("/track-weight/{dietPlanId}")
  public DietPlanTrack trackWeight(@PathVariable String dietPlanId, @RequestBody double weight) {
    return dietPlanService.trackWeight(dietPlanId, weight);
  }

  @GetMapping("/track/{dietPlanId}")
  public DietPlanTrack getDietPlanTrack(@PathVariable String dietPlanId) {
    return dietPlanService.findDietPlanTrackByDietPlanId(dietPlanId);
  }

  @PostMapping("/{category}/{dietPlanId}")
  public DietPlanTrack addMacros(@PathVariable String category, @PathVariable String dietPlanId, @RequestBody Macros macros) {
    if (MacrosConstants.validateMacro(category)) {
      return dietPlanService.addMacros(dietPlanId, category, macros);
    }
    throw new BadRequestException("Invalid macro category");
  }

  @GetMapping("/meal-kcal/{id}")
  public MealKcal getMealKcal(@PathVariable("id") String id) {
    return this.dietPlanService.getMealKcal(id);
  }
}
