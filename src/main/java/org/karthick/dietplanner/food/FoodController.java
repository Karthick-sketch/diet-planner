package org.karthick.dietplanner.food;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.food.entity.Food;
import org.karthick.dietplanner.shared.model.Meals;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/food")
public class FoodController {
  private FoodService foodService;

  @GetMapping
  public List<Food> getAllFoods() {
    return foodService.findAllFoods();
  }

  @PostMapping
  public Food createFood(@RequestBody() Food food) {
    return foodService.createFood(food);
  }

  @GetMapping("/suggest/{dietPlanId}")
  public Meals suggestFood(@PathVariable String dietPlanId) {
    return foodService.suggestMeals(dietPlanId);
  }
}
