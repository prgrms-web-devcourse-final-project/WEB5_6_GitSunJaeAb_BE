package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class MarkerCreateRequest {

    @Size(max = 255)
    private String name;

    private String description;

    private String address;

    private Double lat;

    private Double lng;

    @Size(max = 255)
    private String color;

    private Integer markerSeq;

    private Long layerId;

    public Marker toEntity(Layer layer, Member member) {
        return Marker.builder()
            .name(name)
            .description(description)
            .address(address)
            .lat(lat)
            .lng(lng)
            .color(color)
            .markerSeq(markerSeq)
            .layer(layer)
            .member(member)
            .createdAt(OffsetDateTime.now())
            .build();
    }
}