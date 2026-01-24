package com.aidiary.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class Pagination {

  // 한 페이지 당 게시글 수
  private int pageSize = 10;

  // 한 블럭(range)당 페이지 수
  private int rangeSize = 10;

  //현재 페이지 (초기화)
  private int curPage = 1;

  // 현재 블럭 (range)
  private int curRange = 1;

  //총 게시글 수
  private int totalCnt;

  //총 페이지 수
  private int totalPage;

  // 총 블럭 수
  private int totalRange;

  // 시작 페이지
  private int startPage = 1;

  //끝 페이지
  private int endPage = 1;

  //시작 index
  private int startIndex = 0;

  // 이전 페이지
  private int prevPage;

  //다음 페이지
  private int nextPage;

  //총게시글 수와 현재 페이지를 이용
  public Pagination(int curPage,int totalCnt){
    // 페이징 처리 순서
    // 1. 총 페이지 수
    // 2. 총 블럭(range) 수
    // 3. range setting

    // 총 게시물 수와 현재 페이지를 Controller 로부터 받아온다.

    // 현재 페이지 세팅
    setCurPage(curPage);

    // 총 게시물 수 세팅
    setTotalCnt(totalCnt);

    //총 페이지 수
    setTotalPage(totalCnt);

    //총 블럭 수
    setTotalRange(totalPage);

    setCurRange(curPage);

    // 블럭 세팅(Range)
    rangeSetting(curPage);

    //db 검색을 위한 startIndex 설정
    setStartIndex(curPage);

  }

  // 총 페이지 수
  public void setTotalPage(int totalCnt){
    this.totalPage = (int) Math.ceil(totalCnt*1.0/pageSize);
  }

  // 총 블럭 수
  public void setTotalRange(int totalPage){
    this.totalRange = (int)Math.ceil(totalPage*1.0/rangeSize);
  }

  // range setting
  public void rangeSetting(int curPage){

    //현재 페이지 세팅
    setCurPage(curPage);
    this.startPage = (curRange - 1) * rangeSize + 1;
    this.endPage = startPage + rangeSize - 1;

    if(endPage > totalPage){
      this.endPage = totalPage;
    }

    this.prevPage = curPage - 1;
    this.nextPage = curPage + 1;
  }

  // 현재 range(블럭) 세팅
  public void setCurRange(int curPage){
    this.curRange = (int)((curPage - 1)/rangeSize) + 1;
  }

  //start index 설정
  public void setStartIndex(int curPage){
    this.startIndex = (curPage - 1) * pageSize;
  }




}
