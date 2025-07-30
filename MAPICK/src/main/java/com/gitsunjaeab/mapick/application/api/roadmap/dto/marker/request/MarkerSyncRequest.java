package com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.request;

import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.Marker;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.MarkerCustomImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkerSyncRequest implements MarkerRequest{
    private String action;            // "create", "update", "delete"
    private Long markerTempId;           // 클라이언트에서 발급한 임시 ID
    private Long memberId;
    private String name;
    private String description;
    private String address;
    private String color;
    private Double lat;
    private Double lng;
    private Integer markerSeq;

    private Long layerTempId;

    private Long customImageId;

    public Marker toEntity(Layer layer, Member member, MarkerCustomImage customImage) {
        return Marker.builder()
                .name(name)
                .markerTempId(markerTempId)
                .description(description)
                .customImage(customImage)
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