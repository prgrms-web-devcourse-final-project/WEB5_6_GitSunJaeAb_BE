package com.gitsunjaeab.mapick.api.report.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReportResponse {

    private Long id;

//    @Size(max = 255)
//    private String reportType;

    private String description;

    @NotNull
    private String status;

    private OffsetDateTime createdAt;

    private OffsetDateTime resolvedAt;

    private Long reporter;

    private Long reportedMember;

    private Long roadmap;

    private Long marker;

    private Long quest;

}
