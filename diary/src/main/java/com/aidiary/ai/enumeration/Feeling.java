package com.aidiary.ai.enumeration;

import lombok.Getter;

@Getter
public enum Feeling {

    // 'HAPPY','SAD', 'ANGRY', 'SCARED', 'DISGUST', 'SURPRISE' 
    HAPPY("HAPPY", "행복"), 
    SAD("SAD", "슬픔"), 
    ANGRY("ANGRY", "분노"), 
    SCARED("SCARED", "두려움"), 
    DISGUST("SCARDISGUSTED", "혐오"), 
    SURPRISE("SURPRISE", "놀람");

    private final String code;
    private final String description;

    Feeling(String code, String description){
        this.code = code;
        this.description = description;
    }

}
