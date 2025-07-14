package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerListResponse;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final MemberRepository memberRepository;
    private final LayerRepository layerRepository;
    private final ReportRepository reportRepository;

    public MarkerService(final MarkerRepository markerRepository,
            final MemberRepository memberRepository, final LayerRepository layerRepository,
            final ReportRepository reportRepository) {
        this.markerRepository = markerRepository;
        this.memberRepository = memberRepository;
        this.layerRepository = layerRepository;
        this.reportRepository = reportRepository;
    }

    @Transactional(readOnly = true)
    public MarkerListResponse findAllMarkersOnLayer(Long layerId) {
        final List<Marker> markers = markerRepository.findAllByLayer_Id(layerId);
        return MarkerListResponse.of(markers);
    }

    public MarkerDTO get(final Long id) {
        return markerRepository.findById(id)
                .map(marker -> roadmapToDTO(marker, new MarkerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MarkerDTO markerDTO) {
        final Marker marker = new Marker();
        roadmapToEntity(markerDTO, marker);
        return markerRepository.save(marker).getId();
    }

    public void update(final Long id, final MarkerDTO markerDTO) {
        final Marker marker = markerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(markerDTO, marker);
        markerRepository.save(marker);
    }

    public void delete(final Long id) {
        markerRepository.deleteById(id);
    }

    private MarkerDTO roadmapToDTO(final Marker marker, final MarkerDTO markerDTO) {
        markerDTO.setId(marker.getId());
        markerDTO.setName(marker.getName());
        markerDTO.setDescription(marker.getDescription());
        markerDTO.setLat(marker.getLat());
        markerDTO.setLng(marker.getLng());
        markerDTO.setColor(marker.getColor());
        markerDTO.setImageUrl(marker.getImageUrl());
        markerDTO.setMarkerSeq(marker.getMarkerSeq());
        markerDTO.setCreatedAt(marker.getCreatedAt());
        markerDTO.setUpdatedAt(marker.getUpdatedAt());
        markerDTO.setDeletedAt(marker.getDeletedAt());
        markerDTO.setMember(marker.getMember() == null ? null : marker.getMember().getId());
        markerDTO.setLayer(marker.getLayer() == null ? null : marker.getLayer().getId());
        return markerDTO;
    }

    private Marker roadmapToEntity(final MarkerDTO markerDTO, final Marker marker) {
        marker.setName(markerDTO.getName());
        marker.setDescription(markerDTO.getDescription());
        marker.setLat(markerDTO.getLat());
        marker.setLng(markerDTO.getLng());
        marker.setColor(markerDTO.getColor());
        marker.setImageUrl(markerDTO.getImageUrl());
        marker.setMarkerSeq(markerDTO.getMarkerSeq());
        marker.setCreatedAt(markerDTO.getCreatedAt());
        marker.setUpdatedAt(markerDTO.getUpdatedAt());
        marker.setDeletedAt(markerDTO.getDeletedAt());
        final Member member = markerDTO.getMember() == null ? null : memberRepository.findById(markerDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        marker.setMember(member);
        final Layer layer = markerDTO.getLayer() == null ? null : layerRepository.findById(markerDTO.getLayer())
                .orElseThrow(() -> new NotFoundException("layer not found"));
        marker.setLayer(layer);
        return marker;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Marker marker = markerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Report markerReport = reportRepository.findFirstByMarker(marker);
        if (markerReport != null) {
            referencedWarning.setKey("marker.report.marker.referenced");
            referencedWarning.addParam(markerReport.getId());
            return referencedWarning;
        }
        return null;
    }

}
