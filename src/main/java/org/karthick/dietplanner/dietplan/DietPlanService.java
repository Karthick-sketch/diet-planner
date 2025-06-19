package org.karthick.dietplanner.dietplan;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.dietplan.dto.DietPlanOverviewDTO;
import org.karthick.dietplanner.dietplan.dto.MetricsDTO;
import org.karthick.dietplanner.dietplan.entity.DietPlan;
import org.karthick.dietplanner.dietplan.entity.DietPlanTrack;
import org.karthick.dietplanner.dietplan.repository.DietPlanRepository;
import org.karthick.dietplanner.dietplan.repository.DietPlanTrackRepository;
import org.karthick.dietplanner.dietplan.dto.DietPlanListItemDTO;
import org.karthick.dietplanner.exception.EntityNotFoundException;
import org.karthick.dietplanner.security.UserSession;
import org.karthick.dietplanner.shared.model.Calories;
import org.karthick.dietplanner.shared.model.Macros;
import org.karthick.dietplanner.shared.model.MealKcal;
import org.karthick.dietplanner.util.CaloriesCalculator;
import org.karthick.dietplanner.util.constants.MacrosConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

@AllArgsConstructor
@Service
public class DietPlanService {
  private final DietPlanRepository dietPlannerRepository;
  private final DietPlanTrackRepository dietPlanTrackRepository;
  private final UserSession userSession;

  public List<DietPlan> findAllDietPlans() {
    return dietPlannerRepository.findByUserId(userSession.getAuthenticatedUserId());
  }

  public List<DietPlanListItemDTO> findAllDietPlanList() {
    return dietPlannerRepository.findAllDietPlanListByUserId(userSession.getAuthenticatedUserId());
  }

  public DietPlan findDietPlanById(String id) {
    Optional<DietPlan> dietPlan =
        dietPlannerRepository.findByIdAndUserId(id, userSession.getAuthenticatedUserId());
    if (dietPlan.isEmpty()) {
      throw new EntityNotFoundException("Diet plan not found");
    }
    return dietPlan.get();
  }

  public DietPlan findDietPlanByUserId() {
    Optional<DietPlan> dietPlan =
        dietPlannerRepository.findByUserIdAndActiveIsTrue(userSession.getAuthenticatedUserId());
    if (dietPlan.isEmpty()) {
      throw new EntityNotFoundException("Diet plan not found");
    }
    return dietPlan.get();
  }

  public DietPlan createDietPlan(DietPlan dietPlan) {
    dietPlan.setTodayWeight(dietPlan.getWeight());
    dietPlan.setUserId(userSession.getAuthenticatedUserId());
    dietPlan.setActive(true);
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
            dietPlan,
            new DietPlanTrack(dietPlan.getTodayWeight(), LocalDate.now(), dietPlan.getId())));
  }

  public DietPlanTrack findDietPlanTrackByDietPlanId(String dietPlanId) {
    DietPlan dietPlan = findDietPlanById(dietPlanId);
    return dietPlanTrackRepository
        .findByDateAndDietPlanId(LocalDate.now(), dietPlanId)
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

  public DietPlanTrack updateMacros(String dietPlanId, String category, Macros macros) {
    DietPlanTrack dietPlanTrack = findDietPlanTrackByDietPlanId(dietPlanId);
    Macros dietPlanMacros = getMacrosByCategory(dietPlanTrack.getMealKcal(), category);
    dietPlanMacros.getProtein().setTaken(macros.getProtein().getTaken());
    dietPlanMacros.getFat().setTaken(macros.getFat().getTaken());
    dietPlanMacros.getCarbs().setTaken(macros.getCarbs().getTaken());
    dietPlanTrack.getDeficit().setTaken(calcDeficitTaken(dietPlanTrack));
    adjustMacros(dietPlanTrack);
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

  private double calcDeficitTaken(DietPlanTrack dietPlanTrack) {
    return CaloriesCalculator.calcTakenDeficit(
        new Macros(dietPlanTrack.getProtein(), dietPlanTrack.getFat(), dietPlanTrack.getCarbs()));
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

  public MealKcal getMealKcal(String dietPlanId) {
    Optional<DietPlanTrack> dietPlanTrack =
        dietPlanTrackRepository.findByDateAndDietPlanId(LocalDate.now(), dietPlanId);
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

  public MetricsDTO getMetricsByDateRange() {
    List<String> days = new ArrayList<>();
    List<Double> weights = new ArrayList<>();
    Date[] dateRange = getOneWeekDateRange();
    DietPlan dietPlan = findDietPlanByUserId();
    DietPlanTrack dietPlanTrack = findDietPlanTrackByDietPlanId(dietPlan.getId());
    dietPlanTrackRepository
        .findByDateRangeAndDietPlanId(dateRange[0], dateRange[1], dietPlan.getId())
        .forEach(
            track -> {
              days.add(getDayName(track.getDate()));
              weights.add(track.getWeight());
            });
    return buildMetrics(dietPlanTrack, dietPlan.getTitle(), days, weights);
  }

  private Date[] getOneWeekDateRange() {
    ZoneId zoneId = ZoneId.of("Asia/Kolkata");
    LocalDate today = LocalDate.now();
    return new Date[] {
      Date.from(today.minusWeeks(1).atStartOfDay(zoneId).toInstant()),
      Date.from(today.atTime(LocalTime.MAX).atZone(zoneId).toInstant())
    };
  }

  private String getDayName(LocalDate date) {
    return date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
  }

  private MetricsDTO buildMetrics(
      DietPlanTrack dietPlanTrack, String title, List<String> days, List<Double> weights) {
    MetricsDTO metricsDTO = new MetricsDTO();
    Calories deficit = dietPlanTrack.getDeficit();
    metricsDTO.setTitle(title);
    metricsDTO.setTaken(CaloriesCalculator.calcPercentage(deficit.getTaken(), deficit.getTotal()));
    metricsDTO.setCurrentWeight(dietPlanTrack.getWeight());
    metricsDTO.setDeficit(deficit.getTotal());
    metricsDTO.setProtein(dietPlanTrack.getProtein().getTotal());
    metricsDTO.setCarbs(dietPlanTrack.getCarbs().getTotal());
    metricsDTO.setFat(dietPlanTrack.getFat().getTotal());
    metricsDTO.setDays(days);
    metricsDTO.setWeights(weights);
    return metricsDTO;
  }

  public boolean isThereAnyActivePlans() {
    return dietPlannerRepository.findByActiveTrue().isPresent();
  }

  public long getCaloriesByMealCategory(String category) {
    try {
      DietPlan dietPlan = findDietPlanByUserId();
      DietPlanTrack dietPlanTrack = findDietPlanTrackByDietPlanId(dietPlan.getId());
      Macros macros = getMacrosByCategory(dietPlanTrack.getMealKcal(), category);
      return CaloriesCalculator.calcTotalDeficit(macros);
    } catch (EntityNotFoundException e) {
      return 500L;
    }
  }

  public List<DietPlanOverviewDTO> getDietPlansOverview(String dietPlanId) {
    return dietPlanTrackRepository.findAllDietPlanOverview(dietPlanId);
  }
}
