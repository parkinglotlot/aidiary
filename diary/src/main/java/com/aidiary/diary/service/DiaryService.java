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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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



}
