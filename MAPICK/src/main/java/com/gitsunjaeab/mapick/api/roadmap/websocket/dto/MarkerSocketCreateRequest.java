package com.gitsunjaeab.mapick.api.roadmap.websocket.dto;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCreateRequest;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerCustomImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MarkerSocketCreateRequest {

    private String name;
    private String description;
    private String address;
    private Double lat;
    private Double lng;
    private String color;
    private Integer markerSeq;

    private Long customImageId;
    private Long layerId;

    public Marker toEntity(Layer layer, Member member, MarkerCustomImage customImage) {
        Marker marker = new Marker();
        marker.setLayer(layer);
        marker.setMember(member);
        marker.setCustomImage(customImage);

        marker.setName(this.name);
        marker.setDescription(this.description);
        marker.setAddress(this.address);
        marker.setLat(this.lat);
        marker.setLng(this.lng);
        marker.setColor(this.color);
        marker.setMarkerSeq(this.markerSeq);


        return marker;
    }

    public static MarkerSocketCreateRequest fromSocketDTO(MarkerSocketDTO dto) {
        MarkerSocketCreateRequest request = new MarkerSocketCreateRequest();
        request.setLayerId(dto.getLayerId());
        request.setName(dto.getName());
        request.setDescription(dto.getDescription());
        request.setAddress(dto.getAddress());
        request.setLat(dto.getLat());
        request.setLng(dto.getLng());
        request.setColor(dto.getColor());
        request.setMarkerSeq(dto.getMarkerSeq());
        return request;
    }

    public MarkerCreateRequest toMarkerCreateRequest() {
        MarkerCreateRequest request = new MarkerCreateRequest();
        request.setName(this.name);
        request.setDescription(this.description);
        request.setAddress(this.address);
        request.setLat(this.lat);
        request.setLng(this.lng);
        request.setColor(this.color);
        request.setCustomImageId(this.customImageId);
        request.setMarkerSeq(this.markerSeq);
        request.setLayerId(this.layerId);
        return request;
    }
}
