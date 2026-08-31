package com.aidiary.userAuth.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/userAuth")
public class UserAuthController {

    @GetMapping("/userAuthAdmin")
    public String userAuthAdmin(){
        // 유저권한 페이지 이동
        return "admin/userAuth";
    }
}
