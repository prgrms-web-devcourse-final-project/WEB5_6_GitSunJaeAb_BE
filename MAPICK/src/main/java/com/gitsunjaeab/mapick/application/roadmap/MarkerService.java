package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCreateRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerCustomImageDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.marker.MarkerUpdateRequest;
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
            .collect(Collectors.toList());
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

        final Marker marker = request.toEntity(layer, member);

        markerRepository.save(marker);
    }

    public MarkerDTO get(final Long markerId) {
        Marker marker = markerRepository.findById(markerId)
            .orElseThrow(() -> new NotFoundException("해당 마커가 존재하지 않습니다."));

        return new MarkerDTO(marker);
    }

    @Transactional
    public void update(final Long markerId, final MarkerUpdateRequest request) {
        final Marker marker = markerRepository.findById(markerId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 마커 ID 입니다."));

        applyUpdateRequestToMarker(marker, request);
    }

    @Transactional
    public void delete(final Long id) {
        markerRepository.deleteById(id);
    }

    private void applyUpdateRequestToMarker(Marker marker, MarkerUpdateRequest request) {
        if (request.getName() != null) marker.setName(request.getName());
        if (request.getDescription() != null) marker.setDescription(request.getDescription());
        if (request.getAddress() != null) marker.setAddress(request.getAddress());
        if (request.getLat() != null) marker.setLat(request.getLat());
        if (request.getLng() != null) marker.setLng(request.getLng());
        if (request.getColor() != null) marker.setColor(request.getColor());
        if (request.getMarkerSeq() != null) marker.setMarkerSeq(request.getMarkerSeq());

        marker.setUpdatedAt(OffsetDateTime.now());
    }
}
