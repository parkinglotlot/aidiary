package com.aidiary.diary.service;

import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.diary.dto.PageResponseDTO;
import com.aidiary.diary.jpa.Diary;
import com.aidiary.diary.mapper.DiaryMapper;
import com.aidiary.user.dto.CustomException;
import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.jpa.User;
import java.util.Collections;
import java.util.List;
import javax.naming.AuthenticationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class DiaryService {

  private final DiaryMapper diaryMapper;

  // 조회 : 유저에 맞는 다이어리 리스트 조회
  public PageResponseDTO returnDiaries(int curPage, int pageSize, User user) {

    try {
      //pageRequestDTO 설정
      PageRequestDTO pageRequestDTO = new PageRequestDTO(curPage,pageSize);

      // 유저에 맞는 다이어리 리스트 반환
      List<Diary> diaryList = diaryMapper.selectRequestPaginationList(pageRequestDTO,user);

      //pageResponseDTO 세팅 및 반환
      PageResponseDTO pageResponseDTO = new PageResponseDTO(diaryList,pageRequestDTO);

      return pageResponseDTO;
    } catch (Exception e) {
      HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
      throw new CustomException(new CustomResponseEntity(),serverError);
    }
  }

  // 생성 : 유저에 속한 다이어리 등록
  @Transactional(rollbackFor = Exception.class)
  public int insertDiary(Diary diary,User user){

    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    //다이어리에 유저 세팅
    diary.setWriter(user);

    int insertResult = 0;

    try {
      insertResult = diaryMapper.isInsertDiaryList(diary);
    } catch (Exception e) {
      throw new CustomException(new CustomResponseEntity(httpStatus.getReasonPhrase(), httpStatus.value(),null, httpStatus),httpStatus);
    }

    if(insertResult < 1) throw new CustomException(new CustomResponseEntity(httpStatus.getReasonPhrase(), httpStatus.value(),null, httpStatus),httpStatus);

    return insertResult;
  }


  // 수정 : 유저에 속한 다이어리 수정
  public boolean updateDiary(Diary diary,User user) throws AuthenticationException {
    boolean result = false;

    if(user == null){
      // 검증
      throw new AuthenticationException();
    }

    try {
      int updateNum = diaryMapper.updateDiary(diary,user);

      if(updateNum == 1) result = true;

    } catch (Exception e) {
      CustomResponseEntity customResponseEntity = new CustomResponseEntity();
      HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
      throw new CustomException(customResponseEntity,serverError);
    }

    return result;
  }
}


