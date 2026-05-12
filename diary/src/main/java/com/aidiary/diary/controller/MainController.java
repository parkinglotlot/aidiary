package com.aidiary.diary.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public String Main(HttpServletRequest request){
     HttpSession session = request.getSession();
     if (session.getAttribute("loginId'") != null) return "main";
    return "login";
  }

}
