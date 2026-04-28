package com.aidiary.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

  @GetMapping("/goHome")
  public String goMain(){
    return "main";
  }

}
