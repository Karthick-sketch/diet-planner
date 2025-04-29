package org.karthick.dietplanner.dietplan;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.karthick.dietplanner.dietplan.dto.DietPlanDTO;
import org.karthick.dietplanner.dietplan.dto.DietPlanListItemDTO;
import org.karthick.dietplanner.shared.model.MealKcal;
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
  public DietPlan createDietPlan(@RequestBody() DietPlanDTO dietPlanDTO) {
    return dietPlanService.createDietPlan(dietPlanDTO);
  }

  @GetMapping("meal-kcal/{id}")
  public MealKcal getMealKcal(@PathVariable("id") String id) {
    return this.dietPlanService.getMealKcal(id);
  }
}
