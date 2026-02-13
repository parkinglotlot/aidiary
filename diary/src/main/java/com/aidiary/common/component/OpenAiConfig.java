package com.aidiary.common.component;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OpenAiConfig {

    private final OpenAiProperties openAiProperties;

    // @Bean
    // public WebClient openAiWWebClient(){

    // }
    
}
