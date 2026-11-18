package com.aidiary.common.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.aidiary.common.dto.Menu;
import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.dto.MemberShipDTO;
import com.aidiary.user.service.UserService;
import org.slf4j.Logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class CommonController {

  private final UserService userService;
  private final Logger log = LoggerFactory.getLogger(CommonController.class);

  @GetMapping("/goHome")
  public String goMain(HttpServletRequest request){
    HttpSession session = request.getSession();
    String defaultUrl = (String)session.getAttribute("defaultUrl");

    
    return "redirect:" + defaultUrl;
  }

  @GetMapping ("/getMenu")
  public ResponseEntity<CustomResponseEntity> getMenu(HttpServletRequest request){

    // 메뉴 조회 로직 구현 (추후 db테이블 만들어지면 수정)
    Menu menuDiary = new Menu("일기 목록", "/diary/goHome", "icon-diary");
    Menu menuUserAuth = new Menu ("사용자 관리", "/userAuth/userAuthAdmin", "icon-user-auth");

    // 메뉴를 배열로 묶어서 반환
    List<Menu> menuList = new ArrayList<>();
  

    // 현재 유저 권한 조회
    String loginId = (String)request.getSession().getAttribute("loginId");

    MemberShipDTO curUser = userService.curUser(loginId);
    if(curUser != null ){
      if(curUser.getRole().equals("ADMIN")){
        menuList.add(menuUserAuth);
      }else{
         menuList.add(menuDiary);
      }
    }
    

    CustomResponseEntity response = new CustomResponseEntity(null, 200, menuList,null);
    
    return ResponseEntity.ok(response);
  }

}
