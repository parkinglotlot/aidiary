package com.aidiary.ai.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.aidiary.ai.DTO.ChatCompletionResponse;
import com.aidiary.diary.dto.DiaryDTO;
import com.aidiary.user.dto.CustomException;

import com.aidiary.ai.enumeration.Feeling;

import java.util.ArrayList;
import java.util.Arrays;




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

  public String callGPT(DiaryDTO diary){

    Map<String,Object> requestBody = new HashMap<>();

    requestBody.put("model","gpt-4o-mini"); //가성비 모델


    List<Map<String,String>> messages = List.of(
        Map.of("role","user","content",diary.getContent() + "        해당 내용의 감정을 분석하고 위로와 해결책을 제시해줘. 그리고 맨 마지막 줄에서는 해당 내용의 감정을 'HAPPY','SAD', 'ANGRY', 'SCARED', 'DISGUST', 'SURPRISE' 중에서 골라 그것만 써줘(중복 가능). 모두 합쳐서 5000자 이내여야 해. ")
    );

    requestBody.put("messages",messages);

    //지연  429 Too Many Requests: 방지
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new CustomException(e.getMessage());
    }

    ChatCompletionResponse response = null;
     String answer  = null;

    // ai로 보내기
    try{
        response = restClient.post()
        .uri(baseUrl +"/chat/completions")
        .header("Authorization", "Bearer " + apiKey) //API  키 추가
        .body(requestBody)
        .retrieve()
        .body(ChatCompletionResponse.class);
        if(response != null){
            answer = response.getChoices().get(0).getMessage().getContent();

   // diary 감정상태 세팅
   List<Feeling> diaryFeelings = new ArrayList<>();
   diary.setFeelingList(diaryFeelings);
   
    List<Feeling> feelingList = Arrays.asList(Feeling.values());

    for(Feeling feeling: feelingList){
       if(answer.contains(feeling.getCode())){
        // 다이어리 feelingList에 세팅
        diaryFeelings.add(feeling);
      }
    }

              log.info("answer:{}", answer);

    // diary 분석글 세팅
    diary.setAiAnalysis(answer);
        }
        
    }catch(Exception e){
        throw new CustomException(e.getMessage());
    }
    

    
  
    
    
    return answer;

  }

}
