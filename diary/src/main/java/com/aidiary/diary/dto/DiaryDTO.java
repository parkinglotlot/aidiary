package com.aidiary.diary.dto;

import java.util.List;
import java.time.LocalDateTime;

import com.aidiary.ai.enumeration.Feeling;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class DiaryDTO {

 
  private Long id;

  private String title;


  private String content;




  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime date;

  // 감정 분석
  private String aiAnalysis;

  //감정 분석
  private List<Feeling> feelingList;

  // 유저 loginId
  private String loginId;

  //유저 pk
  private long writerId;

  // 유저 이름
  private String fullName;

  private String mode;

  public DiaryDTO(String title, String content, LocalDateTime date, long writerId){
    this.title = title;
    this.content = content;
    this.date = date;
    this.writerId = writerId;
  }


}
