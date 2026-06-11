package com.aidiary.diary.mapper;
import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.diary.dto.PageResponseDTO;
import com.aidiary.diary.jpa.Diary;
import com.aidiary.user.jpa.User;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DiaryMapper {

  @Insert("insert into diary (content, date, title,writer_id) values (#{content},#{date},#{title},#{writer.id})")
  int isInsertDiaryList(Diary diary);

  // 해당되는 전체 일기장 리스트 가져오기
  @Select("SELECT count(id) from diary WHERE writer_id = #{user.id} and (#{pageRequestDTO.filter} is null or trim(#{pageRequestDTO.filter}) = '' or title like concat('%',#{pageRequestDTO.filter},'%'))")
  int totalCnt(PageRequestDTO pageRequestDTO, User user);

  //검색어 Test
  @Select("SELECT count(id) from diary WHERE writer_id = #{user.id} and (#{pageRequestDTO.filter} is null or trim(#{pageRequestDTO.filter}) = '' or title like concat('%',#{pageRequestDTO.filter},'%'))")
  int totalCntTest(PageRequestDTO pageRequestDTO, User user);

  @Select("<script>"
      + "SELECT id,content,date,title,ai_analysis,writer_id from diary "
      + "WHERE writer_id = #{user.id}"
      + "<if test = 'pageRequestDTO.filter != null and pageRequestDTO.filter.trim() !=\"\"'>"
      + "and (#{pageRequestDTO.filter} is null or trim(#{pageRequestDTO.filter}) = '' or title like concat('%',#{pageRequestDTO.filter},'%')) "
      + "</if>"
      + "limit #{pageRequestDTO.offset}, #{pageRequestDTO.pageSize}"
      + "</script>")
  List<Diary> selectRequestPaginationList(PageRequestDTO pageRequestDTO, User user);

  @Select("SELECT id,content,date,title,ai_analysis,writer_id FROM diary WHERE writer_id = #{user.id} AND id = #{diaryId}")
  Diary getDiaryByIdLoginId(long diaryId, User user);

  @Update("update diary set content = #{diary.content}, title = #{diary.title} where writer_id = #{user.id} and id = #{diary.id}")
  int updateDiary(Diary diary, User user);

  @Update("update diary set ai_analysis = #{diary.aiAnalysis} where writer_id = #{user.id} and id = #{diary.id}")
  int updateAiAnalysis(Diary diary, User user);


}
