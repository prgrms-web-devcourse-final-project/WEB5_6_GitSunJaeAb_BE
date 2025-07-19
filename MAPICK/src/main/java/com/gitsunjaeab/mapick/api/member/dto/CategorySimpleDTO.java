package com.gitsunjaeab.mapick.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategorySimpleDTO {
    private Long id;
    private String name;

}
