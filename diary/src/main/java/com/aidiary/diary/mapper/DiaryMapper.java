package com.aidiary.diary.mapper;
import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.diary.dto.PageResponseDTO;
import com.aidiary.diary.jpa.Diary;
import com.aidiary.user.jpa.User;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DiaryMapper {

  @Insert("insert into diary (content, date, title,writer_id,user_id,login_id) values (#{content},#{date},#{title},#{writer.id},#{writer.id},#{writer.id})")
  int isInsertDiaryList(Diary diary);

  @Select("SELECT id,content,date,title,ai_analysis,writer_id,user_id,login_id from diary WHERE writer_id = #{user.id} limit #{pageRequestDTO.offset}, #{pageRequestDTO.pageSize}")
  List<Diary> selectRequestPaginationList(PageRequestDTO pageRequestDTO, User user);



}
