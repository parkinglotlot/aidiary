package com.aidiary.diary.mapper;

import com.aidiary.diary.jpa.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

  Diary getDiaryById(Long id);


  int deleteDiaryById(Long id);

}
