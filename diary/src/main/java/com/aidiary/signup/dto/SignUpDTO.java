package com.aidiary.signup.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.Builder.Default;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDTO {

  private int id;
  private String loginId;
  private String password;
  private String verifyPassword;
  private String email;

  @Builder.Default
  private boolean isEmailVerified = false;
  
  private String phone;
  private LocalDate birthday;
  private String fullName;
  private String state;
}
