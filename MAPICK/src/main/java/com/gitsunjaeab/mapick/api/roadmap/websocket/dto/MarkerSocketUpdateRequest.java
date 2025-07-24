package com.gitsunjaeab.mapick.api.roadmap.websocket.dto;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MarkerSocketUpdateRequest {

    private String name;
    private String description;
    private String address;
    private Double lat;
    private Double lng;
    private String color;
    private Integer markerSeq;

    // WebSocket 연동용 필드
    private String tempUUID;
    private Long customImageId;
    private Long layerId;

    public static MarkerSocketUpdateRequest fromSocketDTO(MarkerSocketDTO dto) {
        MarkerSocketUpdateRequest request = new MarkerSocketUpdateRequest();
        request.setName(dto.getName());
        request.setDescription(dto.getDescription());
        request.setAddress(dto.getAddress());
        request.setLat(dto.getLat());
        request.setLng(dto.getLng());
        request.setColor(dto.getColor());
        request.setCustomImageId(dto.getCustomImageId());
        request.setMarkerSeq(dto.getMarkerSeq());
        return request;
    }

    public MarkerUpdateRequest toMarkerUpdateRequest() {
        MarkerUpdateRequest request = new MarkerUpdateRequest();
        request.setName(this.name);
        request.setDescription(this.description);
        request.setAddress(this.address);
        request.setLat(this.lat);
        request.setLng(this.lng);
        request.setColor(this.color);
        request.setCustomImageId(this.customImageId);
        request.setMarkerSeq(this.markerSeq);
        return request;
    }
}
