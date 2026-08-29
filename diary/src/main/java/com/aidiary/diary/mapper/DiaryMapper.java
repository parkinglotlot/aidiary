package com.aidiary.diary.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.aidiary.diary.dto.DiaryDTO;
import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.user.jpa.User;

@Mapper
public interface DiaryMapper {

  @Insert("insert into diary (content, date, title,writer_id) values (#{content},#{date},#{title},#{writerId})")
  int isInsertDiaryList(DiaryDTO diary);

  // 해당되는 전체 일기장 리스트 가져오기
  @Select("SELECT count(a.id) from diary a join user u on a.writer_id = u.id WHERE a.writer_id = #{user.id} and (#{pageRequestDTO.filter} is null or trim(#{pageRequestDTO.filter}) = '' or a.title like concat('%',#{pageRequestDTO.filter},'%'))")
  int totalCnt(@Param("pageRequestDTO")PageRequestDTO pageRequestDTO, @Param("user")User user);

  //검색어 Test
  @Select("SELECT count(id) from diary WHERE writer_id = #{user.id} and (#{pageRequestDTO.filter} is null or trim(#{pageRequestDTO.filter}) = '' or title like concat('%',#{pageRequestDTO.filter},'%'))")
  int totalCntTest(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("user") User user);

  @Select("<script>"
      + "SELECT a.id,a.content,a.date,a.title,a.ai_analysis,a.writer_id as writerId, a.ai_analysis as aiAnalysis, u.login_id as loginId, u.full_name as fullName "
      + "from diary a join user u on a.writer_id = u.id "
      + "WHERE a.writer_id = #{user.id} "
      + "<if test = 'pageRequestDTO.filter != null and pageRequestDTO.filter.trim() !=\"\"'>"
      + "and (#{pageRequestDTO.filter} is null or trim(#{pageRequestDTO.filter}) = '' or a.title like concat('%',#{pageRequestDTO.filter},'%')) "
      + "</if>"
      + "limit #{pageRequestDTO.offset}, #{pageRequestDTO.pageSize}"
      + "</script>")
  List<DiaryDTO> selectRequestPaginationList(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO, @Param("user") User user);

  @Select("SELECT a.id,a.content,a.date,a.title,a.ai_analysis,a.writer_id as writerId, a.ai_analysis as aiAnalysis, u.login_id as loginId, u.full_name as fullName FROM diary a join user u on a.writer_id = u.id WHERE a.writer_id = #{user.id} AND a.id = #{diaryId}")
  DiaryDTO getDiaryByIdLoginId(@Param("diaryId") long diaryId, @Param("user") User user);

  @Update("update diary set content = #{diary.content}, title = #{diary.title} where writer_id = #{user.id} and id = #{diary.id}")
  int updateDiary(@Param("diary") DiaryDTO diary, @Param("user")User user);

  @Update("update diary set ai_analysis = #{diary.aiAnalysis} where writer_id = #{user.id} and id = #{diary.id}")
  int updateAiAnalysis(@Param("diary") DiaryDTO diary,@Param("user") User user);

  @Select("select coalesce(max(id),0) from diary")
  int maxDiaryId();


}
