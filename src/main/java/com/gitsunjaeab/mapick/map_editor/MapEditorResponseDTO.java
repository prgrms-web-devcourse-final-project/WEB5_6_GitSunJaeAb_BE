package com.gitsunjaeab.mapick.map_editor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MapEditorResponseDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String permission;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long map;

    private Long member;

    private Long invitedBy;

}
