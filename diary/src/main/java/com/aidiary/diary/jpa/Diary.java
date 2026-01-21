package com.aidiary.diary.jpa;

import com.aidiary.user.jpa.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String title;

  @Column
  private String content;

  @Column
  private LocalDateTime date;

  @Column
  private String aiAnalysis;

  @ManyToOne
  @JoinColumn(name="loginId", nullable = false)
  private User writer;

  public Diary(String title, String content, LocalDateTime date){
    this.title = title;
    this.content = content;
    this.date = date;
  }

}
