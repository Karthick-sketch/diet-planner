package org.karthick.dietplanner.aichatservice;

import lombok.AllArgsConstructor;
import org.karthick.dietplanner.aichatservice.model.ChatModel;
import org.karthick.dietplanner.aichatservice.model.MessageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class Qwen3LLMService {
  private final RestTemplate restTemplate;

  public String chatQwen3LLM(ChatModel chatModel) {
    String baseURL = "http://localhost:1234/v1/chat/completions";
    ResponseEntity<String> response = restTemplate.postForEntity(baseURL, chatModel, String.class);
    return response.getBody();
  }

  public String foodSuggestChat(String prompt) {
    return chatQwen3LLM(buildFoodSuggestChatModel(prompt));
  }

  private ChatModel buildFoodSuggestChatModel(String prompt) {
    ChatModel chatModel = new ChatModel();
    chatModel.setModel("qwen3-8b");
    chatModel.setMessages(
        List.of(
            new MessageModel(
                "system",
                "Respond in JSON format with keys: [name, calories (number), macronutrients (protein, carbs, fat), description (string)] (array). No other output. /no_think"),
            new MessageModel("user", prompt)));
    chatModel.setTemperature(0.4);
    chatModel.setMax_tokens(-1);
    chatModel.setStream(false);
    return chatModel;
  }
}
