package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCreateRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCustomImageDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerUpdateRequest;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.MarkerSocketCreateRequest;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.MarkerSocketDTO;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.MarkerSocketUpdateRequest;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerCustomImage;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerCustomImageRepository;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import com.gitsunjaeab.mapick.util.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final MarkerCustomImageRepository markerCustomImageRepository;

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

    @Transactional
    public Marker createFromSocket(MarkerSocketDTO dto) {
        Layer layer = layerRepository.findById(dto.getLayerId())
                .orElseThrow(() -> new NotFoundException("해당 레이어가 존재하지 않습니다."));

        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        MarkerCustomImage customImage = null;
        if (dto.getCustomImageId() != null && dto.getCustomImageId() > 0) {
            customImage = markerCustomImageRepository.findById(dto.getCustomImageId())
                    .orElseThrow(() -> new NotFoundException("해당 커스텀 마커 이미지가 존재하지 않습니다."));
        }

        Marker marker = new Marker();
        marker.setLayer(layer);
        marker.setMember(member);
        marker.setName(dto.getName());
        marker.setDescription(dto.getDescription());
        marker.setAddress(dto.getAddress());
        marker.setLat(dto.getLat());
        marker.setLng(dto.getLng());
        marker.setColor(dto.getColor());
        marker.setMarkerSeq(dto.getMarkerSeq());
        marker.setCustomImage(customImage);
        marker.setCreatedAt(OffsetDateTime.now());

        return markerRepository.save(marker);
    }

    @Transactional
    public Marker updateFromSocket(MarkerSocketDTO dto) {
        Marker marker = markerRepository.findByIdWithLayerAndRoadmap(dto.getMarkerId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 마커입니다."));

        if (dto.getName() != null) marker.setName(dto.getName());
        if (dto.getDescription() != null) marker.setDescription(dto.getDescription());
        if (dto.getAddress() != null) marker.setAddress(dto.getAddress());
        if (dto.getLat() != null) marker.setLat(dto.getLat());
        if (dto.getLng() != null) marker.setLng(dto.getLng());
        if (dto.getColor() != null) marker.setColor(dto.getColor());
        if (dto.getMarkerSeq() != null) marker.setMarkerSeq(dto.getMarkerSeq());

        if (dto.getCustomImageId() != null) {
            MarkerCustomImage customImage = markerCustomImageRepository.findById(dto.getCustomImageId())
                    .orElseThrow(() -> new NotFoundException("해당 커스텀 이미지가 존재하지 않습니다."));
            marker.setCustomImage(customImage);
        }

        marker.setUpdatedAt(OffsetDateTime.now());

        return markerRepository.save(marker);
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
