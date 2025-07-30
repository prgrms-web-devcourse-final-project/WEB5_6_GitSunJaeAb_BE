package com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.request;

public interface MarkerRequest {
    String getName();
    String getDescription();
    String getAddress();
    Double getLat();
    Double getLng();
    String getColor();
    Integer getMarkerSeq();
}
