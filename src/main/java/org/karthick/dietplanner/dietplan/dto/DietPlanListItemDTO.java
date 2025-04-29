package org.karthick.dietplanner.dietplan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DietPlanListItemDTO {
    private String id;
    private String title;
    private String description;
    private double deficit;
}
