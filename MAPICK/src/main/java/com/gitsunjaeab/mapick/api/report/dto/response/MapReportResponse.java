package com.gitsunjaeab.mapick.api.report.dto.response;

import com.gitsunjaeab.mapick.api.report.dto.internal.ReportDTO;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MapReportResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private ReportDTO reportDTO;

    public static MapReportResponse of(ReportDTO reportDTO) {

        return new MapReportResponse(
            ResponseCode.OK.getCode(),
            "지도 신고 성공",
            OffsetDateTime.now(),
            reportDTO
        );
    }
}


