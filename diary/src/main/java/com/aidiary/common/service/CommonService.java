package com.aidiary.common.service;

import com.aidiary.diary.mapper.DiaryMapper;
import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.dto.CustomException;
import com.aidiary.user.jpa.User;
import com.aidiary.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommonService {

  private final UserRepository userRepository;
  private final DiaryMapper diaryMapper;

    // 로그인 유저 존재 확인 및 현재 로그인 유저가 해당 아이디의 유저인지 확인 (Validation)
    // 이건 다음에  필요할 때 사용
    public void validateUser(String sessionLoginId,String loginId){

    // 로그인 유저 존재 확인
    User user = userRepository.getUserByLoginId(loginId);

    //httpstatus 세팅
    HttpStatus httpStatusBadRequest = HttpStatus.BAD_REQUEST;
    HttpStatus httpStatusUnauthorized = HttpStatus.UNAUTHORIZED;

    //CustomResponseEntity 반환
    CustomResponseEntity customResponse = new CustomResponseEntity();

    // 1. 로그인 유저 존재 확인
    if(user == null){
      throw new CustomException(customResponse,httpStatusBadRequest);
    }

    //2. 로그인 유저
    boolean isLoginUser = sessionLoginId.equals(sessionLoginId);
    if(!isLoginUser) throw new CustomException(customResponse,httpStatusBadRequest);
  }

  public User validateUserEmpty(String sessionLoginId){

    // 로그인 유저 존재 확인
    User user = userRepository.getUserByLoginId(sessionLoginId);

    //httpstatus 세팅
    HttpStatus httpStatusBadRequest = HttpStatus.BAD_REQUEST;
    HttpStatus httpStatusUnauthorized = HttpStatus.UNAUTHORIZED;

    //CustomResponseEntity 반환
    CustomResponseEntity customResponse = new CustomResponseEntity();

    if(user == null){
      throw new CustomException(customResponse,httpStatusBadRequest);
    }

    return user;

  }



}
