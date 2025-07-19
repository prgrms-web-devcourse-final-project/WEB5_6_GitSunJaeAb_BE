package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerResponse;
import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.Role;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import jakarta.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class LayerService {

    private final LayerRepository layerRepository;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final MarkerRepository markerRepository;
    private final LayerLibraryRepository layerLibraryRepository;

    public LayerService(final LayerRepository layerRepository,
        final MemberRepository memberRepository, final RoadmapRepository roadmapRepository,
        final MarkerRepository markerRepository,
        final LayerLibraryRepository layerLibraryRepository, MemberService memberService) {
        this.layerRepository = layerRepository;
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
        this.markerRepository = markerRepository;
        this.layerLibraryRepository = layerLibraryRepository;
    }


    // ===== 기본 CRUD =====

    // 로드맵의 레이어 조회
    @Transactional(readOnly = true)
    public LayerListResponse findAllLayersOnRoadmap(Long roadmapId) {
        final List<Layer> layers;
        if (roadmapId == null) {
            // 전체 레이어 조회 (소프트 딜리트 제외)
            layers = layerRepository.findAllNotDeleted();
        } else {
            // 특정 로드맵 레이어 조회
            layers = layerRepository.findAllByRoadmap_Id(roadmapId);
        }
        return LayerListResponse.of(layers);
    }

    // 레이어 상세 조회 (삭제되지 않은 것만 조회)
    @Transactional(readOnly = true)
    public LayerResponse getLayerDetail(final Long layerId, final Long memberId) {
        Layer layer = layerRepository.findById(layerId)
            .orElseThrow(() -> new EntityNotFoundException("해당 레이어가 존재하지 않습니다. id=" + layerId));

        // 삭제된 레이어는 조회 불가
        if (layer.getDeletedAt() != null) {
            throw new EntityNotFoundException("삭제된 레이어입니다. id=" + layerId);
        }

        // 현재 로그인한 사용자가 이 레이어를 찜했는지 확인
        boolean isZzim = false;
        if (memberId != null) {
            Member member = memberRepository.findById(memberId)
                .orElse(null);
            if (member != null) {
                isZzim = layerLibraryRepository.existsByMemberAndLayer(member, layer);
            }
        }

        return LayerResponse.of(layer, isZzim);
    }


    // 레이어 생성
    public Long create(final LayerRequest request) {
        // 연관 Entity 조회
        Member member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new NotFoundException("member not found"));
        Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId())
            .orElseThrow(() -> new NotFoundException("roadmapId not found"));

        // 요청 DTO -> Entity 변환
        Layer layer = request.toEntity(member, roadmap);

        // 저장 & ID 반환
        return layerRepository.save(layer).getId();
    }


    // 레이어 수정
    @Transactional
    public void update(Long id, LayerRequest request, Long memberId) throws AccessDeniedException {
        // 기존 레이어 조회
        Layer layer = layerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("layer not found"));

        // 본인만 수정 가능
        if (!layer.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("본인만 수정 가능합니다.");
        }

        // 연관 Entity 조회
        Member member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new NotFoundException("member not found"));
        Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId())
            .orElseThrow(() -> new NotFoundException("roadmapId not found"));

        // 수정 및 저장
        layer.updateFromRequest(request, member, roadmap);
        layerRepository.save(layer);
    }


    // 레이어 삭제
    @Transactional
    public ResponseEntity<ApiResponse> delete(Long id, Long memberId) {
        // 삭제할 레이어 조회
        Optional<Layer> optionalLayer = layerRepository.findById(id);
        if (optionalLayer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.of(ResponseCode.NOT_FOUND, "레이어가 존재하지 않습니다."));
        }

        Layer layer = optionalLayer.get();

        // 소유자 검증 및 관리자 권한 체크
        if (!layer.getMember().getId().equals(memberId)) {
            // 관리자 권한 확인
            if (isAdmin(memberId)) {
                return blockLayer(layer, memberId, "부적절한 콘텐츠로 인한 관리자 블록 처리");
            }
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.of(ResponseCode.FORBIDDEN, "본인만 삭제 가능합니다."));
        }

        // 참조 무결성 검사
        ReferencedWarning warning = getReferencedWarning(layer.getId());
        if (warning != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.of(ResponseCode.CONFLICT, "해당 레이어는 다른 객체에서 참조중입니다"));
        }

        // 일반 사용자 삭제 처리
        // 1. 찜한 기록 soft delete
        layerLibraryRepository.softDeleteAllByLayer(layer.getId());

        // 2. 레이어 소프트 딜리트
        layer.setDeletedAt(java.time.OffsetDateTime.now());
        layerRepository.save(layer);
        layerRepository.flush();

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어 삭제 완료"));
    }

    // 관리자 블록 처리
    @Transactional
    public ResponseEntity<ApiResponse> blockLayer(Layer layer, Long adminId, String reason) {
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

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어가 블록 처리되었습니다."));
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

        // 해당 레이어를 찜한 기록이 하나라도 있는지 확인
        final List<LayerLibrary> layerLibraries = layerLibraryRepository.findValidZzim(layer.getId());
        if (!layerLibraries.isEmpty()) {
            LayerLibrary layerLibrary = layerLibraries.get(0);
            referencedWarning.setKey("layer.layerLibrary.layer.referenced");
            referencedWarning.addParam(layerLibrary.getId());
            return referencedWarning;
        }

        // 참조 없으면 null 반환 → 삭제 가능
        return null;
    }
}