package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerDetailDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerRequest;
import com.gitsunjaeab.mapick.api.roadmap.websocket.dto.LayerSocketDTO;
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
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LayerService {

    private final LayerRepository layerRepository;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final MarkerRepository markerRepository;
    private final LayerLibraryRepository layerLibraryRepository;

    public LayerService(final LayerRepository layerRepository,
        final MemberRepository memberRepository, final RoadmapRepository roadmapRepository,
        final MarkerRepository markerRepository,
        final LayerLibraryRepository layerLibraryRepository) {
        this.layerRepository = layerRepository;
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
        this.markerRepository = markerRepository;
        this.layerLibraryRepository = layerLibraryRepository;
    }

    // ===== 기본 CRUD =====

    // 로드맵의 레이어 조회
    @Transactional(readOnly = true)
    public List<Layer> findAllLayersOnRoadmap(Long roadmapId) {
        if (roadmapId == null) {
            // 전체 레이어 조회 (삭제되지 않은 것만 조회) - 모든 연관 엔티티 함께 조회
            return layerRepository.findAllNotDeletedWithAssociations();
        } else {
            // 특정 로드맵 레이어 조회 - 모든 연관 엔티티 함께 조회
            return layerRepository.findAllByRoadmap_IdWithAssociations(roadmapId);
        }
    }

    // 레이어 상세 조회 (삭제되지 않은 것만 조회) 
    @Transactional(readOnly = true)
    public LayerDetailDTO getLayerDetail(final Long layerId, final Long memberId) {
        Layer layer = layerRepository.findByIdWithMember(layerId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 삭제된 레이어 체크
        if (layer.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }

        // 찜 여부 확인
        boolean isZzim = false;
        if (memberId != null) {
            Member member = memberRepository.findById(memberId).orElse(null);
            if (member != null) {
                isZzim = layerLibraryRepository.existsByMemberAndLayer(member, layer);
            }
        }

        return LayerDetailDTO.from(layer, isZzim);
    }


    // 레이어 생성
    public Layer create(final LayerRequest request, Long memberId, Long roadmapId) {
        // 연관 Entity 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 요청 DTO -> Entity 변환
        Layer layer = request.toEntity(member, roadmap);

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
        layer.setLayerTime(request.getLayerTime());

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

        // 소유자 검증 및 관리자 권한 체크
        if (!layer.getMember().getId().equals(memberId)) {
            // 관리자 권한 확인
            if (isAdmin(memberId)) {
                blockLayer(layer, memberId, "부적절한 콘텐츠로 인한 관리자 블록 처리");
                return layer; // 블록 처리된 엔티티 반환
            }
            throw new CommonException(ResponseCode.FORBIDDEN);
        }



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

    @Transactional
    public Layer createFromSocket(LayerSocketDTO dto) {
        Long memberId = dto.getMemberId();
        Long roadmapId = dto.getRoadmapId();

        // DTO → LayerRequest 변환 후 기존 create() 재사용
        LayerRequest request = new LayerRequest(
                dto.getName(),
                dto.getDescription(),
                dto.getLayerSeq(),
                dto.getLayerTime()
        );

        return create(request, memberId, roadmapId);
    }

    // WebSocket용 레이어 수정
    @Transactional
    public Layer updateFromSocket(LayerSocketDTO dto) {
        Layer layer = layerRepository.findById(dto.getLayerId())
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 권한 검증 없음 → 공유지도이므로 누구나 수정 가능
        layer.setName(dto.getName());
        layer.setDescription(dto.getDescription());
        layer.setLayerSeq(dto.getLayerSeq());
        layer.setLayerTime(dto.getLayerTime());

        return layerRepository.save(layer);
    }

    // WebSocket용 레이어 삭제
    @Transactional
    public Layer deleteFromSocket(LayerSocketDTO dto) {
        Layer layer = layerRepository.findById(dto.getLayerId())
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 공유지도에서는 권한 없이도 삭제 가능
        if (layer.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.ALREADY_PROCESSED);
        }

        ReferencedWarning warning = getReferencedWarning(layer.getId());
        if (warning != null) {
            throw new CommonException(ResponseCode.CONFLICT);
        }

        layerLibraryRepository.softDeleteAllByLayer(layer.getId());
        layer.setDeletedAt(OffsetDateTime.now());
        layerRepository.save(layer);
        layerRepository.flush();

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

        // 해당 레이어를 찜한 기록이 하나라도 있는지 확인
        final List<LayerLibrary> layerLibraries = layerLibraryRepository.findValidZzim(
            layer.getId());
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