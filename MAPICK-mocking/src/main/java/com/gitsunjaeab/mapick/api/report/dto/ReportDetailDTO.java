package com.gitsunjaeab.mapick.api.report.dto;

import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
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

    private MemberDTO reporter;

    private MemberDTO reportedMember;

    private RoadmapDTO roadmap;
    private MarkerDTO marker;
    private QuestDTO quest;
}
