package com.gitsunjaeab.mapick.application.api.member.dto.internal;

import com.gitsunjaeab.mapick.application.domain.member.MemberInterest;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class MemberInterestDTO {

    private Long id;

    @NotNull
    private OffsetDateTime createdAt;

    private List<CategorySimpleDTO> categories;

    public static MemberInterestDTO of(MemberInterest memberInterest) {
        return MemberInterestDTO.builder()
                .id(memberInterest.getId())
                .createdAt(memberInterest.getCreatedAt())
                .categories(List.of(CategorySimpleDTO.of(memberInterest)))
                .build();
    }

}
