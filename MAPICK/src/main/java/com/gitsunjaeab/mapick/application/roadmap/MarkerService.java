package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.*;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.*;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerRepository;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import com.gitsunjaeab.mapick.util.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MarkerService {

    private final RoadmapEditorService roadmapEditorService;
    private final MarkerRepository markerRepository;
    private final SupabaseStorageService supabaseStorageService;
    private final LayerRepository layerRepository;
    private final MemberRepository memberRepository;
    private final MarkerCustomImageRepository markerCustomImageRepository;

    // ===== 실시간 공유지도 상 CRUD =====

    @Transactional
    public Marker createFromSync(MarkerSyncRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Layer layer = layerRepository.findByLayerTempId(request.getLayerTempId())
                .orElseThrow(() -> new IllegalArgumentException("레이어 없음"));

        MarkerCustomImage customImage = null;
        if (request.getCustomImageId() != null) {
            customImage = markerCustomImageRepository.findById(request.getCustomImageId())
                    .orElseThrow(() -> new NotFoundException("해당 커스텀 마커 이미지가 존재하지 않습니다."));
        }

        final Marker marker = request.toEntity(layer, member, customImage);

        roadmapEditorService.registerEditorIfNotExists(layer.getRoadmap().getId(), member.getId());
        return markerRepository.save(marker);
    }

    @Transactional
    public Marker updateFromSync(final MarkerSyncRequest request) {
        final Marker marker = markerRepository.findByMarkerTempId(request.getMarkerTempId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 markerTempId 입니다."));

        MarkerCustomImage newCustomImage = null;
        if (request.getCustomImageId() != null && !request.getCustomImageId().toString().isBlank()) {
            newCustomImage = markerCustomImageRepository.findById(request.getCustomImageId())
                    .orElseThrow(() -> new NotFoundException("해당 커스텀 마커 이미지가 존재하지 않습니다."));
        }

        applyUpdateRequestToMarker(marker, request, newCustomImage);

        Long roadmapId = marker.getLayer().getRoadmap().getId();
        Long memberId = marker.getMember().getId();
        roadmapEditorService.registerEditorIfNotExists(roadmapId, memberId);
        return marker;
    }

    @Transactional
    public Marker deleteFromSync(MarkerSyncRequest request) {
        Marker marker = markerRepository.findByMarkerTempId(request.getMarkerTempId())
                .orElseThrow(() -> new IllegalArgumentException("마커 없음"));
        markerRepository.delete(marker);

        Long roadmapId = marker.getLayer().getRoadmap().getId();
        Long memberId = marker.getMember().getId();
        roadmapEditorService.registerEditorIfNotExists(roadmapId, memberId);
        return marker;
    }

    /**
     * 관리자 커스텀 이미지 마커
     */

    @Transactional
    public void createCustomImage(String name, MultipartFile imageFile) {
        MarkerCustomImage customImage = new MarkerCustomImage();
        customImage.setName(name);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            customImage.setMarkerImage(imageUrl);
        }

        markerCustomImageRepository.save(customImage);
    }

    public List<MarkerCustomImageDTO> getCustomImages() {
        List<MarkerCustomImage> customImages = markerCustomImageRepository.findAll();

        return customImages.stream()
            .map(MarkerCustomImageDTO::of)
            .toList();
    }

    @Transactional
    public void updateCustomImage(Long customImageId, String name, MultipartFile imageFile) {
        MarkerCustomImage customImage = markerCustomImageRepository.findById(customImageId)
            .orElseThrow(() -> new EntityNotFoundException("해당 커스텀 이미지를 찾을 수 없습니다."));

        if (!(name == null))
            customImage.setName(name);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            customImage.setMarkerImage(imageUrl);
        }
    }

    public void deleteCustomImage(Long customImageId) {
        markerCustomImageRepository.deleteById(customImageId);
    }

    /**
     * 로드맵 위의 마커
     */

    @Transactional
    public void create(final Long memberId, final MarkerCreateRequest request) {
        Layer layer = layerRepository.findById(request.getLayerId())
            .orElseThrow(() -> new NotFoundException("해당 레이어가 존재하지 않습니다."));

        Member member = Optional.ofNullable(memberId)
            .flatMap(memberRepository::findById)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

        MarkerCustomImage customImage = null;
        if (request.getCustomImageId() != null) {
            customImage = markerCustomImageRepository.findById(request.getCustomImageId())
                .orElseThrow(() -> new NotFoundException("해당 커스텀 마커 이미지가 존재하지 않습니다."));
        }

        final Marker marker = request.toEntity(layer, member, customImage);
        markerRepository.save(marker);
    }

    @Transactional
    public MarkerDTO get(final Long markerId) {
        Marker marker = markerRepository.findById(markerId)
            .orElseThrow(() -> new NotFoundException("해당 마커가 존재하지 않습니다."));

        return new MarkerDTO(marker);
    }

    @Transactional
    public void update(final Long markerId, final MarkerUpdateRequest request) {
        final Marker marker = markerRepository.findById(markerId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 마커 ID 입니다."));

        MarkerCustomImage newCustomImage = null;
        if (request.getCustomImageId() != null) {
            newCustomImage = markerCustomImageRepository.findById(request.getCustomImageId())
                .orElseThrow(() -> new NotFoundException("해당 커스텀 마커 이미지가 존재하지 않습니다."));
        }

        applyUpdateRequestToMarker(marker, request, newCustomImage);
    }


    @Transactional
    public void delete(final Long id) {
        markerRepository.deleteById(id);
    }

    private void applyUpdateRequestToMarker(Marker marker, MarkerUpdateRequest request, MarkerCustomImage customImage) {
        if (request.getName() != null) marker.setName(request.getName());
        if (request.getDescription() != null) marker.setDescription(request.getDescription());
        if (request.getAddress() != null) marker.setAddress(request.getAddress());
        if (request.getLat() != null) marker.setLat(request.getLat());
        if (request.getLng() != null) marker.setLng(request.getLng());
        marker.setColor(request.getColor());
        if (request.getMarkerSeq() != null) marker.setMarkerSeq(request.getMarkerSeq());
        marker.setCustomImage(customImage);

        marker.setUpdatedAt(OffsetDateTime.now());
    }

    // 실시간 공유지도 협업 시 마커 수정 update
    private void applyUpdateRequestToMarker(Marker marker, MarkerSyncRequest request, MarkerCustomImage customImage) {
        if (request.getName() != null) marker.setName(request.getName());
        if (request.getDescription() != null) marker.setDescription(request.getDescription());
        if (request.getAddress() != null) marker.setAddress(request.getAddress());
        if (request.getLat() != null) marker.setLat(request.getLat());
        if (request.getLng() != null) marker.setLng(request.getLng());
        marker.setColor(request.getColor());
        if (request.getMarkerSeq() != null) marker.setMarkerSeq(request.getMarkerSeq());
        marker.setCustomImage(customImage);

        marker.setUpdatedAt(OffsetDateTime.now());
    }

    public Long findRoadmapIdByLayerId(Long layerId) {
        Layer layer = layerRepository.findById(layerId)
                .orElseThrow(() -> new RuntimeException("💥 해당 Layer를 찾을 수 없습니다. layerId: " + layerId));

        return layer.getRoadmap().getId();
    }

    public Marker findById(Long markerId) {
        return markerRepository.findById(markerId)
                .orElseThrow(() -> new RuntimeException("💥 해당 Marker를 찾을 수 없습니다. markerId: " + markerId));
    }

    @Transactional
    public Marker findByIdWithLayerAndRoadmap(Long markerId) {
        return markerRepository.findByIdWithLayerAndRoadmap(markerId)
                .orElseThrow(() -> new NotFoundException("마커 조회 실패"));
    }
}
