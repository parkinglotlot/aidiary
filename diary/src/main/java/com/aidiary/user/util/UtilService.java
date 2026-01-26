package com.aidiary.user.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class UtilService {


  // 난수 생성
  public String randomNum(){

    StringBuilder sb = new StringBuilder();


    for (int i = 0; i < 8; i++){
      int checkNum = (int)(Math.random() * 10);
      char randomChar = (char)(checkNum + '0');
      sb.append(randomChar + checkNum);
    }


    return sb.toString();
  }





}
