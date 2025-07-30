package com.gitsunjaeab.mapick.application.api.member.dto.internal;

import com.gitsunjaeab.mapick.application.domain.member.MemberInterest;
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

    public static CategorySimpleDTO of(MemberInterest memberInterest) {
        return CategorySimpleDTO.builder()
                .id(memberInterest.getCategory().getId())
                .name(memberInterest.getCategory().getName())
                .build();
    }
}
