package com.gitsunjaeab.mapick.api.report.dto.request;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.report.Report;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    // 신고 데이터 필드들
    private Long id;

    private String description;

    @NotNull
    private String status;

    private OffsetDateTime createdAt;

    private OffsetDateTime resolvedAt;

    private Long reporter;

    private Long reportedMember;

    private Long roadmap;

    private Long marker;

    private Long quest;


}
