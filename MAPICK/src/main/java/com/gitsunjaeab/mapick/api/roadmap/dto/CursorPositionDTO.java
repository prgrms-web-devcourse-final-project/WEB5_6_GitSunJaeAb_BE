package com.gitsunjaeab.mapick.api.roadmap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursorPositionDTO {
    private Long userId;
    private String nickname; // 식별자용
    private double x;
    private double y;
}