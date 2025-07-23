package com.gitsunjaeab.mapick.api.report.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestReportDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerReportDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapReportDTO;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportStatus;
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
public class ReportDetailDTO {

    private Long id;

//    @Size(max = 255)
//    private String reportType;

    private MemberDTO reporter;  // 신고자

    private MemberDTO reportedMember;  // 피신고자

    private String description;

    private RoadmapReportDTO roadmap;

    private MarkerReportDTO marker;

    private QuestReportDTO quest;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportStatus status; // 상태

    private OffsetDateTime createdAt;

    private OffsetDateTime resolvedAt;

    public static ReportDetailDTO of(Report report ) {
        return ReportDetailDTO.builder()
                .id(report.getId())
                .description(report.getDescription())
                .status(report.getStatus())
                .resolvedAt(report.getResolvedAt())
                .createdAt(report.getCreatedAt())
                .resolvedAt(report.getResolvedAt())
                .reporter(MemberDTO.of(report.getReporter()))
                .reportedMember(MemberDTO.of(report.getReportedMember()))
                .quest(report.getQuest() != null ? QuestReportDTO.of(report.getQuest()) : null)
                .marker(report.getMarker() != null ? MarkerReportDTO.of(report.getMarker()) : null)
                .roadmap(report.getRoadmap() != null ? RoadmapReportDTO.of(report.getRoadmap()) : null)
                .build();
    }
}
