package com.gitsunjaeab.mapick.api.report.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapDTO;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class ReportDetailResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;
    private ReportDetailInfo reportDetail;

    public ReportDetailResponse(String code, String message, LocalDateTime timestamp, ReportDetailInfo reportDetail) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.reportDetail = reportDetail;
    }

    @Getter
    @Setter
    public static class ReportDetailInfo {
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

    public static ReportDetailResponse of(Report report) {
        ReportDetailInfo info = new ReportDetailInfo();
        info.setId(report.getId());
        info.setDescription(report.getDescription());
        info.setStatus(report.getStatus().name());
        info.setCreatedAt(report.getCreatedAt());
        info.setResolvedAt(report.getResolvedAt());
        info.setReporter(report.getReporter() == null ? null : new MemberSimpleDTO(report.getReporter()));
        info.setReportedMember(report.getReportedMember() == null ? null : new MemberSimpleDTO(report.getReportedMember()));
        info.setRoadmap(report.getRoadmap() == null ? null : new RoadmapDTO(report.getRoadmap()));
        info.setMarker(report.getMarker() == null ? null : new MarkerDTO(report.getMarker()));
        info.setQuest(report.getQuest() == null ? null : new QuestDTO(report.getQuest()));

        return new ReportDetailResponse(
            ResponseCode.OK.getCode(),
            "신고 상세 조회 성공",
            LocalDateTime.now(),
            info
        );
    }
}


