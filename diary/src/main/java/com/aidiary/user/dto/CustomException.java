package com.aidiary.user.dto;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class CustomException extends RuntimeException{

  private CustomResponseEntity customResponseEntity;
  private ResponseEntity<CustomResponseEntity> responseEntity;
  private HttpStatus httpStatus;

  public CustomException(String message){
    super(message);
  }

  public CustomException(CustomResponseEntity customResponseEntity, HttpStatus httpStatus){
    this.customResponseEntity = customResponseEntity;

    // httpStatus 를 반영해 customResponseEntity 세팅
    customResponseEntity.setHttpStatus(httpStatus);
    customResponseEntity.setCode(httpStatus.value());
    customResponseEntity.setMessage(httpStatus.getReasonPhrase());

    this.responseEntity = ResponseEntity.status(httpStatus).body(customResponseEntity);
  }

}
