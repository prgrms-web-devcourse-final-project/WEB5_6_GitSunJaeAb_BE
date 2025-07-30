package com.gitsunjaeab.mapick.api.roadmap.dto.marker;

public interface MarkerRequest {
    String getName();
    String getDescription();
    String getAddress();
    Double getLat();
    Double getLng();
    String getColor();
    Integer getMarkerSeq();
}
