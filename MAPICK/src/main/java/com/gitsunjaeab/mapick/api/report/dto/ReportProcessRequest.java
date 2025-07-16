package com.gitsunjaeab.mapick.api.report.dto;

import com.gitsunjaeab.mapick.domain.report.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관리자용 신고 처리 요청 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportProcessRequest {

    @NotNull(message = "처리 상태는 필수입니다")
    private ReportStatus status;  // 처리 상태 (IN_PROGRESS, RESOLVED, REJECTED)

//    private String processNote;  // 처리 메모 (선택사항)
} 