package com.aidiary.common.exceptionhandler;

import java.nio.file.AccessDeniedException;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<CustomResponseEntity> exceptionResponse(Exception e){
    HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    log.error("AuthenticationException 발생: ", e);  // 스택트레이스 전체 출력

    CustomException customException = new CustomException(new CustomResponseEntity(),httpStatus);

    return customException.getResponseEntity();
  }

  //유저 인증은 했으나 관리자 권한 x
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<CustomResponseEntity> exceptionForbidden(Exception e){
    HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    log.error("AccessDeniedException 발생: ", e);  // 스택트레이스 전체 출력

    CustomException customException = new CustomException(new CustomResponseEntity(),httpStatus);

    return customException.getResponseEntity();
  }


  //런타임
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<CustomResponseEntity> exceptionRuntime(Exception e){
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    log.error("RuntimeException 발생: ", e);  // 스택트레이스 전체 출력
    CustomException customException = new CustomException(new CustomResponseEntity(),httpStatus);
    return customException.getResponseEntity();
  }

}
