package org.karthick.dietplanner.dietplan;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.dietplan.document.DietPlan;
import org.karthick.dietplanner.dietplan.dto.DietPlanDTO;
import org.karthick.dietplanner.dietplan.model.Meals;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("meals/{id}")
    public Meals getMeal(@PathVariable("id") UUID id) {
        return this.dietPlanService.getMeals(id);
    }
}

