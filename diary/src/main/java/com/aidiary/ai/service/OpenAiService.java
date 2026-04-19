package com.aidiary.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;



@Service
public class OpenAiService {

  private final RestClient restClient;

  @Value("${ai.openai.base-url}")
  private String baseUrl;

  @Value("${ai.openai.model}")
  private String model;

  @Value("${ai.openai.api-key}")
  private String apiKey;

  public OpenAiService (){
    this.restClient = RestClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader("Content-Type", "application/json")
        .defaultHeader("Authorization", "Bearer" + apiKey)
        .build();
  }

  public String callGPT(String prompt){

    Map<String,Object> requestBody = new HashMap<>();

    requestBody.put("model","gpt-4o-mini"); //가성비 모델

    List<Map<String,String>> messages = List.of(
        Map.of("role","user","content",prompt)
    );

    requestBody.put("messages",messages);

    return restClient.post()
        .uri("/chat/completions")
        .body(requestBody)
        .retrieve()
        .body(String.class);

  }

}
