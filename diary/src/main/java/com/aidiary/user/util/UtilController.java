package com.aidiary.user.util;


import com.aidiary.user.dto.MemberShipDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@AllArgsConstructor
@Controller
public class UtilController {



  //메일 보내기
  private final JavaMailSender javamailSender;
  private final UtilService utilService;

  @PostMapping(value = "/sendMail")
  @ResponseBody
  public ResponseEntity<String> mailSend(@Value("${spring.mail.username}") String emailFrom, String emailTo, HttpSession session){

    //난수 생성
    String verifyNumber = utilService.randomNum();

    //이메일 보내기
    String title = "인증번호 메일 보냅니다.";

    String content = "인증번호는" + "<br></br>" + verifyNumber + "<br></br>" + "입니다. 해당 인증 번호를 확인란에 입력해주세요.";

    try {
      MimeMessage mimeMailMessage = javamailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true,"UTF-8");

      mimeMessageHelper.setFrom(emailFrom);
      mimeMessageHelper.setTo(emailTo);
      mimeMessageHelper.setSubject(title);
      mimeMessageHelper.setText(content);
      javamailSender.send(mimeMailMessage);

    } catch (MessagingException e) {
//      throw new UserException("메일 발송에 실패했습니다.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증번호 발송에 실패했습니다.");
    }

    // 인증번호 boolean 세션이 있을 경우 초기화
    if(session.getAttribute("VERIFY_NUMBER") != null){
      session.setAttribute("VERIFY_NUMBER",false);
    }

    // 이메일 보낸 뒤 인증번호 세션에 저장
    session.setAttribute("verifyNumber",verifyNumber);

    return ResponseEntity.ok().body("인증번호가 발송되었습니다.");

  }

  @PostMapping(value = "/verifyEmail")
  @ResponseBody
  public ResponseEntity<String> verifyEmail(HttpSession session, String verifyNumber, MemberShipDTO member){

    //세션에 저장된 인증번호 불러오기
    String setVerifyNumber =  (String)session.getAttribute("verifyNumber");

    if(verifyNumber.equals(setVerifyNumber)) {
//      member.setVerifyEmail(true);
      session.setAttribute("VERIFY_NUMBER",true);

      //기존 번호 삭제
      session.removeAttribute("verifyNumber");

      return ResponseEntity.ok("인증번호가 일치합니다.");

    }else{
//      member.setVerifyEmail(false);
      session.setAttribute("VERIFY_NUMBER",false);
    }


    return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");

  }

}
