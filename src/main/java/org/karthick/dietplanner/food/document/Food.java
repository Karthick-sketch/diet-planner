package org.karthick.dietplanner.food.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document(collection = "foods")
public class Food {
    @Id
    private UUID id;
    private String name;
    private String servingSize;
    private double calories;
    private double gProtein;
    private double gFat;
    private double gCarbs;
    private String category;
    private String[] tags;
    private boolean isCustom;

    public Food() {
        this.id = UUID.randomUUID();
    }
}
