package com.gitsunjaeab.mapick.api.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 퀘스트 신고 요청 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestReportRequest {

    @NotNull(message = "신고자 ID는 필수입니다")
    private Long reporterId;  // 신고자 ID

    @NotBlank(message = "신고 내용은 필수입니다")
    private String description;  // 신고 내용

    // questId는 URL path에서 받으므로 DTO에 포함하지 않음
} 