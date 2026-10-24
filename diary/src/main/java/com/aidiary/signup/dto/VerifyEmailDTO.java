package com.aidiary.signup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class VerifyEmailDTO {

    private String email;
    private String emailCode;
    private String emailCodeResponse;

}
