package com.aidiary.userAuth.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.diary.dto.PageResponseDTO;
import com.aidiary.user.dto.MemberShipDTO;
import com.aidiary.user.mapper.UserMapper;

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
}