package com.gitsunjaeab.mapick.api.report.dto;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import com.gitsunjaeab.mapick.api.member.dto.MemberListDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberListResponse;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.report.Report;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 신고 목록(List) 반환 Response
 */
@Getter
@AllArgsConstructor
public class ReportListResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<ReportDTO> reports;

    public static ReportListResponse of(List<Report> reportEntities){
        List<ReportDTO> reportDTOS = reportEntities.stream()
            .map(r -> new ReportDTO(
                r.getId(),
                r.getReporter().getId(),
                r.getReportedMember().getId(),
                r.getDescription(),
                r.getRoadmap() != null ? r.getRoadmap().getId() : null,
                r.getMarker() != null ? r.getMarker().getId() : null,
                r.getQuest() != null ? r.getQuest().getId() : null,
                r.getStatus(),
                r.getCreatedAt(),
                r.getResolvedAt()
            )).toList();

        return new ReportListResponse(
            ResponseCode.OK.getCode(),
            "전체 신고내역 조회 성공",
            LocalDateTime.now(),
            reportDTOS
        );
    }
}
