package org.karthick.dietplanner.aifoodsuggest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.karthick.dietplanner.aifoodsuggest.dto.FilterDTO;
import org.karthick.dietplanner.aifoodsuggest.entity.AIFoodSuggest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai-food-suggest")
@AllArgsConstructor
public class AIFoodSuggestController {
  private final AIFoodSuggestService aiFoodSuggestService;

  @PostMapping
  public List<AIFoodSuggest> suggestFood(@RequestBody FilterDTO filterDTO) throws JsonProcessingException {
    return aiFoodSuggestService.suggestFood(filterDTO);
  }
}
