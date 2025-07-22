package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayerSimpleDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    private Integer layerSeq;

    private LocalDate layerTime;

    @NotNull
    private OffsetDateTime createdAt;

    // 생성자
    public LayerSimpleDTO(Layer layer) {
        this.id = layer.getId();
        this.name = layer.getName();
        this.description = layer.getDescription();
        this.layerSeq = layer.getLayerSeq();
        this.layerTime = layer.getLayerTime();
        this.createdAt = layer.getCreatedAt();
    }

    // 정적 팩토리 메서드
    public static LayerSimpleDTO from(Layer layer) {
        if (layer == null) return null;
        return new LayerSimpleDTO(layer);
    }
} 