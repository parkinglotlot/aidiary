package com.aidiary.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
public class PageResponseDTO<T> {

  private List<T> list;
  private PageRequestDTO pageRequestDTO;
  private Pagination pagination;

  public PageResponseDTO(List<T> list, PageRequestDTO pageRequestDTO){
    this.list = list;
    this.pageRequestDTO = pageRequestDTO;
    this.pagination = new Pagination(pageRequestDTO.getPage(), pageRequestDTO.getPageSize());
  }


}
