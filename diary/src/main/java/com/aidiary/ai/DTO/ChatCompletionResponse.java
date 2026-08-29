package com.aidiary.ai.DTO;

import java.util.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatCompletionResponse {

    private String id;
    private String object;
    private String model;
    private List<Choice> choices;

    @Data
    public static class Choice {
        private int index;
        private Message message;
        private String finishReason;
    }

    @Data
    public static class Message{
        private String role;
        private String content;
    }

}
