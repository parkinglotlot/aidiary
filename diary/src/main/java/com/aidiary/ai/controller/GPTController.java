 package com.aidiary.ai.controller;

import com.aidiary.ai.service.OpenAiService;
import com.aidiary.diary.jpa.Diary;
import com.aidiary.diary.service.DiaryService;
import com.aidiary.user.dto.CustomException;
import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.jpa.User;
import com.aidiary.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/gpt")
public class GPTController {

  private final OpenAiService openAiService;
  private final UserService userService;
  private final DiaryService diaryService;

  @PostMapping("/call")
  public CustomResponseEntity ask(@RequestBody(required = true) Diary diary,HttpServletRequest request){

    HttpSession httpSession = request.getSession();
    String loginId = (String)httpSession.getAttribute("loginId");

    //1. user 검사
    User user = userService.findUserById(loginId);
    HttpStatus httpStatusUnAuth = HttpStatus.UNAUTHORIZED;
    if(user == null || !diary.getWriter().getLoginId().equals(loginId)) throw new CustomException(new CustomResponseEntity(httpStatusUnAuth.getReasonPhrase(),httpStatusUnAuth.hashCode(),null,httpStatusUnAuth),httpStatusUnAuth);

    // 2. 메세지 전송
    String answer = openAiService.callGPT(diary);
    diary.setAiAnalysis(answer);

    //다이어리에 ai 분석 저장
    boolean aiAnalysis = diaryService.setAiDiary(diary, user) == 1 ? true : false;
    HttpStatus httpStatus500 = HttpStatus.INTERNAL_SERVER_ERROR;
    if(!aiAnalysis) throw new CustomException(new CustomResponseEntity(httpStatus500.getReasonPhrase(),httpStatus500.hashCode(),null,httpStatus500),httpStatus500);

    HttpStatus httpStatus200 = HttpStatus.OK;
    return new CustomResponseEntity(httpStatus500.getReasonPhrase(),httpStatus200.hashCode(),answer,httpStatus200);
  }


}
