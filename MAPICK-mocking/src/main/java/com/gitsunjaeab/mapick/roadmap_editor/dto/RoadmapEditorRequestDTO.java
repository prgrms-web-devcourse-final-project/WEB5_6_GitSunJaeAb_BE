package com.gitsunjaeab.mapick.roadmap_editor.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoadmapEditorRequestDTO {

//    private Long id;

    @NotNull
    @Size(max = 255)
    private String permission;

//    @NotNull
//    private OffsetDateTime createdAt;
//
//    private OffsetDateTime updatedAt;
//
//    private OffsetDateTime deletedAt;
//
//    private Long map;
//
//    private Long member;

//    private Long invitedBy;

}