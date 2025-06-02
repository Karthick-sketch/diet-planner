package org.karthick.dietplanner.aifoodsuggest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.karthick.dietplanner.aifoodsuggest.dto.FilterDTO;
import org.karthick.dietplanner.aifoodsuggest.entity.AIFoodSuggest;
import org.karthick.dietplanner.aifoodsuggest.repository.AIFoodSuggestRepository;
import org.karthick.dietplanner.dietplan.DietPlanService;
import org.karthick.dietplanner.aichatservice.Qwen3LLMService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AIFoodSuggestService {
  private final AIFoodSuggestRepository aifoodSuggestRepository;
  private final DietPlanService dietPlanService;
  private final Qwen3LLMService qwen3LLMService;

  private String buildPrompt(FilterDTO filter, long calories) {
    return String.format(
        "Suggest me 5 healthy %s cuisine %s foods for %s under %d calories.",
        filter.getCuisineFilter(), filter.getFoodFilter(), filter.getMealFilter(), calories);
  }

  public List<AIFoodSuggest> suggestFood(FilterDTO filterDTO) throws JsonProcessingException {
    return convertToFoodSuggest(
        qwen3LLMService.foodSuggestChat(
            buildPrompt(
                filterDTO, dietPlanService.getCaloriesByMealCategory(filterDTO.getMealFilter()))));
  }

  private List<AIFoodSuggest> convertToFoodSuggest(String response) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = mapper.readTree(response);
    JsonNode contentNode = rootNode.path("choices").path(0).path("message").path("content");
    String jsonString = removeObstacles(contentNode.asText());
    return mapper.readValue(jsonString, new TypeReference<>() {});
  }

  private String removeObstacles(String content) {
    String jsonString = content.replace("\n", "");
    jsonString = jsonString.replace("<think>", "");
    jsonString = jsonString.replace("</think>", "");
    if (jsonString.charAt(0) != '[') {
      jsonString = "[" + jsonString;
    }
    if (jsonString.charAt(jsonString.length() - 1) != ']') {
      jsonString = jsonString + "]";
    }
    return jsonString;
  }

  public void addSuggestions(List<AIFoodSuggest> AIFoodSuggests) {
    aifoodSuggestRepository.saveAll(AIFoodSuggests);
  }
}
