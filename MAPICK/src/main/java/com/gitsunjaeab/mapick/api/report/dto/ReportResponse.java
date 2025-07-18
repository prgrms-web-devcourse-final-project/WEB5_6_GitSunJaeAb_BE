package com.gitsunjaeab.mapick.api.report.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.report.Report;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ReportResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private ReportInfo report;

    public ReportResponse(String code, String message, LocalDateTime timestamp, ReportInfo report) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.report = report;
    }

    @Getter
    @Setter
    public static class ReportInfo {
        private Long id;
        private String description;

        @NotNull
        @Size(max = 255)
        private String status;

        private OffsetDateTime createdAt;
        private OffsetDateTime resolvedAt;

        private MemberSimpleDTO reporter;
        private MemberSimpleDTO reportedMember;

        private RoadmapDTO roadmap;
        private MarkerDTO marker;
        private QuestDTO quest;
    }

    public static ReportResponse of(Report report) {
        ReportInfo reportInfo = new ReportInfo();
        reportInfo.setId(report.getId());
        reportInfo.setDescription(report.getDescription());
        reportInfo.setStatus(report.getStatus().name());
        reportInfo.setCreatedAt(report.getCreatedAt());
        reportInfo.setResolvedAt(report.getResolvedAt());
        reportInfo.setReporter(new MemberSimpleDTO(report.getReporter()));
        reportInfo.setReportedMember(new MemberSimpleDTO(report.getReportedMember()));
        reportInfo.setRoadmap(report.getRoadmap() == null ? null : new RoadmapDTO(report.getRoadmap()));
        reportInfo.setMarker(report.getMarker() == null ? null : new MarkerDTO(report.getMarker()));
        reportInfo.setQuest(report.getQuest() == null ? null : new QuestDTO(report.getQuest()));

        return new ReportResponse(
            ResponseCode.OK.getCode(),
            "신고 상세 조회 성공",
            LocalDateTime.now(),
            reportInfo
        );
    }
}


