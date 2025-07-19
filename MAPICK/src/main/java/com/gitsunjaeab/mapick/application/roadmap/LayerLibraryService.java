package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimResponse;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerForkHistory;
import com.gitsunjaeab.mapick.domain.roadmap.LayerForkHistoryRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.ZzimException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerForkHistoryDTO;


@Service
@Slf4j
public class LayerLibraryService {

    private final LayerLibraryRepository layerLibraryRepository;
    private final LayerRepository layerRepository;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final LayerForkHistoryRepository layerForkHistoryRepository;

    public LayerLibraryService(final LayerLibraryRepository layerLibraryRepository,
        LayerRepository layerRepository, MemberRepository memberRepository,
        RoadmapRepository roadmapRepository, LayerForkHistoryRepository layerForkHistoryRepository) {
        this.layerLibraryRepository = layerLibraryRepository;
        this.layerRepository = layerRepository;
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
        this.layerForkHistoryRepository = layerForkHistoryRepository;
    }



    // ===== 마이페이지 : 레이어 찜 관리  =====

    // 찜 조회
    @Transactional(readOnly = true)
    public LayerZzimListResponse findAllMemberLayers(Member member) {
        List<Long> layerIds = layerLibraryRepository.findLayerIdsByMemberId(member.getId());

        if (layerIds.isEmpty()) {
            return LayerZzimListResponse.of(member, List.of());
        }

        List<Layer> layers = layerRepository.findAllByIdWithMember(layerIds);
        
        // 각 레이어의 포크 이력을 포함한 LayerZzimDTO 생성
        List<LayerZzimDTO> layerZzimDTOs = layers.stream()
            .map(layer -> {
                LayerZzimDTO dto = new LayerZzimDTO(layer);
                
                // 해당 레이어의 포크 이력 조회
                List<LayerForkHistory> forkHistories = layerForkHistoryRepository
                    .findByOriginalLayerAndMember(layer, member);
                
                List<LayerForkHistoryDTO> forkHistoryDTOs = forkHistories.stream()
                    .map(LayerForkHistoryDTO::from)
                    .toList();
                
                dto.setForkHistories(forkHistoryDTOs);
                return dto;
            })
            .toList();

        return LayerZzimListResponse.of(member, layerZzimDTOs);
    }


    // 찜 등록
    @Transactional
    public LayerZzimResponse addLibrary(Long memberId, Long layerId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Layer layer = layerRepository.findById(layerId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레이어입니다."));

        // 삭제된 레이어는 찜 불가
        if (layer.getDeletedAt() != null) {
            throw new IllegalArgumentException("삭제된 레이어는 찜할 수 없습니다.");
        }

        // 중복 방지
        if (layerLibraryRepository.existsByMemberAndLayer(member, layer)) {
            throw ZzimException.alreadyZzimed();
        }

        LayerLibrary layerLibrary = new LayerLibrary();
        layerLibrary.setMember(member);
        layerLibrary.setLayer(layer);
        layerLibrary.setZzim(true);
        layerLibraryRepository.save(layerLibrary);

        // EntityGraph로 다시 조회 (member까지 로딩됨)
        LayerLibrary loaded = layerLibraryRepository.findWithMemberById(layerLibrary.getId())
            .orElseThrow(() -> new IllegalStateException("라이브러리 저장 실패"));

        // 여기서 DTO로 변환해서 리턴 (트랜잭션 안 끝난 시점에)
        return LayerZzimResponse.of(loaded, "레이어 찜 추가 완료");
    }


    // 찜 삭제
    @Transactional
    public LayerZzimResponse removeLibrary(Long memberId, Long layerId) {
        // 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Layer layer = layerRepository.findById(layerId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레이어입니다."));

        LayerLibrary library = layerLibraryRepository.findByMemberAndLayer(member, layer)
            .orElseThrow(() -> ZzimException.notZzimedYet());

        // 소프트 딜리트로 변경
        library.setDeletedAt(OffsetDateTime.now());
        layerLibraryRepository.save(library);

        return LayerZzimResponse.of(library, "레이어 찜 해제 완료");
    }

    // 찜한 레이어를 내 로드맵에 포크
    @Transactional
    public LayerZzimResponse forkLayer(Long memberId, Long layerId, Long targetRoadmapId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        
        // 원본 레이어 조회
        Layer originalLayer = layerRepository.findById(layerId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레이어입니다."));

        // 삭제된 레이어는 포크 불가
        if (originalLayer.getDeletedAt() != null) {
            throw new IllegalArgumentException("삭제된 레이어는 포크할 수 없습니다.");
        }

        // 찜 여부 확인
        boolean isZzimed = layerLibraryRepository.existsByMemberAndLayer(member, originalLayer);
        if (!isZzimed) {
            throw new IllegalArgumentException("찜하지 않은 레이어는 포크할 수 없습니다.");
        }

        // 타겟 로드맵 조회
        Roadmap targetRoadmap = roadmapRepository.findById(targetRoadmapId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 로드맵입니다."));

        // 타겟 로드맵 소유자 확인
        if (!targetRoadmap.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인의 로드맵에만 포크할 수 있습니다.");
        }

        // 레이어 복사 (포크)
        Layer forkedLayer = new Layer();
        forkedLayer.setName(originalLayer.getName());
        forkedLayer.setDescription(originalLayer.getDescription());
        forkedLayer.setLayerSeq(originalLayer.getLayerSeq());
        forkedLayer.setLayerTime(originalLayer.getLayerTime());
        forkedLayer.setMember(member);
        forkedLayer.setRoadmap(targetRoadmap);
        
        Layer savedForkedLayer = layerRepository.save(forkedLayer);

        // 포크 이력 저장
        LayerForkHistory forkHistory = new LayerForkHistory();
        forkHistory.setOriginalLayer(originalLayer);
        forkHistory.setForkedLayer(savedForkedLayer);
        forkHistory.setMember(member);
        layerForkHistoryRepository.save(forkHistory);

        // 찜 기록 조회해서 응답 생성
        LayerLibrary library = layerLibraryRepository.findByMemberAndLayer(member, originalLayer)
            .orElseThrow(() -> new IllegalStateException("찜 기록을 찾을 수 없습니다."));

        return LayerZzimResponse.of(library, "레이어 포크 완료");
    }


    // ===== 안 쓰이는데 만든 기능.... =====

    // 전체 회원 라이브러리 레이어 조회
//    @Transactional(readOnly = true)
//    public LayerListResponse findAllLibraryLayers() {
//        List<Long> layerIds = layerLibraryRepository.findAllLayerIdsInLibrary();
//
//        if (layerIds.isEmpty()) {
//            return LayerListResponse.of(Collections.emptyList());
//        }
//
//        List<Layer> layers = layerRepository.findAllByIdWithMember(layerIds);
//
//        return LayerListResponse.of(layers);
//    }

}