package com.aidiary.diary.controller;

import com.aidiary.diary.dto.Pagination;
import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.dto.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@AllArgsConstructor
@Controller
@RequestMapping("/diary")
public class DiaryController {


  // 다이어리 조회
  // 로그인 유저에 맞는 다이어리 목록을 조회한다.
  // 페이징 적용

  @ResponseBody
  @GetMapping("/readCustom")
  public ResponseEntity<CustomResponseEntity> readDiary(HttpServletRequest request,String loginId, int curPage, int pageSize) {

    // 유저 validation
    HttpSession session =  request.getSession();
    boolean isLoginUser = loginId.equals((String)session.getAttribute("loginId"));
    if(!isLoginUser) throw new UserException("로그인 유저가 아닙니다.");

    Pagination pagination = new Pagination(curPage,pageSize);

    return null;
  }


}
