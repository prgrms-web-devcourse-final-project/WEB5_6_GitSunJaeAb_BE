package com.gitsunjaeab.mapick.application.api.report.dto.internal;

import com.gitsunjaeab.mapick.application.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.application.api.quest.dto.internal.QuestReportDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.internal.MarkerReportDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapReportDTO;
import com.gitsunjaeab.mapick.application.domain.report.Report;
import com.gitsunjaeab.mapick.application.domain.report.code.ReportStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    private Long id;

//    @Size(max = 255)
//    private String reportType;

    private MemberSimpleDTO reporter;  // 신고자

    private MemberSimpleDTO reportedMember;  // 피신고자

    private String description;

    private RoadmapReportDTO roadmap;

    private MarkerReportDTO marker;

    private QuestReportDTO quest;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportStatus status; // 상태

    private OffsetDateTime createdAt;

    private OffsetDateTime resolvedAt;

    public static ReportDTO of(Report report) {
        return ReportDTO.builder()
                .id(report.getId())
                .reporter(MemberSimpleDTO.of(report.getReporter()))
                .reportedMember(MemberSimpleDTO.of(report.getReportedMember()))
                .roadmap(report.getRoadmap() != null ? RoadmapReportDTO.of(report.getRoadmap()) : null)
                .marker(report.getMarker() != null ? MarkerReportDTO.of(report.getMarker()) : null)
                .quest(report.getQuest() != null ? QuestReportDTO.of(report.getQuest()) : null)
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .resolvedAt(report.getResolvedAt())
                .build();
    }
}
