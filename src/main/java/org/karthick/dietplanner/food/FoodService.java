package org.karthick.dietplanner.food;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.dietplan.DietPlanService;
import org.karthick.dietplanner.food.entity.Food;
import org.karthick.dietplanner.shared.model.MacroFoodItems;
import org.karthick.dietplanner.shared.model.Macros;
import org.karthick.dietplanner.shared.model.MealKcal;
import org.karthick.dietplanner.shared.model.Meals;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class FoodService {
  private FoodRepository foodRepository;
  private DietPlanService dietPlanService;

  public List<Food> findAllFoods() {
    return foodRepository.findAll();
  }

  public List<Food> findFoodsByCategory(String category) {
    return foodRepository.findFoodsByCategory(category);
  }

  public Food createFood(Food food) {
    return foodRepository.save(food);
  }

  public Meals suggestMeals(String dietPlanId) {
    MealKcal mealKcal = dietPlanService.getMealKcal(dietPlanId);
    MacroFoodItems foodItems = getMacroFoodItems();
    Macros macros = mealKcal.getBreakfast();
    Meals meals = new Meals();
    double p = 0, f = 0, c = 0;
    List<String> foodNames = new ArrayList<>();
    while (macros.getProtein().getTotal() > 0
        || macros.getCarbs().getTotal() > 0
        || macros.getFat().getTotal() > 0) {
      for (Food food : foodItems.getProteins()) {
        if (food.getGProtein() + p < mealKcal.getBreakfast().getProtein().getTotal()) {
          foodNames.add(food.getName());
          p += food.getGProtein();
          f += food.getGFat();
          c += food.getGCarbs();
        }
      }
      for (Food food : foodItems.getFats()) {
        if (food.getGFat() + f < mealKcal.getBreakfast().getFat().getTotal()) {
          foodNames.add(food.getName());
          p += food.getGProtein();
          f += food.getGFat();
          c += food.getGCarbs();
        }
      }
      for (Food food : foodItems.getCarbs()) {
        if (food.getGCarbs() + c < mealKcal.getBreakfast().getCarbs().getTotal()) {
          foodNames.add(food.getName());
          p += food.getGProtein();
          f += food.getGFat();
          c += food.getGCarbs();
        }
      }
    }
    meals.setBreakfast(foodNames);
    meals.setLunch(foodNames);
    meals.setDinner(foodNames);
    return meals;
  }

  @SuppressWarnings("unused")
  private double adjustServing(Food food, Macros targetMacros) {
    return Math.min(
        Math.min(
            getTarget(food.getGProtein(), targetMacros.getProtein().getTotal()),
            getTarget(food.getGCarbs(), targetMacros.getCarbs().getTotal())),
        Math.min(getTarget(food.getGFat(), targetMacros.getFat().getTotal()), 1.0));
  }

  private double getTarget(double actual, double target) {
    return actual > 0 ? target / actual : Double.POSITIVE_INFINITY;
  }

  private MacroFoodItems getMacroFoodItems() {
    return new MacroFoodItems(
        findFoodsByCategory("protein"),
        findFoodsByCategory("fat"),
        findFoodsByCategory("carbohydrate"));
  }
}
