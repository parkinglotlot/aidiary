package com.aidiary.userAuth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.diary.dto.PageResponseDTO;
import com.aidiary.user.dto.CustomException;
import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.dto.MemberShipDTO;
import com.aidiary.userAuth.service.UserAuthService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Controller
@RequestMapping("/userAuth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    private final Logger log = LoggerFactory.getLogger(UserAuthController.class);

    @GetMapping("/userAuthAdmin")
    public String userAuthAdmin(HttpServletRequest request){
        
        //유저 권한 확인
        String loginId = (String) request.getSession().getAttribute("loginId");
        String role = userAuthService.checkUserRole(loginId);
        if(role == null  || !role.equals("ADMIN")){
            HttpStatus httpStatus = HttpStatus.FORBIDDEN;
            CustomResponseEntity customResponseEntity = new CustomResponseEntity("권한이 없습니다.", httpStatus.value(), null, httpStatus);
            throw new CustomException(customResponseEntity, httpStatus);
        }

        // 유저권한 페이지 이동
        return "admin/userAuth";
    }

    // (관리자)사용자 목록 가져오기
    @GetMapping("/userAuthUser")
    @ResponseBody
    public ResponseEntity<CustomResponseEntity> userAuthUser(HttpServletRequest request, @RequestParam(value = "curPage", defaultValue = "1") int curPage, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, 
    @RequestParam(value = "filter", defaultValue = "") String filter, @RequestParam(value = "filter2", defaultValue = "") String filter2) {

        log.info("filter:{}",filter);

        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(curPage);
        pageRequestDTO.setPageSize(pageSize);
        pageRequestDTO.setFilter(filter);
        pageRequestDTO.setFilter2(filter2);


        log.info("pageRequestDTO:{}",pageRequestDTO);

        
        // 유저목록 페이지 리스트 가져오기
        PageResponseDTO pageResponseDTO = userAuthService.getUserList(pageRequestDTO);

        return new ResponseEntity<>(new CustomResponseEntity("Success",200,pageResponseDTO,HttpStatus.OK),HttpStatus.OK);
    }
    
    @PutMapping("/updateUserState")
    @ResponseBody 
    public ResponseEntity<CustomResponseEntity> updateUserState(HttpServletRequest request, @RequestBody MemberShipDTO membershipDTO) {

        String adminLoginId = (String)request.getSession().getAttribute("loginId");

        int updateResult = userAuthService.updateUserState(membershipDTO, adminLoginId);

        if(updateResult != 1){
            return new ResponseEntity<>(new CustomResponseEntity("회원 상태 업데이트 실패",500,null,HttpStatus.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new CustomResponseEntity("회원 상태 업데이트 성공",200,null,HttpStatus.OK),HttpStatus.OK);
    }
}
