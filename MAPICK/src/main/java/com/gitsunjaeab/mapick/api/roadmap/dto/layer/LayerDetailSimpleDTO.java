package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LayerDetailSimpleDTO {
    
    private Layer layer;
    private boolean isZzim;

    public LayerDetailSimpleDTO(Layer layer, boolean isZzim) {
        this.layer = layer;
        this.isZzim = isZzim;
    }
} 