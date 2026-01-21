package com.aidiary.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
public class PageResponseDTO {

  private List<Object> list;
  private Pagination pagination;

  public PageResponseDTO(List<Object> list, Pagination pagination){

    this.list = list;
    this.pagination = pagination;
  }


}
