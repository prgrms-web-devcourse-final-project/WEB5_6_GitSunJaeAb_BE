package com.gitsunjaeab.mapick.api.report.dto.response;

import com.gitsunjaeab.mapick.api.report.dto.ReportDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class QuestReportResponse implements BaseApiResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private ReportDTO reportDTO;

    public static QuestReportResponse of(ReportDTO reportDTO) {

        return new QuestReportResponse(
            ResponseCode.OK.getCode(),
            "퀘스트 신고 성공",
            OffsetDateTime.now(),
            reportDTO
        );
    }
}


