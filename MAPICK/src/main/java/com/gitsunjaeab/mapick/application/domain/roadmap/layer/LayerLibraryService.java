package com.gitsunjaeab.mapick.application.domain.roadmap.layer;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.layer.internal.LayerZzimSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.marker.internal.MarkerSimpleDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.internal.RoadmapSimpleDTO;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.application.domain.notification.NotificationService;
import com.gitsunjaeab.mapick.application.domain.notification.code.NotificationType;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class LayerLibraryService {

    private final LayerLibraryRepository layerLibraryRepository;
    private final LayerRepository layerRepository;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final LayerForkHistoryRepository layerForkHistoryRepository;
    private final NotificationService notificationService;

    // ===== 마이페이지 : 레이어 찜 관리  =====

    // 찜 조회
    @Transactional(readOnly = true)
    public LayerZzimSimpleDTO findAllMemberLayers(Member member) {

        List<Long> layerIds = layerLibraryRepository.findLayerIdsByMemberId(member.getId());

        if (layerIds.isEmpty()) {
            return new LayerZzimSimpleDTO(member, List.of(), Map.of(), List.of(), List.of());
        }

        final List<Layer> layers = layerRepository.findByIdWithAllAssociations(layerIds);

        if (layers.isEmpty()) {
            throw new CommonException(ResponseCode.LAYER_NOT_FOUND);
        }

        // DTO 변환
        List<LayerSimpleDTO> layerDtos = layers.stream()
            .map(LayerSimpleDTO::from)
            .toList();

        // 각 레이어별 포크 이력을 Map으로 조회
        // layerId 목록 기반으로 forkHistory + roadmap + category 한 번에 패치
        List<LayerForkHistory> forkHistories = layerForkHistoryRepository.findWithRoadmapAndCategory(
            layerIds);

        // 레이어 ID 기준으로 그룹핑
        Map<Long, List<LayerForkHistory>> forkHistoriesMap = forkHistories.stream()
            .collect(Collectors.groupingBy(fh -> fh.getOriginalLayer().getId()));

        List<MarkerSimpleDTO> markers = layers.stream()
            .flatMap(layer -> layer.getLayerMarkers().stream())
            .map(MarkerSimpleDTO::from)
            .collect(Collectors.toList());

        List<RoadmapSimpleDTO> roadmaps = layers.stream()
            .map(Layer::getRoadmap)
            .filter(Objects::nonNull)
            .map(RoadmapSimpleDTO::from)
            .distinct() // 같은 로드맵 중복 제거 (필요 시)
            .collect(Collectors.toList());

        return new LayerZzimSimpleDTO(member, layerDtos, forkHistoriesMap, markers, roadmaps);
    }


    // 찜 등록
    @Transactional
    public LayerLibrary addLibrary(Long memberId, Long layerId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));
        Layer layer = layerRepository.findByIdWithMember(layerId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 삭제된 레이어 체크
        if (layer.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }

        // 중복 찜 방지
        if (layerLibraryRepository.existsByMemberAndLayer(member, layer)) {
            throw new CommonException(ResponseCode.ALREADY_PROCESSED);
        }

        // 찜 등록
        LayerLibrary layerLibrary = new LayerLibrary();
        layerLibrary.setMember(member);
        layerLibrary.setLayer(layer);
        layerLibrary.setZzim(true);
        layerLibraryRepository.save(layerLibrary);

        // 찜 기록 조회 (모든 연관 엔티티와 함께)
        LayerLibrary savedLibrary = layerLibraryRepository.findByIdWithAllAssociations(
                layerLibrary.getId())
            .orElseThrow(() -> new CommonException(ResponseCode.SAVE_FAILED));

        // 찜 알림 발송 로직 
        Member layerOwner = layer.getMember();
        notificationService.createNotification(
            layerOwner,            // 알림 받을 대상 (레이어 소유자)
            NotificationType.ZZIM, // 알림 타입
            layer.getRoadmap(),    // 로드맵 정보
            layer,                 // 레이어 정보
            savedLibrary,          // 찜 기록 정보
            null,                  // 퀘스트
            null,                  // 멤버퀘스트
            null,                  // 댓글
            null                   // 북마크
        );

        return savedLibrary;
    }


    // 찜 삭제
    @Transactional
    public LayerLibrary removeLibrary(Long memberId, Long layerId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));
        Layer layer = layerRepository.findByIdWithMember(layerId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        LayerLibrary library = layerLibraryRepository.findByMemberAndLayer(member, layer)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 찜 해제 처리 (소프트 딜리트 + isZzim 변경)
        library.setZzim(false);
        library.setDeletedAt(OffsetDateTime.now());
        layerLibraryRepository.save(library);

        return library;
    }

    // 찜한 레이어를 내 로드맵에 포크
    @Transactional
    public ForkResult forkLayer(Long memberId, Long layerId, Long targetRoadmapId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        Layer originalLayer = layerRepository.findByIdWithMember(layerId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        // 삭제된 레이어 체크
        if (originalLayer.getDeletedAt() != null) {
            throw new CommonException(ResponseCode.FORBIDDEN);
        }

        // 찜 여부 확인
        if (!layerLibraryRepository.existsByMemberAndLayer(member, originalLayer)) {
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

        // 찜 기록 조회 (포크 알림 발송용)
        LayerLibrary layerLibrary = layerLibraryRepository.findByMemberAndLayer(member,
                originalLayer)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

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

        // 찜 기록 조회해서 반환
//        return layerLibraryRepository.findByMemberAndLayer(member, originalLayer)
//            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        return new ForkResult(originalLayer, originalLayer.getRoadmap(), savedForkedLayer,
            targetRoadmap);
    }
}