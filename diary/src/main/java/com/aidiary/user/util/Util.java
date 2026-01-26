package com.aidiary.user.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Util {

  // Key : 1) 비밀키 만들어 반환
  public Key getAESKey()throws Exception{
    Key keySpec = null;
    //16자리 문자열 생성
//    String key = "1234567891234567";
//    String iv = key.substring(0,16);
    String iv = "1234567891234567";
    byte[] keyBytes = new byte[16];
    byte[] b = iv.getBytes("UTF-8");

    int len = b.length;
    if(len > keyBytes.length){
      // 일반적인 숫자나 영어라면 keyBytes와 b의 length는 같지만
      // 한글이나 특수문자가 섞이면 그 숫자가 더 커지기 때문에
      // 이를 방지하기 위해 해당 식을 사용한다.
      len = keyBytes.length;
    }

    //b 배열을 keyBytes 배열로 복사
    System.arraycopy(b,0,keyBytes,0,len);
    keySpec = new SecretKeySpec(keyBytes,"AES");

    return keySpec;

  }

  // Key : 2) 암호화
  public String encAES(String str)throws Exception{
    Key keySpec = getAESKey();
    //16자리 문자열 생성
    String iv = "0987654321654321";
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE,keySpec,new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
    byte[] encrypted = c.doFinal(str.getBytes(StandardCharsets.UTF_8));
    String enStr = new String(Base64.encodeBase64String(encrypted));

    return enStr;
  }

  // Key : 3) 복호화
  public String deAES(String enStr) throws Exception{
    Key keySpec = getAESKey();
    String iv = "0987654321654321";
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.DECRYPT_MODE,keySpec,new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
    byte[] byteStr = Base64.decodeBase64(enStr);
    String decStr = new String(c.doFinal(byteStr),StandardCharsets.UTF_8);

    return decStr;
  }

}
