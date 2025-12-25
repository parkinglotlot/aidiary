package com.aidiary.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberShipDTO {

  private String loginId;
  private String password;
  private String verifyPassword;
  private String email;
  private LocalDate birthday;

}
