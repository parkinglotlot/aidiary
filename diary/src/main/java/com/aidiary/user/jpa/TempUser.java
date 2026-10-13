package com.aidiary.user.jpa;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class TempUser {
    
  @Id
  private long id;

  @Column
  private String loginId;

  @Column
  private String password;

  @Column
  private LocalDate birthday;
}
