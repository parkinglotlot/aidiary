package com.aidiary.common.exceptionhandler;

import com.aidiary.user.dto.CustomException;
import com.aidiary.user.dto.CustomResponseEntity;
import javax.naming.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AllExceptionHandlers {

  // 유저 인증
  @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
  public CustomException exceptionResponse(){
    HttpStatus httpStatus = HttpStatus.PROXY_AUTHENTICATION_REQUIRED;

    CustomResponseEntity customResponseEntity = new CustomResponseEntity();

    return new  CustomException(customResponseEntity,httpStatus);
  }

  //런타임
  @ExceptionHandler(RuntimeException.class)
  public CustomException exceptionRuntime(){
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    return new CustomException(new CustomResponseEntity(),httpStatus);
  }

}
