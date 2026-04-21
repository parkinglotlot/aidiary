package com.aidiary.ai.service;

import com.aidiary.diary.jpa.Diary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;





@Service
public class OpenAiService {

  private final RestClient restClient;
  private final Logger log = LoggerFactory.getLogger(getClass());

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
        .defaultHeader("Authorization", "Bearer " + apiKey)
        .build();
  }

  public String callGPT(Diary diary){
//    log.info("error1:{}",prompt);
    Map<String,Object> requestBody = new HashMap<>();
//    log.info("error2:{}",prompt);
    requestBody.put("model","gpt-4o-mini"); //가성비 모델

//    log.info("error3:{}",prompt);
    List<Map<String,String>> messages = List.of(
        Map.of("role","user","content",diary.getContent() + "        해당 내용의 감정을 분석하고 위로와 해결책을 제시해줘.")
    );
//    log.info("error4:{}",prompt);

    requestBody.put("messages",messages);
//    log.info("error5:{}",prompt);
    return restClient.post()
        .uri(baseUrl +"/chat/completions")
        .header("Authorization", "Bearer " + apiKey) //API  키 추가
        .body(requestBody)
        .retrieve()
        .body(String.class);

  }

}
