package com.gitsunjaeab.mapick.api.report.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.report.dto.ReportDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.report.Report;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 신고 목록(List) 반환 Response
 */

@Getter
@AllArgsConstructor
public class ReportListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<ReportDTO> reportDTOS;

    public static ReportListResponse of(List<ReportDTO> reportDTOS){


        return new ReportListResponse(
            ResponseCode.OK.getCode(),
            "전체 신고내역 조회 성공",
            LocalDateTime.now(),
            reportDTOS
        );
    }
}
