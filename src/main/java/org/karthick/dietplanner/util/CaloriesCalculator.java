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
        percentageOf(macros.getProtein().getTotal(), percent),
        percentageOf(macros.getFat().getTotal(), percent),
        percentageOf(macros.getCarbs().getTotal(), percent));
  }

  public static long calcPercentage(double value, double maxValue) {
    return Math.round((value / maxValue) * 100);
  }

  public static long calcDeficit(double kcal, int percent) {
    long deficit = CaloriesCalculator.kcalPercentage(kcal, percent);
    long round = deficit / 100 * 100;
    if (deficit - round == 0) {
      return deficit;
    } else if (deficit - round < 50) {
      return round + 50;
    } else {
      return round + 100;
    }
  }

  public static double calcTakenDeficit(Macros macros) {
    return macros.getProtein().getTaken() * 4
        + macros.getFat().getTaken() * 9
        + macros.getCarbs().getTaken() * 4;
  }

  public static long calcTotalDeficit(Macros macros) {
    return Math.round(
        macros.getProtein().getTotal() * 4
            + macros.getFat().getTotal() * 9
            + macros.getCarbs().getTotal() * 4);
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
