package com.aidiary.diary.service;

import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aidiary.diary.dto.PageRequestDTO;
import com.aidiary.diary.dto.PageResponseDTO;
import com.aidiary.diary.jpa.Diary;
import com.aidiary.diary.mapper.DiaryMapper;
import com.aidiary.diary.mapper.DiaryRepository;
import com.aidiary.user.dto.CustomException;
import com.aidiary.user.dto.CustomResponseEntity;
import com.aidiary.user.jpa.User;
import com.aidiary.user.repository.UserRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class DiaryService {

    private final UserRepository userRepository;
    private final DiaryMapper diaryMapper;
    private final DiaryRepository diaryRepository;
    private final Logger log = LoggerFactory.getLogger(DiaryService.class);

    // 조회 : 유저에 맞는 다이어리 리스트 조회
    public PageResponseDTO returnDiaries(int curPage, int pageSize, String filter, User user) {

        try {
            //pageRequestDTO 설정
            // log.info("curPage:{}", curPage);
            PageRequestDTO pageRequestDTO = new PageRequestDTO(curPage, pageSize);

            // 유저에 맞는 다이어리 리스트 반환
            // log.info("returnDiaries:{}", pageRequestDTO);
            // log.info("filter:{}", filter);
            pageRequestDTO.setFilter(filter); // 검색어 세팅
            // log.info("error 발생1 filter:{}",filter);
            List<Diary> diaryList = diaryMapper.selectRequestPaginationList(pageRequestDTO, user);
            log.info("error 발생2 pageRequestDTO :{}",pageRequestDTO);
            //  log.info("error 발생2 user :{}",user);
            //총 다이어리 수 반환
            int totalDiaries = diaryMapper.totalCnt(pageRequestDTO, user);
            // log.info("error 발생3 totalDiaries:{}",totalDiaries);
            //pageResponseDTO 세팅 및 반환
            PageResponseDTO pageResponseDTO = new PageResponseDTO(diaryList, pageRequestDTO, totalDiaries);
            // log.info("error 발생4 pageResponseDTO:{}", pageResponseDTO);
            return pageResponseDTO;
        } catch (Exception e) {
            log.error("returnDiaries 예외 발생: ", e);
            HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
            throw new CustomException(new CustomResponseEntity(), serverError);
        }
    }

    // 유저에게 맞는 다이어리 반환(로그인 유저 존재 검증 및 반환 + 해당 아이디의 게시글이 유저의 것인지 판단 + 페이지 이동-쿼리스트링 붙힌 상태)
    public Diary detailOk(String loginId, Long id) {
        boolean result = true;

        // 로그인 유저 존재 검증
        Optional<User> user = userRepository.findByLoginId(loginId);
        if (!user.isPresent()) {
            result = false;

        }

        //해당 아이디의 게시글이 유저의 것인지 판단
        Diary diary = diaryMapper.getDiaryByIdLoginId(id, user.get());

        if (diary == null) {
            result = false;

        }

        if (result) {
            return diary;
        }

        return null;
    }

    //생성 시, 다이어리 새 id 반환
    public int newDiaryId() {
        return diaryMapper.maxDiaryId();
    }

    // 생성 : 유저에 속한 다이어리 등록
    @Transactional(rollbackFor = Exception.class)
    public int insertDiary(Diary diary, User user) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        //다이어리에 유저 세팅
        diary.setWriter(user);

        //다이어리 date 세팅
        LocalDateTime now = LocalDateTime.now();
        diary.setDate(now);

        int insertResult = 0;

        try {
            insertResult = diaryMapper.isInsertDiaryList(diary);
             if (insertResult < 1) {
            throw new CustomException(new CustomResponseEntity(httpStatus.getReasonPhrase(), httpStatus.value(), null, httpStatus), httpStatus);
        }
        } catch (Exception e) {
            throw new CustomException(new CustomResponseEntity(httpStatus.getReasonPhrase(), httpStatus.value(), null, httpStatus), httpStatus);
        }

       

        return insertResult;
    }

    // 수정 : 유저에 속한 다이어리 수정
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDiary(Diary diary, User user) throws AuthenticationException {
        boolean result = false;

        if (user == null) {
            // 검증
            throw new AuthenticationException();
        }

        try {
            int updateNum = diaryMapper.updateDiary(diary, user);

            if (updateNum == 1) {
                result = true;
            }

        } catch (Exception e) {
            CustomResponseEntity customResponseEntity = new CustomResponseEntity();
            HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
            throw new CustomException(customResponseEntity, serverError);
        }

        return result;
    }

    // 삭제 : 유저에 속한 다이어리 삭제
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDiary(User user, Diary diary) throws AuthenticationException {
        boolean result = false;

        if (user == null) {
            throw new AuthenticationException();
        }

        int deleteResult = 0;

        try {
            deleteResult = diaryRepository.deleteDiaryById(diary.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (deleteResult == 1) {
            result = true;
        }

        return result;
    }

    // 다이어리 AI 분석 내용 저장
    public int setAiDiary(Diary diary, User user) {
        return diaryMapper.updateAiAnalysis(diary, user);
    }
}
