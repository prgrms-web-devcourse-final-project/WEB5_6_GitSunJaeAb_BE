package com.gitsunjaeab.mapick.api.report.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportDetailResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private ReportDetailDTO reportDetailDTO;

    public static ReportDetailResponse of(ReportDetailDTO reportDetailDTO) {
        return new ReportDetailResponse(
            ResponseCode.OK.getCode(),
            "신고 상세 조회 성공",
            LocalDateTime.now(),
            reportDetailDTO
        );
    }
}

