package com.gitsunjaeab.mapick.api.report.dto.response;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 특정 신고 처리 반환 Response
 */

@Getter
@AllArgsConstructor
public class ReportProcessResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
//    private ReportDTO reportDTO;

    public static ReportProcessResponse of() {

        return new ReportProcessResponse(
            ResponseCode.OK.getCode(),
            "특정 신고 처리 완료",
            OffsetDateTime.now()
//            reportDTO
        );
    }
}
