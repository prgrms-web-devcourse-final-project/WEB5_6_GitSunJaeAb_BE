package com.gitsunjaeab.mapick.api.report.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportSimpleDTO {

    private Long id;

    private MemberSimpleDTO reporter;  // 신고자

    private MemberSimpleDTO reportedMember;  // 피신고자

    private String description;

    private Long roadmap;

    private Long marker;

    private Long quest;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportStatus status; // 상태

    private OffsetDateTime createdAt;

    private OffsetDateTime resolvedAt;

    public static ReportSimpleDTO of(Report report) {
        return ReportSimpleDTO.builder()
                .id(report.getId())
                .reporter(MemberSimpleDTO.of(report.getReporter()))
                .reportedMember(MemberSimpleDTO.of(report.getReportedMember()))
                .roadmap(report.getRoadmap() != null ? report.getRoadmap().getId() : null)
                .marker(report.getMarker() != null ? report.getMarker().getId() : null)
                .quest(report.getQuest() != null ? report.getQuest().getId() : null)
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .resolvedAt(report.getResolvedAt())
                .build();
    }
}
