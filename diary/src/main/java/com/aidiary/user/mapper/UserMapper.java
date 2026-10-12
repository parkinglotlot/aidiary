package com.aidiary.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.diary.dto.PageResponseDTO;
import com.aidiary.diary.dto.Pagination;


import com.aidiary.user.dto.MemberShipDTO;

@Mapper 
public interface UserMapper {
    
    // 총 회원수를 조회
    @Select ("select \n" 
                + "count(id) \n "
            + "from user "
    +  " where 1=1 "
    +  " and (#{pageRequestDTO.filter} is null or trim(#{pageRequestDTO.filter}) = '' "
    +  " or login_id like concat('%', #{pageRequestDTO.filter}, '%') "
    +  " or full_name like concat('%', #{pageRequestDTO.filter}, '%') "
    +  " or email like concat('%', #{pageRequestDTO.filter}, '%') )"
    +  " and (#{pageRequestDTO.filter2} is null or trim(#{pageRequestDTO.filter2}) = '' or state = #{pageRequestDTO.filter2} )  ")
    int totalUserCount(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 회원을 조회 (페이지네이션 적용)
    @Select ("select \n" 
                + "id\n "
                + ", row_number()over(order by id) as ser\n "
                + ", login_id as loginId\n "
                + ", full_name as fullName\n "
                + ", email\n "
                + ", birthday\n "
                + ", state\n "
                + ", 'Management' as management \n "
            + "from user "
    +  " where 1=1 "
    +  " and (#{pageRequestDTO.filter} is null or trim(#{pageRequestDTO.filter}) = '' "
    +  " or login_id like concat('%', #{pageRequestDTO.filter}, '%') "
    +  " or full_name like concat('%', #{pageRequestDTO.filter}, '%') "
    +  " or email like concat('%', #{pageRequestDTO.filter}, '%') )"
    +  " and (#{pageRequestDTO.filter2} is null or trim(#{pageRequestDTO.filter2}) = '' or state = #{pageRequestDTO.filter2} ) "
    +  " order by id "
    + " limit #{pageRequestDTO.pageSize} offset #{pageRequestDTO.offset} ")
    List<MemberShipDTO> selectUserList(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 로그인 아이디로 회원 조회
    @Select("select"
                + " id\n "
                + ", role\n "
                + ", state\n "
                + ", birthday\n "
                + ", full_name as fullName\n "
                + ", login_id as loginId\n "
                + ", email\n "
            + " from user where login_id = #{loginId}\n "
            + " and state = 'ACTIVE'\n"
            +" limit 1")
    MemberShipDTO selectUserByLoginId(@Param("loginId") String loginId); 

    
}
