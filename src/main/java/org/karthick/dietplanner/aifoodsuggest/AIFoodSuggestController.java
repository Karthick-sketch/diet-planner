package org.karthick.dietplanner.aifoodsuggest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.karthick.dietplanner.aifoodsuggest.entity.AIFoodSuggest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai-food-suggest")
@AllArgsConstructor
public class AIFoodSuggestController {
  private final AIFoodSuggestService aiFoodSuggestService;

  @GetMapping
  public List<AIFoodSuggest> getSuggestions() throws JsonProcessingException {
    return aiFoodSuggestService.getFoodSuggest();
  }

  @PostMapping
  public void addSuggestions(@RequestBody List<AIFoodSuggest> AIFoodSuggests) {
    aiFoodSuggestService.addSuggestions(AIFoodSuggests);
  }
}
