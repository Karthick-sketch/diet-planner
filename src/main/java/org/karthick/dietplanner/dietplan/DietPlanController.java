package org.karthick.dietplanner.dietplan;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.karthick.dietplanner.dietplan.dto.DietPlanDTO;
import org.karthick.dietplanner.shared.model.MealKcal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/diet-planner")
public class DietPlanController {
  private DietPlanService dietPlanService;

  @GetMapping
  public List<DietPlan> getAllDietPlans() {
    return dietPlanService.findAllDietPlans();
  }

  @PostMapping
  public DietPlan createDietPlan(@RequestBody() DietPlanDTO dietPlanDTO) {
    return dietPlanService.createDietPlan(dietPlanDTO);
  }

  @GetMapping("meal-kcal/{id}")
  public MealKcal getMealKcal(@PathVariable("id") String id) {
    return this.dietPlanService.getMealKcal(id);
  }
}
