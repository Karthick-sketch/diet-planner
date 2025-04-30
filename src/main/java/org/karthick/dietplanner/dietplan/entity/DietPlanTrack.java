package org.karthick.dietplanner.dietplan.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import org.karthick.dietplanner.shared.model.Calories;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "diet_plan_tracks")
public class DietPlanTrack {
    @Id
    private String id;
    private double weight;
    private double tdee;
    private Calories deficit;
    private Calories protein;
    private Calories fat;
    private Calories carbs;
    private Date date;
    private String dietPlanId;
}
