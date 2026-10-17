package com.aidiary.user.jpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.aidiary.diary.jpa.Diary;
import com.aidiary.user.enumeration.Role;
import com.aidiary.user.enumeration.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String loginId;

  @Column
  private String password;

  @Column
  private String email;

  @Column
  private String fullName;

  @Column
  private LocalDate birthday;

  @Column(length = 13)
  private String phone;

  @OneToMany(mappedBy = "user")
  private List<Diary> diaries = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @NotNull 
  private Role role;

  @Enumerated(EnumType.STRING)
  @NotNull 
  private Status state;

}


