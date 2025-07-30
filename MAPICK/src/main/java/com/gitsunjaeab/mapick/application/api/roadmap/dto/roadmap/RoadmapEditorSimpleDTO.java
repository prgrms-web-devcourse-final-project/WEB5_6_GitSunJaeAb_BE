package com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoadmapEditorSimpleDTO {
    private Long memberId;
    private String name;
    private String nickname;
    private String profileImage;
}
