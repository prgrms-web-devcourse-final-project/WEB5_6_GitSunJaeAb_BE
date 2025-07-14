package com.gitsunjaeab.mapick.api.report.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.domain.report.Report;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ReportDetailDTO {

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

    public static ReportDetailDTO fromEntity(Report report) {
        return new ReportDetailDTO(
            report.getId(),
            report.getDescription(),
            report.getStatus().name(),
            report.getCreatedAt(),
            report.getResolvedAt(),
            report.getReporter() == null ? null : new MemberSimpleDTO(report.getReporter()),
            report.getReportedMember() == null ? null : new MemberSimpleDTO(report.getReportedMember()),
            report.getRoadmap() == null ? null : new RoadmapDTO(report.getRoadmap()),
            report.getMarker() == null ? null : new MarkerDTO(report.getMarker()),
            report.getQuest() == null ? null : new QuestDTO(report.getQuest())
        );
    }
}
