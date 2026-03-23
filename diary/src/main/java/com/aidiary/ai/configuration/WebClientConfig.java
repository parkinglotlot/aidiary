package com.aidiary.ai.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
public class WebClientConfig {

  @Value("${ai.openai.base-url}")
  private String url;

  @Value("${ai.openai.model}")
  private String model;

  @Value("${ai.openai.api-key}")
  private String apiKey;


  @Bean
  public RestClient webClient(){
    return RestClient.builder()
        .baseUrl(url)
        .defaultHeader("Authorization","Bearer" + System.getenv("ai.openai.api-key"))
        .defaultHeader("Content-Type","application/json")
        .build();
  }

}
