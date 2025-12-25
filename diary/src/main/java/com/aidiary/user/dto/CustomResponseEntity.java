package com.aidiary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@NoArgsConstructor
public class CustomResponseEntity {

  private String message;
  private int code;
  private Object data;
  private HttpStatus httpStatus;




  public CustomResponseEntity(String message, int code,Object data,HttpStatus httpStatus){

    this.message = message;
    this.code = code;
    this.data = data;
    this.httpStatus = httpStatus;
  }


}
