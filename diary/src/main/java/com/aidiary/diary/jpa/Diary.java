package com.aidiary.diary.jpa;

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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String title;

  @Column
  private String content;



  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime date;

  @Column(length= 5000)
  private String aiAnalysis;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="writer_id", nullable = false)
  private User user;

  @Transient
  private String mode;

  public Diary(String title, String content, LocalDateTime date){
    this.title = title;
    this.content = content;
    this.date = date;
  }

}
