package org.karthick.dietplanner.aifoodsuggest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.karthick.dietplanner.aichatservice.LLMService;
import org.karthick.dietplanner.aifoodsuggest.dto.FilterDTO;
import org.karthick.dietplanner.aifoodsuggest.entity.AIFoodSuggest;
import org.karthick.dietplanner.aifoodsuggest.repository.AIFoodSuggestRepository;
import org.karthick.dietplanner.dietplan.DietPlanService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AIFoodSuggestService {

  private final AIFoodSuggestRepository aifoodSuggestRepository;
  private final DietPlanService dietPlanService;
  private final LLMService llmService;

  private String buildPrompt(FilterDTO filter, long calories) {
    return String.format(
      "Suggest me 5 healthy %s cuisine %s foods for %s under %d calories.",
      filter.getCuisineFilter(),
      filter.getFoodFilter(),
      filter.getMealFilter(),
      calories
    );
  }

  public List<AIFoodSuggest> suggestFood(FilterDTO filterDTO)
    throws JsonProcessingException {
    return convertToFoodSuggest(
      llmService.foodSuggestChat(
        buildPrompt(
          filterDTO,
          dietPlanService.getCaloriesByMealCategory(filterDTO.getMealFilter())
        )
      )
    );
  }

  private List<AIFoodSuggest> convertToFoodSuggest(String response)
    throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(response, new TypeReference<>() {});
  }

  public void addSuggestions(@NonNull List<AIFoodSuggest> aiFoodSuggests) {
    aifoodSuggestRepository.saveAll(aiFoodSuggests);
  }
}
