package com.aidiary.diary.dto;
import jakarta.persistence.FetchType;
import com.aidiary.user.jpa.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryDTO {

 
  private Long id;

  private String title;


  private String content;




  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime date;

  
  private String aiAnalysis;

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
