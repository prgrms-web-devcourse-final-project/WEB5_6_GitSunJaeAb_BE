package com.gitsunjaeab.mapick.api.report.dto.response;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.api.report.dto.ReportDetailDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.report.Report;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ReportResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private ReportDetailDTO reportDetailDTO;

    public static ReportResponse of(ReportDetailDTO reportDetailDTO) {

        return new ReportResponse(
            ResponseCode.OK.getCode(),
            "신고 상세 조회 성공",
            LocalDateTime.now(),
            reportDetailDTO
        );
    }
}


