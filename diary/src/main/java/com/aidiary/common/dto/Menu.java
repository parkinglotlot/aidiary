package com.aidiary.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor 
@NoArgsConstructor
@Builder 
public class Menu {

    private String name;
    private String url;
    private String icon;

}
