package com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoadmapEditorResponse {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String permission;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long roadmap;

    private Long member;

    private Long invitedBy;

}
