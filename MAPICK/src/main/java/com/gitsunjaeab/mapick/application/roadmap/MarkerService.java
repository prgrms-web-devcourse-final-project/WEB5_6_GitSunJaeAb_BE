package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCreateRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCustomImageDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerSyncRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerUpdateRequest;
import com.gitsunjaeab.mapick.common.EntityFinder;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerCustomImage;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerCustomImageRepository;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import java.time.OffsetDateTime;
import java.util.List;
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
    private final MarkerCustomImageRepository markerCustomImageRepository;
    private final EntityFinder entityFinder;

    // ===== 실시간 공유지도 상 CRUD =====\
    @Transactional
    public Marker createFromSync(MarkerSyncRequest request) {
        Member member = entityFinder.findMemberById(request.getMemberId());
        Layer layer = entityFinder.findLayerByTempId(request.getLayerTempId());

        MarkerCustomImage customImage = null;
        if (request.getCustomImageId() != null) {
            customImage = entityFinder.findByMarkerCustomId(request.getCustomImageId());
        }

        Marker marker = new Marker();
        marker.setName(request.getName());
        marker.setMarkerTempId(request.getMarkerTempId());
        marker.setDescription(request.getDescription());
        marker.setAddress(request.getAddress());
        marker.setLat(request.getLat());
        marker.setLng(request.getLng());
        marker.setColor(request.getColor());
        marker.setMarkerSeq(request.getMarkerSeq());
        marker.setLayer(layer);
        marker.setMember(member);
        marker.setCustomImage(customImage);
        marker.setCreatedAt(OffsetDateTime.now());

        Marker saved = markerRepository.save(marker);
        markerRepository.flush();

        roadmapEditorService.registerEditorIfNotExists(layer.getRoadmap().getId(), member.getId());

        return saved;
    }

    @Transactional
    public Marker updateFromSync(final MarkerSyncRequest request) {
        final Marker marker = entityFinder.findByMarkerTempId(request.getMarkerTempId());

        MarkerCustomImage newCustomImage = null;
        if (request.getCustomImageId() != null && !request.getCustomImageId().toString().isBlank()) {
            newCustomImage = entityFinder.findByMarkerCustomId(request.getCustomImageId());
        }

        applySyncRequestToMarker(marker, request, newCustomImage);

        Long roadmapId = marker.getLayer().getRoadmap().getId();
        Long memberId = marker.getMember().getId();
        roadmapEditorService.registerEditorIfNotExists(roadmapId, memberId);
        return marker;
    }

    @Transactional
    public Marker deleteFromSync(MarkerSyncRequest request) {
        Marker marker = entityFinder.findByMarkerTempId(request.getMarkerTempId());
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
    public void createCustomImage(final String name, final MultipartFile imageFile) {
        final MarkerCustomImage customImage = new MarkerCustomImage();
        customImage.setName(name);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            customImage.setMarkerImage(imageUrl);
        }

        markerCustomImageRepository.save(customImage);
    }

    public List<MarkerCustomImageDTO> getCustomImages() {
        final List<MarkerCustomImage> customImages = markerCustomImageRepository.findAll();

        return customImages.stream()
            .map(MarkerCustomImageDTO::of)
            .toList();
    }

    @Transactional
    public void updateCustomImage(final Long customImageId, final String name, final MultipartFile imageFile) {
        final MarkerCustomImage customImage = entityFinder.findByMarkerCustomId(customImageId);

        if (!(name == null))
            customImage.setName(name);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            customImage.setMarkerImage(imageUrl);
        }
    }

    @Transactional
    public void deleteCustomImage(final Long customImageId) {
        markerCustomImageRepository.deleteById(customImageId);
    }

    /**
     * 로드맵 위의 마커
     */

    @Transactional
    public void create(final Long memberId, final MarkerCreateRequest request) {
        Layer layer = entityFinder.findLayerById(request.getLayerId());
        Member member = entityFinder.findMemberById(memberId);

        MarkerCustomImage customImage = null;
        if (request.getCustomImageId() != null) {
            customImage = entityFinder.findByMarkerCustomId(request.getCustomImageId());
        }

        final Marker marker = request.toEntity(layer, member, customImage);
        markerRepository.save(marker);
    }

    @Transactional
    public MarkerDTO get(final Long markerId) {
        Marker marker = entityFinder.findByMarkerId(markerId);

        return new MarkerDTO(marker);
    }

    @Transactional
    public void update(final Long markerId, final MarkerUpdateRequest request) {
        final Marker marker = entityFinder.findByMarkerId(markerId);

        MarkerCustomImage newCustomImage = null;
        if (request.getCustomImageId() != null) {
            newCustomImage = entityFinder.findByMarkerCustomId(request.getCustomImageId());
        }

        applySyncRequestToMarker(marker, request, newCustomImage);
    }

    @Transactional
    public void delete(final Long id) {
        markerRepository.deleteById(id);
    }


    private void applySyncRequestToMarker(Marker marker, MarkerUpdateRequest request, MarkerCustomImage customImage) {
        applyRequestToMarker(marker, request, customImage);
    }

    // 실시간 공유지도 협업 시 마커 수정 update
    private void applySyncRequestToMarker(Marker marker, MarkerSyncRequest request, MarkerCustomImage customImage) {
        applyRequestToMarker(marker, request, customImage);
    }

    private void applyRequestToMarker(Marker marker, MarkerRequest request, MarkerCustomImage customImage) {
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

    public Marker findById(Long markerId) {
        return markerRepository.findById(markerId)
                .orElseThrow(() -> new RuntimeException("💥 해당 Marker를 찾을 수 없습니다. markerId: " + markerId));
    }
}
