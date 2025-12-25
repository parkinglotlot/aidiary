package com.aidiary.user.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class UserException extends RuntimeException{

  private CustomResponseEntity customResponseEntity;
  private ResponseEntity<CustomResponseEntity> responseEntity;
  private HttpStatus httpStatus;

  public UserException(String message){
    super(message);
  }

  public UserException(CustomResponseEntity customResponseEntity, HttpStatus httpStatus){
    this.customResponseEntity = customResponseEntity;
    this.responseEntity = ResponseEntity.status(httpStatus).body(customResponseEntity);
  }

}
