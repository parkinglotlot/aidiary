package com.aidiary.common.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix="ai.openai")
@Getter
@Setter
public class OpenAiProperties {

    private String baseUrl;
    private String apiKey;
    private String model;
    
}
