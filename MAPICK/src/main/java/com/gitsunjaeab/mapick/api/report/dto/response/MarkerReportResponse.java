package com.gitsunjaeab.mapick.api.report.dto.response;

import com.gitsunjaeab.mapick.api.report.dto.ReportDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MarkerReportResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private ReportDTO reportDTO;

    public static MarkerReportResponse of(ReportDTO reportDTO) {

        return new MarkerReportResponse(
            ResponseCode.OK.getCode(),
            "마커 신고 성공",
            LocalDateTime.now(),
            reportDTO
        );
    }
}


