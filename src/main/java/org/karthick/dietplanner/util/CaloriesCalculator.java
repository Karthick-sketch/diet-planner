package org.karthick.dietplanner.util;

import org.karthick.dietplanner.dietplan.enums.Activity;
import org.karthick.dietplanner.shared.model.Macros;

public final class CaloriesCalculator {
  public static double findTDEE(int age, double weight, double height, Activity activity) {
    return 10 * weight + 6.25 * height - 5 * age + activity.getValue();
  }

  public static long kcalPercentage(double kcal, int percent) {
    return Math.round(kcal - percentageOf(kcal, percent));
  }

  public static long percentageOf(double value, int percent) {
    return Math.round((value * percent) / 100);
  }

  public static Macros macrosPercentage(Macros macros, int percent) {
    return new Macros(
        percentageOf(macros.getProtein(), percent),
        percentageOf(macros.getFat(), percent),
        percentageOf(macros.getCarbs(), percent));
  }

  public static long calcProtein(double deficit) {
    return Math.round(percentageOf(deficit, 30) / 4.0);
  }

  public static long calcFat(double deficit) {
    return Math.round(percentageOf(deficit, 30) / 9.0);
  }

  public static long calcCarbs(double deficit) {
    return Math.round(percentageOf(deficit, 40) / 4.0);
  }
}
