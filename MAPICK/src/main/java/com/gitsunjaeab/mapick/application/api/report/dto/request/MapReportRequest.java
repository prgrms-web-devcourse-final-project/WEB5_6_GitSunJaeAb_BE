package com.gitsunjaeab.mapick.application.api.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 지도(로드맵) 신고 생성 요청 DTO
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapReportRequest {

    @NotBlank(message = "신고 내용은 필수입니다")
    private String description;  // 신고 내용


} 