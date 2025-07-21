package com.gitsunjaeab.mapick.api.roadmap.dto.layer;

import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayerRequest {

    // 레이어 필드만
    @NotNull
    @Size(max = 255)
    private String name;

    private String description;
    private Integer layerSeq;
    private LocalDate layerTime;


    // Entity 변환 메서드
    public Layer toEntity(Member member, Roadmap roadmap) {
        Layer layer = new Layer();
        layer.setName(this.name);
        layer.setDescription(this.description);
        layer.setLayerSeq(this.layerSeq);
        layer.setLayerTime(this.layerTime);

        layer.setMember(member);
        layer.setRoadmap(roadmap);
        return layer;
    }
}


