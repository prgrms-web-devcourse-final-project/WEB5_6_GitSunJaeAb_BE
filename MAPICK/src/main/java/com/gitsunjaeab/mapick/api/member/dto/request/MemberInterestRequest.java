package com.gitsunjaeab.mapick.api.member.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 관심분야 생성/수정 요청 DTO
 */

@Getter
@Setter
public class MemberInterestRequest {

    @NotNull(message = "카테고리는 필수입니다.")
    private List<Long> categoryId;


}