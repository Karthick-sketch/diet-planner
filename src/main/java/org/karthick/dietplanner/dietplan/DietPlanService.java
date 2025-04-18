package org.karthick.dietplanner.dietplan;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.config.ModelMapperConfig;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.karthick.dietplanner.dietplan.dto.DietPlanDTO;
import org.karthick.dietplanner.exception.EntityNotFoundException;
import org.karthick.dietplanner.dietplan.model.Macros;
import org.karthick.dietplanner.dietplan.model.Meals;
import org.karthick.dietplanner.util.CaloriesCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DietPlanService {
  private DietPlanRepository dietPlannerRepository;
  private ModelMapperConfig mapper;

  public List<DietPlan> findAllDietPlans() {
    return dietPlannerRepository.findAll();
  }

  public DietPlan createDietPlan(DietPlanDTO dietPlanDTO) {
    return dietPlannerRepository.save(this.processDietPlan(dietPlanDTO));
  }

  private DietPlan processDietPlan(DietPlanDTO dietPlanDTO) {
    DietPlan dietPlan = mapper.convertToEntity(dietPlanDTO, DietPlan.class);
    dietPlan.setTdee(
        CaloriesCalculator.findTDEE(
            dietPlanDTO.getAge(),
            dietPlan.getWeight(),
            dietPlan.getHeight(),
            dietPlan.getActivity()));
    dietPlan.setDeficit(
        CaloriesCalculator.kcalPercentage(dietPlan.getTdee(), dietPlanDTO.getGoal().getValue()));
    dietPlan.setProtein(CaloriesCalculator.calcProtein(dietPlan.getDeficit()));
    dietPlan.setFat(CaloriesCalculator.calcFat(dietPlan.getDeficit()));
    dietPlan.setCarbs(CaloriesCalculator.calcCarbs(dietPlan.getDeficit()));
    return dietPlan;
  }

  public Meals getMeals(String id) {
    Optional<DietPlan> dietPlan = this.dietPlannerRepository.findById(id);
    if (dietPlan.isPresent()) {
      return splitMeals(dietPlan.get());
    } else {
      throw new EntityNotFoundException("No macros found.");
    }
  }

  private Meals splitMeals(DietPlan dietPlan) {
    Macros macros = new Macros(dietPlan.getProtein(), dietPlan.getFat(), dietPlan.getCarbs());
    return new Meals(
        CaloriesCalculator.macrosPercentage(macros, 30),
        CaloriesCalculator.macrosPercentage(macros, 30),
        CaloriesCalculator.macrosPercentage(macros, 15),
        CaloriesCalculator.macrosPercentage(macros, 25));
  }
}
