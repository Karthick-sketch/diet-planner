package org.karthick.dietplanner.aifoodsuggest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.karthick.dietplanner.aifoodsuggest.entity.AIFoodSuggest;
import org.karthick.dietplanner.aifoodsuggest.repository.AIFoodSuggestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AIFoodSuggestService {
  private final AIFoodSuggestRepository aifoodSuggestRepository;

  private final String content =
      "<think>\n\n</think>\n\n[\n  {\n    \"name\": \"Upma\",\n    \"calories\": 250,\n    \"macronutrients\": {\n      \"protein\": 6,\n      \"carbs\": 38,\n      \"fat\": 4\n    },\n    \"description\": \"A traditional Indian breakfast made with semolina, vegetables, and spices.\"\n  },\n  {\n    \"name\": \"Idli\",\n    \"calories\": 150,\n    \"macronutrients\": {\n      \"protein\": 3,\n      \"carbs\": 20,\n      \"fat\": 1\n    },\n    \"description\": \"Steamed rice and lentil cakes, usually served with sambar and chutney.\"\n  },\n  {\n    \"name\": \"Dosa\",\n    \"calories\": 180,\n    \"macronutrients\": {\n      \"protein\": 4,\n      \"carbs\": 25,\n      \"fat\": 3\n    },\n    \"description\": \"A thin, crispy pancake made from fermented rice and urad dal.\"\n  },\n  {\n    \"name\": \"Poha\",\n    \"calories\": 160,\n    \"macronutrients\": {\n      \"protein\": 5,\n      \"carbs\": 24,\n      \"fat\": 3\n    },\n    \"description\": \"A quick breakfast made from flattened rice, often cooked with vegetables and spices.\"\n  },\n  {\n    \"name\": \"Oats Porridge\",\n    \"calories\": 200,\n    \"macronutrients\": {\n      \"protein\": 6,\n      \"carbs\": 30,\n      \"fat\": 4\n    },\n    \"description\": \"A healthy alternative to traditional breakfasts, made with oats, milk, and fruits.\"\n  }\n]";

  public List<AIFoodSuggest> getFoodSuggest() throws JsonProcessingException {
    return convertToFoodSuggest();
  }

  private List<AIFoodSuggest> convertToFoodSuggest() throws JsonProcessingException {
    String jsonString = removeObstacles();
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonString, new TypeReference<>() {});
  }

  private String removeObstacles() {
    String jsonString = content.replace("\n", "");
    jsonString = jsonString.replace("<think>", "");
    return jsonString.replace("</think>", "");
  }

  public void addSuggestions(List<AIFoodSuggest> AIFoodSuggests) {
    aifoodSuggestRepository.saveAll(AIFoodSuggests);
  }
}
