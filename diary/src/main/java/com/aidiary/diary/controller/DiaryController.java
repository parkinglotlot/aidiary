package com.aidiary.diary.controller;

import com.aidiary.common.service.CommonService;
import com.aidiary.diary.dto.PageResponseDTO;
import com.aidiary.diary.jpa.Diary;
import com.aidiary.diary.service.DiaryService;
import com.aidiary.user.dto.CustomException;
import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.jpa.User;
import com.aidiary.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import javax.naming.AuthenticationException;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.springframework.web.servlet.View;

@AllArgsConstructor
@Controller
@RequestMapping("/diary")
public class DiaryController {


  private final Logger log = LoggerFactory.getLogger(DiaryController.class);

  private final DiaryService diaryService;
  private final UserService userService;
  private final CommonService commonService;
  private final View error;


  // 다이어리 조회
  // 로그인 유저에 맞는 다이어리 목록을 조회한다.
  // 페이징 적용
  @ResponseBody
  @GetMapping("/readCustom")
  public ResponseEntity<CustomResponseEntity> readDiary(HttpServletRequest request,
      @RequestParam(defaultValue = "1") int curPage, @RequestParam(defaultValue = "10") int pageSize)
      throws AuthenticationException {

//    log.info("로그1");

    // 로그인 유저 validation
//    log.info("sessionLoginId 확인 중:{}",curUser.getLoginId());
//    log.info("sessionLoginId 확인 중:{}",curUser.getLoginId() + sessionLoginId);
//    commonService.validateUser(sessionLoginId,loginId);

    String sessionLoginId = String.valueOf(request.getSession().getAttribute("loginId"));

    User curUser = commonService.validateUserEmpty(sessionLoginId);

    // 프론트에 반환할 (리스트를 포함한) paginationDTO 생성 (서비스단에서 에러 핸들링 포함)
    PageResponseDTO pageResponseDTO =  diaryService.returnDiaries(curPage,pageSize,curUser);


    // 최종 상태 반환
    HttpStatus httpStatusOK = HttpStatus.OK;

    CustomResponseEntity customResponseEntity = new CustomResponseEntity().builder()
        .code(httpStatusOK.value())
        .data(pageResponseDTO)
        .message(httpStatusOK.getReasonPhrase())
        .build();

    return ResponseEntity.ok(customResponseEntity);
  }

  //다이어리 생성
  //로그인 유저에 속한 다이어리를 생성한다.
  @ResponseBody
  @PostMapping("/create")
  public ResponseEntity<CustomResponseEntity> createDiary(HttpServletRequest request, @RequestBody
      Diary diary) throws AuthenticationException {

    HttpSession session =  request.getSession();
    String sessionLoginId = String.valueOf(session.getAttribute("loginId"));

    // 권한 확인
//    commonService.validateUser(sessionLoginId,loginId);

    //해당 유저 가져오기
    commonService.validateUserEmpty(sessionLoginId);
    User curUser = commonService.validateUserEmpty(sessionLoginId);

    int insertDiary =  diaryService.insertDiary(diary,curUser);

    HttpStatus httpStatusOK = HttpStatus.OK;
    CustomResponseEntity customResponseEntity = new CustomResponseEntity(httpStatusOK.getReasonPhrase(),httpStatusOK.value(),insertDiary,httpStatusOK);

    return new ResponseEntity<>(customResponseEntity,httpStatusOK);
  }

  //로그인 유저가 속하고, 선택한 다이어리 수정
  @ResponseBody
  @PutMapping("/modify")
  public ResponseEntity<CustomResponseEntity> modifyDiary(HttpServletRequest request,@RequestBody Diary diary)
      throws AuthenticationException {
    String sessionId = "";
    User user = null;

    HttpSession session = request.getSession(true);
    sessionId = (String) session.getAttribute("loginId");

//    log.info("log1 : {}",error);
//    log.info("sessionId : {}",sessionId);
    user = commonService.validateUserEmpty(sessionId);


    boolean result = false;


    try {

      result = diaryService.updateDiary(diary,user);

    } catch (Exception e) {
      throw new RuntimeException();
    }

    HttpStatus httpStatusOK = HttpStatus.OK;
    HttpStatus httpStatusBad = HttpStatus.BAD_REQUEST;

    if(result){
      return new ResponseEntity<>(new CustomResponseEntity(httpStatusOK.getReasonPhrase(),httpStatusOK.value(),result,httpStatusOK),httpStatusOK);
    }else {
      return new ResponseEntity<>(new CustomResponseEntity(httpStatusBad.getReasonPhrase(),httpStatusBad.value(),null,httpStatusBad), httpStatusBad);
    }

  }

  // 로그인 유저가 택한 다이어리 삭제
  @ResponseBody
  @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  public ResponseEntity<CustomResponseEntity> deleteDiary(HttpServletRequest request, @RequestBody Diary diary)
      throws AuthenticationException {

    log.info("디버깅:{}",diary);

    // 유저 validation
    HttpSession session = request.getSession(true);
    String sessionLoginId = String.valueOf(session.getAttribute("loginId"));
    User user =  commonService.validateUserEmpty(sessionLoginId);

    boolean deleteResult = false;


    deleteResult =  diaryService.deleteDiary(user,diary);

    HttpStatus status = null;

    status = deleteResult ? HttpStatus.OK : HttpStatus.BAD_REQUEST;


    CustomResponseEntity customResponseEntity = new CustomResponseEntity(status.getReasonPhrase(),status.value(), deleteResult,status);

    return new ResponseEntity<>(customResponseEntity,status);

  }



}
