package com.gitsunjaeab.mapick.api.report.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestReportDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerReportDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapReportDTO;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportStatus;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
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
