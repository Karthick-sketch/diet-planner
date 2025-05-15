package org.karthick.dietplanner.dietplan;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.karthick.dietplanner.dietplan.entity.DietPlanTrack;
import org.karthick.dietplanner.dietplan.repository.DietPlanRepository;
import org.karthick.dietplanner.dietplan.repository.DietPlanTrackRepository;
import org.karthick.dietplanner.dietplan.dto.DietPlanListItemDTO;
import org.karthick.dietplanner.exception.EntityNotFoundException;
import org.karthick.dietplanner.shared.model.Calories;
import org.karthick.dietplanner.shared.model.Macros;
import org.karthick.dietplanner.shared.model.MealKcal;
import org.karthick.dietplanner.util.CaloriesCalculator;
import org.karthick.dietplanner.util.constants.MacrosConstants;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DietPlanService {
  private DietPlanRepository dietPlannerRepository;
  private DietPlanTrackRepository dietPlanTrackRepository;

  public List<DietPlan> findAllDietPlans() {
    return dietPlannerRepository.findAll();
  }

  public List<DietPlanListItemDTO> findAllDietPlanList() {
    return dietPlannerRepository.findAllDietPlanList();
  }

  public DietPlan findDietPlanById(String id) {
    Optional<DietPlan> dietPlan = dietPlannerRepository.findById(id);
    if (dietPlan.isEmpty()) {
      throw new EntityNotFoundException("The Diet plan with the ID of '" + id + "' is not found");
    }
    return dietPlan.get();
  }

  public DietPlan createDietPlan(DietPlan dietPlan) {
    dietPlan.setTodayWeight(dietPlan.getWeight());
    dietPlannerRepository.save(dietPlan);
    createDietPlanTrack(dietPlan);
    return dietPlan;
  }

  private DietPlanTrack processDietPlan(DietPlan dietPlan, DietPlanTrack dietPlanTrack) {
    dietPlanTrack.setTdee(
        CaloriesCalculator.findTDEE(
            dietPlan.getAge(),
            dietPlanTrack.getWeight(),
            dietPlan.getHeight(),
            dietPlan.getActivity()));
    double deficit =
        CaloriesCalculator.calcDeficit(dietPlanTrack.getTdee(), dietPlan.getGoal().getValue());
    dietPlanTrack.setDeficit(new Calories(deficit));
    dietPlanTrack.setProtein(new Calories(CaloriesCalculator.calcProtein(deficit)));
    dietPlanTrack.setFat(new Calories(CaloriesCalculator.calcFat(deficit)));
    dietPlanTrack.setCarbs(new Calories(CaloriesCalculator.calcCarbs(deficit)));
    dietPlanTrack.setMealKcal(splitForMealKcal(dietPlanTrack));
    return dietPlanTrack;
  }

  public DietPlanTrack createDietPlanTrack(DietPlan dietPlan) {
    return dietPlanTrackRepository.save(
        processDietPlan(
            dietPlan, new DietPlanTrack(dietPlan.getTodayWeight(), getToday(), dietPlan.getId())));
  }

  public DietPlanTrack findDietPlanTrackByDietPlanId(String dietPlanId) {
    String today = getToday();
    DietPlan dietPlan = findDietPlanById(dietPlanId);
    return dietPlanTrackRepository
        .findByDateAndDietPlanId(today, dietPlanId)
        .orElseGet(() -> createDietPlanTrack(dietPlan));
  }

  public DietPlanTrack addWeight(String dietPlanId, double weight) {
    DietPlan dietPlan = findDietPlanById(dietPlanId);
    dietPlan.setTodayWeight(weight);
    dietPlannerRepository.save(dietPlan);
    DietPlanTrack dietPlanTrack = findDietPlanTrackByDietPlanId(dietPlanId);
    dietPlanTrack.setWeight(weight);
    return dietPlanTrackRepository.save(processDietPlan(dietPlan, dietPlanTrack));
  }

  private String getToday() {
    return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
  }

  public DietPlanTrack updateMacros(String dietPlanId, String category, Macros macros) {
    DietPlanTrack dietPlanTrack = findDietPlanTrackByDietPlanId(dietPlanId);
    dietPlanTrack.getDeficit().setTaken(CaloriesCalculator.calcDeficit(macros));
    Macros dietPlanMacros = getMacrosByCategory(dietPlanTrack.getMealKcal(), category);
    dietPlanMacros.getProtein().setTaken(macros.getProtein().getTaken());
    dietPlanMacros.getFat().setTaken(macros.getFat().getTaken());
    dietPlanMacros.getCarbs().setTaken(macros.getCarbs().getTaken());
    adjustMacros(dietPlanTrack);
    return dietPlanTrackRepository.save(dietPlanTrack);
  }

  private void adjustMacros(DietPlanTrack dietPlanTrack) {
    MealKcal mealKcal = dietPlanTrack.getMealKcal();
    dietPlanTrack
        .getProtein()
        .setTaken(
            mealKcal.getBreakfast().getProtein().getTaken()
                + mealKcal.getLunch().getProtein().getTaken()
                + mealKcal.getDinner().getProtein().getTaken()
                + mealKcal.getSnack().getProtein().getTaken());
    dietPlanTrack
        .getCarbs()
        .setTaken(
            mealKcal.getBreakfast().getCarbs().getTaken()
                + mealKcal.getLunch().getCarbs().getTaken()
                + mealKcal.getDinner().getCarbs().getTaken()
                + mealKcal.getSnack().getCarbs().getTaken());
    dietPlanTrack
        .getFat()
        .setTaken(
            mealKcal.getBreakfast().getFat().getTaken()
                + mealKcal.getLunch().getFat().getTaken()
                + mealKcal.getDinner().getFat().getTaken()
                + mealKcal.getSnack().getFat().getTaken());
  }

  private Macros getMacrosByCategory(MealKcal mealKcal, String category) {
    return switch (category) {
      case MacrosConstants.BREAKFAST -> mealKcal.getBreakfast();
      case MacrosConstants.LUNCH -> mealKcal.getLunch();
      case MacrosConstants.DINNER -> mealKcal.getDinner();
      default -> mealKcal.getSnack();
    };
  }

  public MealKcal getMealKcal(String dietPlanId) {
    Optional<DietPlanTrack> dietPlanTrack =
        dietPlanTrackRepository.findByDateAndDietPlanId(getToday(), dietPlanId);
    if (dietPlanTrack.isEmpty()) {
      throw new EntityNotFoundException("No macros found.");
    }
    return splitForMealKcal(dietPlanTrack.get());
  }

  private MealKcal splitForMealKcal(DietPlanTrack dietPlanTrack) {
    Macros macros =
        new Macros(dietPlanTrack.getProtein(), dietPlanTrack.getFat(), dietPlanTrack.getCarbs());
    return new MealKcal(
        CaloriesCalculator.macrosPercentage(macros, 30),
        CaloriesCalculator.macrosPercentage(macros, 30),
        CaloriesCalculator.macrosPercentage(macros, 15),
        CaloriesCalculator.macrosPercentage(macros, 25));
  }
}
