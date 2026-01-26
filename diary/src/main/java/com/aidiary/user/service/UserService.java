package com.aidiary.user.service;

import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.dto.MemberShipDTO;
import com.aidiary.user.dto.CustomException;
import com.aidiary.user.jpa.User;
import com.aidiary.user.repository.UserRepository;
//import com.aidiary.user.util.SecurityConfig;
import com.aidiary.user.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final Util util;
//  private final SecurityConfig securityConfig;
  private final Logger log = LoggerFactory.getLogger(UserService.class);

  //로그인 아이디로 유저 단순 반환
  public User findUserById(String loginId){
    return userRepository.getUserByLoginId(loginId);

  }




  // 비밀번호 BCrypt 암호 인코딩 -> 다른 암호화/복호화 로직 사용
//  private String incodePassword(String password){
//
////    if(password != null) return securityConfig.passwordEncoder().encode(password);
//    return null;
//  }

  // 로그인 아이디와 비밀번호 비교
  public boolean existsByLoginIdAndPassword(String loginId,String password)throws Exception{
    //암호화
    String hashedPassword = util.encAES(password);

    if(userRepository.findByLoginIdAndPassword(loginId,hashedPassword) != null){
      return true;
    }
    return false;
  }

  //로그인 세션 만들기
  public void loginSession(HttpServletRequest request, String loginId){
    HttpSession session = request.getSession(true);
    log.info("setlogin 중 : {}", loginId);
    if(loginId != null) session.setAttribute("loginId",loginId);
    String sessionId = session.getAttribute("loginId").toString();
    log.info("setlogin 성공 : {}", sessionId);
//    return session;
  }
  
  //회원가입 시 유효성 검사

  public ResponseEntity<CustomResponseEntity> verifyIdentification(MemberShipDTO member, HttpSession session){

    // OK일때
    HttpStatus goodStatus =HttpStatus.OK;
    HttpStatus badStatus = HttpStatus.UNPROCESSABLE_ENTITY;

    // 응답 데이터

    boolean valTest = false;

    boolean emptyLoginId =  !(member.getLoginId() == null || member.getLoginId().isEmpty());
    boolean notLoginIdMethod = false;
    boolean duplicateLoginId = false;
    boolean emptyPassword = !(member.getPassword() == null || member.getPassword().isEmpty());
    boolean notPasswordMethod = false;
    boolean verifyPassword = false;

    boolean emptyEmail = !(member.getEmail() == null || member.getEmail().isEmpty());
    boolean isEmailVerify = (boolean) session.getAttribute("VERIFY_NUMBER");

    boolean emptyFullName = !(member.getFullName() == null || member.getFullName().isEmpty());

    boolean emptyBirthday = member.getBirthday() != null;
    boolean isBirthdayAge = false;


    if (!emptyLoginId)
      throw new CustomException("아이디를 입력해주세요.");
      // log.info("아이디를 입력해주세요.");


    if(emptyLoginId){
      if (!member.getLoginId().matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,12}$"))
        throw new CustomException("아이디는 8자에서 12자 이상 영문자와 숫자를 혼합한 형태여야 합니다.");
        //log.info("아이디는 8자에서 12자 이상 영문자와 숫자를 혼합한 형태여야 합니다.");
      else notLoginIdMethod = true;
    }

    if(notLoginIdMethod){
      Optional<User> duplicateUser =  userRepository.findByLoginId(member.getLoginId());
      if(duplicateUser.isPresent())
        throw new CustomException("아이디가 중복됩니다.");
        //log.info("아이디가 중복됩니다.");
      else duplicateLoginId = true;
    }


    if (!emptyPassword)
      throw new CustomException("비밀번호를 입력하세요.");
      //log.info("비밀번호를 입력하세요.");


    if (emptyPassword){
      if(!(member.getPassword().length() >= 4 && member.getPassword().length() <=8))
        throw new CustomException("비밀번호는 4자이상 8자이하여야 합니다.");
        // log.info("비밀번호는 4자이상 8자이하여야 합니다.");
      else notPasswordMethod = true;
    }



    if(notPasswordMethod){
      if(!member.getVerifyPassword().equals(member.getPassword()))
        throw new CustomException("비밀번호가 일치하지 않습니다.");
        //log.info("비밀번호가 일치하지 않습니다.");
      else verifyPassword = true;
    }

    if(!emptyEmail){
      throw new CustomException("이메일을 입력해주세요.");
    }



    if(!isEmailVerify) throw new CustomException("이메일 인증을 진행해주세요.");

    if(!emptyBirthday)
      throw new CustomException("생년월일을 입력해주세요.");
      //log.info("생년월일을 입력해주세요.");

    if(emptyBirthday){
      if(!LocalDate.now().minusYears(18).isAfter(member.getBirthday()))
        throw new CustomException("만 18세 이상만 가입 가능합니다.");
        //log.info("만 18세 이상만 가입 가능합니다.");
      else isBirthdayAge = true;
    }


    if(emptyLoginId && notLoginIdMethod && duplicateLoginId && emptyPassword  && notPasswordMethod && verifyPassword && emptyEmail && isEmailVerify && emptyFullName && emptyBirthday && isBirthdayAge) {
      valTest = true;
    };

    if(valTest) return ResponseEntity.status(goodStatus).body(new CustomResponseEntity("회원가입을 진행합니다.",goodStatus.value(),null,goodStatus));

    return ResponseEntity.status(badStatus).body(new CustomResponseEntity("회원가입 실패",badStatus.value(),null,badStatus));

  }


  // 회원가입 시 아이디/비번 insert
  @Transactional
  public boolean insertMember(MemberShipDTO member) throws Exception {

    User user = new User();
    user.setLoginId(member.getLoginId());
    //암호화
    String hashedPassword = util.encAES(member.getPassword());
    user.setPassword(hashedPassword);
    user.setEmail(member.getEmail());
    user.setBirthday(member.getBirthday());
    user.setFullName(member.getFullName());
    userRepository.save(user);

    Optional<User> isLoginUser = userRepository.findByLoginId(user.getLoginId());

    if(isLoginUser.isPresent()) return true;

    return false;
  }




}
