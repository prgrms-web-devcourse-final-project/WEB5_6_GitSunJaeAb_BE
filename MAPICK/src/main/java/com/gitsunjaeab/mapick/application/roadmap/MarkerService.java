package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCreateRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerUpdateRequest;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final SupabaseStorageService supabaseStorageService;
    private final LayerRepository layerRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(final Long memberId, final MarkerCreateRequest request, final MultipartFile imageFile) {
        Layer layer = layerRepository.findById(request.getLayerId())
            .orElseThrow(() -> new NotFoundException("해당 레이어가 존재하지 않습니다."));

        Member member = Optional.ofNullable(memberId)
            .flatMap(memberRepository::findById)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

        final Marker marker = request.toEntity(layer, member);
        uploadAndSetMarkerImage(marker, imageFile);

        markerRepository.save(marker);
    }

    public MarkerDTO get(final Long markerId) {
        Marker marker = markerRepository.findById(markerId)
            .orElseThrow(() -> new NotFoundException("해당 마커가 존재하지 않습니다."));

        return new MarkerDTO(marker);
    }

    @Transactional
    public void update(final Long markerId, final MarkerUpdateRequest request, final MultipartFile imageFile) {
        final Marker marker = markerRepository.findById(markerId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 마커 ID 입니다."));

        applyUpdateRequestToMarker(marker, request);
        uploadAndSetMarkerImage(marker, imageFile);
    }

    @Transactional
    public void delete(final Long id) {
        markerRepository.deleteById(id);
    }

    private void uploadAndSetMarkerImage(Marker marker, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            marker.setMarkerImage(imageUrl);
        }
    }

    private void applyUpdateRequestToMarker(Marker marker, MarkerUpdateRequest request) {
        if (request.getName() != null) marker.setName(request.getName());
        if (request.getDescription() != null) marker.setDescription(request.getDescription());
        if (request.getLat() != null) marker.setLat(request.getLat());
        if (request.getLng() != null) marker.setLng(request.getLng());
        if (request.getColor() != null) marker.setColor(request.getColor());
        if (request.getMarkerSeq() != null) marker.setMarkerSeq(request.getMarkerSeq());

        marker.setUpdatedAt(OffsetDateTime.now());
    }


}
