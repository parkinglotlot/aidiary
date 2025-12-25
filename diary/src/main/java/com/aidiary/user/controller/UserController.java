package com.aidiary.user.controller;

import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.dto.MemberShipDTO;
import com.aidiary.user.service.UserService;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Controller
public class UserController {

  private final UserService userService;

  // 로그인 시
  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<CustomResponseEntity> loginUser(String loginId, String passWord, HttpServletRequest request){

    //로그인 검사
    boolean isLogin =  userService.existsByLoginIdAndPassword(loginId,passWord);

    HttpSession session = request.getSession();

    if(isLogin){
      session = userService.loginSession(request,loginId);
    }

    HttpStatus status = isLogin ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
    String message = isLogin ? "Success" : "Fail";
    Object data = isLogin ? session.getAttribute("loginId") : null;


    return ResponseEntity.status(status).body(new CustomResponseEntity(message,status.value(),data,status));

  }

  //회원가입 검증 및 성공 시 user insert
  @PostMapping(value = "/valMembership", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<CustomResponseEntity> valMembership(@RequestBody MemberShipDTO member){

    ResponseEntity<CustomResponseEntity> returnResponse = userService.verifyIdentification(member);

    //회원가입 성공 시 member insert
    if (returnResponse.getStatusCode() == HttpStatus.OK) {
      boolean insertUser = userService.insertMember(member); // 유저 insert

      //유저 insert 실패시 진행할 것
      HttpStatus badStatus = HttpStatus.INTERNAL_SERVER_ERROR;

      if(insertUser) return returnResponse;
      else return ResponseEntity.status(badStatus).body(new CustomResponseEntity(badStatus.getReasonPhrase(),badStatus.value(),null,badStatus));
    }

    //유저 insert 없이 응답만 반환
    return userService.verifyIdentification(member);

  }



}
