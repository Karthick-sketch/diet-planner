package org.karthick.dietplanner.dietplan;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.config.ModelMapperConfig;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.karthick.dietplanner.dietplan.entity.DietPlanTrack;
import org.karthick.dietplanner.dietplan.repository.DietPlanRepository;
import org.karthick.dietplanner.dietplan.repository.DietPlanTrackRepository;
import org.karthick.dietplanner.dietplan.dto.DietPlanDTO;
import org.karthick.dietplanner.dietplan.dto.DietPlanListItemDTO;
import org.karthick.dietplanner.exception.EntityNotFoundException;
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
  private ModelMapperConfig mapper;

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
        CaloriesCalculator.calcDeficit(dietPlan.getTdee(), dietPlanDTO.getGoal().getValue()));
    dietPlan.setProtein(CaloriesCalculator.calcProtein(dietPlan.getDeficit()));
    dietPlan.setFat(CaloriesCalculator.calcFat(dietPlan.getDeficit()));
    dietPlan.setCarbs(CaloriesCalculator.calcCarbs(dietPlan.getDeficit()));
    return dietPlan;
  }

  public DietPlanTrack findDietPlanTrackByDietPlanId(String dietPlanId) {
    DietPlan dietPlan = findDietPlanById(dietPlanId);
    return dietPlanTrackRepository
        .findByDateAndDietPlanId(getToday(), dietPlanId)
        .orElseGet(() -> new DietPlanTrack(dietPlan, splitForMealKcal(dietPlan), getToday()));
  }

  private String getToday() {
    return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
  }

  public DietPlanTrack addMacros(String dietPlanId, String category, Macros macros) {
    DietPlanTrack dietPlanTrack = findDietPlanTrackByDietPlanId(dietPlanId);
    dietPlanTrack.getDeficit().addTaken(CaloriesCalculator.calcDeficit(macros));
    Macros dietPlanMacros = getMacrosByCategory(dietPlanTrack.getMealKcal(), category);
    dietPlanMacros.getProtein().addTaken(macros.getProtein().getTaken());
    dietPlanMacros.getFat().addTaken(macros.getFat().getTaken());
    dietPlanMacros.getCarbs().addTaken(macros.getCarbs().getTaken());
    return dietPlanTrackRepository.save(dietPlanTrack);
  }

  private Macros getMacrosByCategory(MealKcal mealKcal, String category) {
    return switch (category) {
      case MacrosConstants.BREAKFAST -> mealKcal.getBreakfast();
      case MacrosConstants.LUNCH -> mealKcal.getLunch();
      case MacrosConstants.DINNER -> mealKcal.getDinner();
      default -> mealKcal.getSnack();
    };
  }

  public MealKcal getMealKcal(String id) {
    Optional<DietPlan> dietPlan = this.dietPlannerRepository.findById(id);
    if (dietPlan.isPresent()) {
      return splitForMealKcal(dietPlan.get());
    } else {
      throw new EntityNotFoundException("No macros found.");
    }
  }

  private MealKcal splitForMealKcal(DietPlan dietPlan) {
    Macros macros = new Macros(dietPlan.getProtein(), dietPlan.getFat(), dietPlan.getCarbs());
    return new MealKcal(
        CaloriesCalculator.macrosPercentage(macros, 30),
        CaloriesCalculator.macrosPercentage(macros, 30),
        CaloriesCalculator.macrosPercentage(macros, 15),
        CaloriesCalculator.macrosPercentage(macros, 25));
  }
}
