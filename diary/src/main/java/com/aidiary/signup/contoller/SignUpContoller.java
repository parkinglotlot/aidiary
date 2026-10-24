package com.aidiary.signup.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@Controller 
@AllArgsConstructor 
@RequestMapping ("/signup")
public class SignUpContoller {

    @GetMapping  ("/goSignUp")
    private String goSignUp(){

        return "signup/signup";
    }
    
}
