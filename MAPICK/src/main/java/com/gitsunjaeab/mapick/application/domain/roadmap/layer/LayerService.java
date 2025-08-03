package com.gitsunjaeab.mapick.application.domain.roadmap.layer;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerDetailDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerListDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.request.LayerRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.request.LayerSyncRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.response.LayerResponse;
import com.gitsunjaeab.mapick.application.domain.auth.code.Role;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.application.domain.notification.NotificationService;
import com.gitsunjaeab.mapick.application.domain.notification.code.NotificationType;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.Marker;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.MarkerRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.MarkerService;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmapeditor.RoadmapEditorService;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.infra.common.EntityFinder;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.error.ReferencedWarning;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.error.exceptions.DuplicatedLayerSeqException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    private final LayerForkHistoryRepository layerForkHistoryRepository;
    private final NotificationService notificationService;
    private final EntityFinder entityFinder;
    @PersistenceContext
    private final EntityManager entityManager;

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

            // 레이어 시퀀스 중복 체크
            Map<Integer, Long> seqCount = new HashMap<>();
            for (Layer layer : layers) {
                if (layer.getLayerSeq() != null) {
                    seqCount.put(layer.getLayerSeq(), seqCount.getOrDefault(layer.getLayerSeq(), 0L) + 1);
                }
            }
            
            // 중복된 시퀀스 찾기
            List<Integer> duplicatedSeqs = seqCount.entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();

            // 중복된 시퀀스가 있으면 예외 발생
            if (!duplicatedSeqs.isEmpty()) {
                throw new DuplicatedLayerSeqException(duplicatedSeqs);
            }

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
    @Transactional
    public ForkResult create(final LayerRequest request, Long memberId, Long targetRoadmapId) {

        // 연관 Entity 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        // 레이어 포크 분기 (찜 여부, 중복 포크, 소유자 확인, 알림발송 등)
        if (request.getOriginalLayerId() != null) {
            // 포크 처리 - roadmapId는 사용하지 않음
            Layer originalLayer = layerRepository.findByIdWithMember(request.getOriginalLayerId())
                .orElseThrow(() -> new CommonException(ResponseCode.LAYER_NOT_FOUND));
            Roadmap originRoadmap = originalLayer.getRoadmap();

            // 삭제된 레이어 체크
            if (originalLayer.getDeletedAt() != null) {
                throw new CommonException(ResponseCode.FORBIDDEN);
            }

            // 찜 여부 확인 - 찜을 해야만 포크 가능
            Optional<LayerLibrary> layerLibraryOpt = layerLibraryRepository.findValidZzimByMemberAndLayer(member, originalLayer);
            boolean existsZzim = layerLibraryOpt.isPresent();
            if (!existsZzim) {
                throw new CommonException(ResponseCode.NOT_FOUND);
            }

            // 중복 포크 방지
            List<LayerForkHistory> existingForks = layerForkHistoryRepository
                .findByOriginalLayerAndMember(originalLayer, member);
            if (!existingForks.isEmpty()) {
                throw new CommonException(ResponseCode.ALREADY_PROCESSED);
            }

            // 타겟 로드맵 검증
            Roadmap targetRoadmap = roadmapRepository.findById(targetRoadmapId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

            // 타겟 로드맵 소유자 확인
            if (!targetRoadmap.getMember().getId().equals(memberId)) {
                throw new CommonException(ResponseCode.FORBIDDEN);
            }

            // 레이어 복사 (포크)
            Layer forkedLayer = new Layer();
            forkedLayer.setName(originalLayer.getName());
            forkedLayer.setDescription(originalLayer.getDescription());
            forkedLayer.setLayerSeq(originalLayer.getLayerSeq());
            forkedLayer.setMember(member);
            forkedLayer.setRoadmap(targetRoadmap);

            Layer savedForkedLayer = layerRepository.save(forkedLayer);

            // 포크 이력 저장
            LayerForkHistory forkHistory = new LayerForkHistory();
            forkHistory.setOriginalLayer(originalLayer);
            forkHistory.setForkedLayer(savedForkedLayer);
            forkHistory.setMember(member);
            layerForkHistoryRepository.save(forkHistory);

            // 찜 기록 조회 (포크 알림 발송용) - 이미 위에서 조회했으므로 재사용
            LayerLibrary layerLibrary = layerLibraryOpt.get();

            // 포크 알림 발송 로직 (찜 알림과 동일한 패턴)
            Member layerOwner = originalLayer.getMember();
            notificationService.createNotification(
                layerOwner,            // 알림 받을 대상 (레이어 소유자)
                NotificationType.FORK, // 알림 타입
                targetRoadmap,         // 로드맵 정보
                originalLayer,         // 레이어 정보
                layerLibrary,          // 찜 기록 정보
                null,                  // 퀘스트
                null,                  // 멤버퀘스트
                null,                  // 댓글
                null                   // 북마크
            );
            // 포크된 레이어 반환
            return new ForkResult(originalLayer, originRoadmap, savedForkedLayer, targetRoadmap);
        }
        
        // 일반 생성 - roadmapId 사용
        Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId())
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        Layer layer = Layer.fromRequest(request, member, roadmap);
        Layer createdLayer = layerRepository.save(layer);
        return new ForkResult(null, null, createdLayer, roadmap);

    }

    // 레이어 수정
    @Transactional
    public Layer update(Long layerId, LayerRequest request, Long memberId) {

        // 기존 레이어 조회 (Member와 함께 조회하여 LazyInitializationException 방지)
        Layer layer = layerRepository.findByIdWithMember(layerId)
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

        markerRepository.flush();
        entityManager.clear();

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
        final Layer layer = entityFinder.findLayerById(id);

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