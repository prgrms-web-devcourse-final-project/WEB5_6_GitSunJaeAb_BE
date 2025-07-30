package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerDetailDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.request.LayerRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.request.LayerSyncRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.response.LayerResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Role;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerLibraryRepository;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LayerService {

    private final MarkerService markerService;
    private final RoadmapEditorService roadmapEditorService;
    private final LayerRepository layerRepository;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final MarkerRepository markerRepository;
    private final LayerLibraryRepository layerLibraryRepository;

    // ===== 실시간 공유지도 상 CRUD =====

    @Transactional
    public LayerResponse createFromSync(LayerSyncRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId())
            .orElseThrow(() -> new IllegalArgumentException("로드맵 없음"));

        Layer layer = new Layer();
        layer.setName(request.getName());
        layer.setDescription(request.getDescription());
        layer.setLayerSeq(request.getLayerSeq());
        layer.setMember(member);
        layer.setRoadmap(roadmap);
        layer.setLayerTempId(request.getLayerTempId());

        Layer saved = layerRepository.save(layer);

        roadmapEditorService.registerEditorIfNotExists(roadmap.getId(), member.getId());

        return LayerResponse.create(saved, false, "레이어 생성 성공");
    }

    @Transactional
    public LayerResponse updateFromSync(LayerSyncRequest request) {
        Layer layer = layerRepository.findByLayerTempId(request.getLayerTempId())
            .orElseThrow(() -> new IllegalArgumentException("레이어를 찾을 수 없음"));

        layer.setName(request.getName());
        layer.setDescription(request.getDescription());
        layer.setLayerSeq(request.getLayerSeq());

        return LayerResponse.update(layerRepository.save(layer), false, "레이어 수정 성공");
    }

    @Transactional
    public LayerResponse deleteByTempId(Long layerTempId) {
        Layer layer = layerRepository.findByLayerTempId(layerTempId)
            .orElseThrow(() -> new IllegalArgumentException("레이어를 찾을 수 없음"));

        if (layer.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.ALREADY_PROCESSED);
        }
        List<Marker> markers = markerRepository.findAllByLayer_Id(layer.getId());

        for (Marker marker : markers) {
            markerService.delete(marker.getId());
        }

        layer.setRoadmap(null);

        ReferencedWarning warning = getReferencedWarning(layer.getId());
        if (warning != null) {
            throw new CommonException(ResponseCode.CONFLICT);
        }

        layerLibraryRepository.softDeleteAllByLayer(layer.getId());
        layer.setDeletedAt(OffsetDateTime.now());

        return LayerResponse.delete(layerRepository.save(layer), false, "레이어 삭제 성공");
    }

    // ===== 기본 CRUD =====

    // 로드맵의 레이어 조회
    // 정적 팩토리 메서드로 구현 완료 todo 예외 처리 필요
    @Transactional(readOnly = true)
    public List<LayerListDTO> findAllLayersOnRoadmap(Long roadmapId) {

        if (roadmapId == null) {// 전체 레이어 조회 (삭제되지 않은 것만 조회) - 모든 연관 엔티티 함께 조회

            List<Layer> layers = layerRepository.findAllNotDeletedWithAssociations();

            // 엔티티 리스트 -> DTO 리스트 변환
            List<LayerListDTO> layerDTOs = layers.stream()
                .map(LayerListDTO::of)
                .toList();

            return layerDTOs;

        } else { // 특정 로드맵 레이어 조회 - 모든 연관 엔티티 함께 조회

            List<Layer> layers = layerRepository.findAllByRoadmap_IdWithAssociations(roadmapId);

            // 엔티티 리스트 -> DTO 리스트 변환
            List<LayerListDTO> layerDTOs = layers.stream()
                .map(LayerListDTO::of)
                .toList();

            return layerDTOs;
        }
    }

    // 레이어 상세 조회 (삭제되지 않은 것만 조회)
    @Transactional(readOnly = true)
    public LayerDetailDTO getLayerDetail(final Long layerId, final Long memberId) {

        final Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        final Layer layer = layerRepository.findByIdWithMember(layerId)
            .orElseThrow(() -> new CommonException(ResponseCode.LAYER_NOT_FOUND));

        // 삭제된 레이어 체크
        if (layer.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }

        // 찜 여부 확인
        boolean isZzim = false;
        isZzim = layerLibraryRepository.existsByMemberAndLayer(member, layer);

        LayerDetailDTO layerDetailDTO = LayerDetailDTO.from(layer, isZzim);

        return layerDetailDTO;
    }

    // 레이어 생성
    public Layer create(final LayerRequest request, Long memberId, Long roadmapId) {

        // 연관 Entity 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        Roadmap roadmap = roadmapRepository.findById(roadmapId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        Layer layer = Layer.fromRequest(request, member, roadmap);

        // 저장 & 생성된 레이어 반환
        return layerRepository.save(layer);
    }

    // 레이어 수정
    @Transactional
    public Layer update(Long id, LayerRequest request, Long memberId) {

        // 기존 레이어 조회 (Member와 함께 조회하여 LazyInitializationException 방지)
        Layer layer = layerRepository.findByIdWithMember(id)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 본인만 수정 가능
        if (!layer.getMember().getId().equals(memberId)) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }

        // 기본 필드들만 업데이트 (member와 roadmap은 기존 값 유지)
        layer.setName(request.getName());
        layer.setDescription(request.getDescription());
        layer.setLayerSeq(request.getLayerSeq());

        // 수정된 레이어 반환
        return layerRepository.save(layer);
    }

    // 레이어 삭제
    @Transactional
    public Layer delete(Long id, Long memberId) {

        Layer layer = layerRepository.findByIdWithMember(id)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 이미 삭제된 레이어 체크
        if (layer.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.ALREADY_PROCESSED);
        }

        boolean isOwner = layer.getMember().getId().equals(memberId);
        boolean isAdmin = isAdmin(memberId);

        if (!(isOwner || isAdmin)) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }

        List<Marker> markers = markerRepository.findAllByLayer_Id(layer.getId());
        for (Marker marker : markers) {
            markerService.delete(marker.getId());
        }

        layer.setRoadmap(null);

        // 참조 무결성 검사
        ReferencedWarning warning = getReferencedWarning(layer.getId());
        if (warning != null) {
            throw new CommonException(ResponseCode.CONFLICT);
        }

        // 삭제 처리
        layerLibraryRepository.softDeleteAllByLayer(layer.getId()); // 찜 기록 소프트 딜리트
        layer.setDeletedAt(java.time.OffsetDateTime.now()); // 레이어 소프트 딜리트
        layerRepository.save(layer);
        layerRepository.flush();

        // 소프트 딜리트된 엔티티 반환
        return layer;
    }


    @Transactional(readOnly = true)
    public Layer findById(Long id) {

        return layerRepository.findById(id)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
    }


    // 관리자 블록 처리
    @Transactional
    public void blockLayer(Layer layer, Long adminId, String reason) {
        // 블록 처리
        layer.setIsBlocked(true);
        layer.setBlockedAt(java.time.OffsetDateTime.now());
        layer.setBlockReason(reason);
        layer.setBlockedBy(adminId);
        layerRepository.save(layer);

        // TODO: 감사 로그 저장
        // auditService.logAdminBlock(layer, adminId, reason);

        // TODO: 사용자 알림 발송
        // notificationService.sendBlockNotification(layer.getMember().getEmail(), 
        //     "관리자가 비활성화한 콘텐츠입니다. 문의사항은 연락주시기 바랍니다.");
    }

    // 관리자 권한 확인
    private boolean isAdmin(Long memberId) {
        return memberRepository.findById(memberId)
            .map(member -> Role.ROLE_ADMIN.name().equals(member.getRole()))
            .orElse(false);
    }

    // ===== 부가 기능 =====

    // 레이어 <-> 마커, 찜 - 참조 무결성 검사
    // 레이어 삭제 전 호출하여 다른 객체가 참조 중이면 삭제 중단
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();

        // 삭제할 내 레이어 조회
        final Layer layer = layerRepository.findById(id)
            .orElseThrow(NotFoundException::new);

        // 해당 레이어를 참조 중인 마커가 하나라도 존재하는지 확인
        final Marker layerMarker = markerRepository.findFirstByLayer(layer);
        if (layerMarker != null) {
            referencedWarning.setKey("layer.marker.layer.referenced");
            referencedWarning.addParam(layerMarker.getId());
            return referencedWarning;
        }

//        // 해당 레이어를 찜한 기록이 하나라도 있는지 확인
//        final List<LayerLibrary> layerLibraries = layerLibraryRepository.findValidZzim(
//            layer.getId());
//        if (!layerLibraries.isEmpty()) {
//            LayerLibrary layerLibrary = layerLibraries.get(0);
//            referencedWarning.setKey("layer.layerLibrary.layer.referenced");
//            referencedWarning.addParam(layerLibrary.getId());
//            return referencedWarning;
//        }

        // 참조 없으면 null 반환 → 삭제 가능
        return null;
    }


}