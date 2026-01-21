package com.aidiary.diary.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class PageRequestDTO {

  // 쿼리에서 쓰일 offset과 limit을 구하기 위함(limit은 pageSize)

  private int page; //요청하는 페이지 번호(1부터 시작)
  private int pageSize; //limit 역할

  public PageRequestDTO(int page, int pageSize){
    this.page = page <= 0 ? 1 : page;
    this.pageSize = pageSize <= 0 ? 10 : pageSize;

  }

  // MyBatis 쿼리에서 사용할 offset 계산
  public int getOffset(){
    return (page - 1) * pageSize;
  }



}
