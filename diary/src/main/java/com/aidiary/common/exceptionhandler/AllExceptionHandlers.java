package com.aidiary.common.exceptionhandler;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aidiary.user.dto.CustomException;
import com.aidiary.user.dto.CustomResponseEntity;

@RestControllerAdvice
@ControllerAdvice
public class AllExceptionHandlers {

  private final Logger log = LoggerFactory.getLogger(AllExceptionHandlers.class);

  // 유저 인증
  @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
  public CustomException exceptionResponse(Exception e){
    HttpStatus httpStatus = HttpStatus.PROXY_AUTHENTICATION_REQUIRED;
    log.error("AuthenticationException 발생: ", e);  // 스택트레이스 전체 출력

    CustomResponseEntity customResponseEntity = new CustomResponseEntity();

    return new  CustomException(customResponseEntity,httpStatus);
  }

  //런타임
  @ExceptionHandler(RuntimeException.class)
  public CustomException exceptionRuntime(Exception e){
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    log.error("AuthenticationException 발생: ", e);  // 스택트레이스 전체 출력
    return new CustomException(new CustomResponseEntity(),httpStatus);
  }

}
