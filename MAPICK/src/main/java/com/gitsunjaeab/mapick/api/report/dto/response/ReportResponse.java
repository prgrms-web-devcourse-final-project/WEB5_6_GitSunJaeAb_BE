package com.gitsunjaeab.mapick.api.report.dto.response;

import com.gitsunjaeab.mapick.api.report.dto.ReportDetailDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private ReportDetailDTO reportDetailDTO;

    public static ReportResponse of(ReportDetailDTO reportDetailDTO) {

        return new ReportResponse(
            ResponseCode.OK.getCode(),
            "신고 상세 조회 성공",
            OffsetDateTime.now(),
            reportDetailDTO
        );
    }
}


