package com.aidiary.userAuth.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.diary.dto.PageResponseDTO;
import com.aidiary.user.dto.MemberShipDTO;
import com.aidiary.user.mapper.UserMapper;

import com.aidiary.user.dto.CustomException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserAuthService{

    private final UserMapper userMapper;
    private final Logger log = LoggerFactory.getLogger(UserAuthService.class);

    //사용자가 관리자인지 확인 
    public String checkUserRole(String loginId){
    
        MemberShipDTO user = userMapper.selectUserByLoginId(loginId);
        if(user != null){
            return user.getRole();
        }
        return null;
    }

    // 사용자 관리 리스트 가져오기
    public PageResponseDTO getUserList(PageRequestDTO pageRequestDTO) {
    
    List<MemberShipDTO> userList = userMapper.selectUserList(pageRequestDTO);
    log.info("userList:{}",userList);
    int totalUsers = userMapper.totalUserCount(pageRequestDTO);

    PageResponseDTO pageResponseDTO = new PageResponseDTO(userList,pageRequestDTO,totalUsers);

    return pageResponseDTO;
  }
  
  // 관리자가 회원 상태 업데이트
  public int updateUserState(MemberShipDTO membershipDTO, String adminLoginId) {

    MemberShipDTO admin = userMapper.selectUserByLoginId(adminLoginId); // 관리자 로그인 아이디로 회원 조회 (관리자 권한 확인)

    if(admin == null || !admin.getRole().equalsIgnoreCase("admin")){
        throw new CustomException("관리자 권한이 필요합니다.");
    }

    try {
        int updateResult = userMapper.updateUserState(membershipDTO);
        if(updateResult != 1){
            throw new CustomException("회원 상태 업데이트 중 오류가 발생했습니다.");
        }
        return updateResult;
    } catch (Exception e) {
        // TODO Auto-generated catch block
        throw new CustomException("회원 상태 업데이트 중 오류가 발생했습니다.");
    }

 }
}