package org.karthick.dietplanner.food;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.food.document.Food;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FoodService {
    private FoodRepository foodRepository;

    public List<Food> findAllFoods() {
        return foodRepository.findAll();
    }

    public Food createFood(Food food) {
        return foodRepository.save(food);
    }
}
