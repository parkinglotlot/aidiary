package com.aidiary.ai.controller;

import com.aidiary.ai.service.OpenAiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/gpt")
public class GPTController {

  private final OpenAiService openAiService;

  @GetMapping("/call")
  public String ask(@RequestParam(value = "question", required = true) String question){
    return openAiService.callGPT(question);
  }


}
