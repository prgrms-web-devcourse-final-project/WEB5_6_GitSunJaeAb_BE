package com.gitsunjaeab.mapick.application.roadmap;

import static com.gitsunjaeab.mapick.domain.roadmap.QLayer.layer;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerResponse;
import com.gitsunjaeab.mapick.application.member.MemberService;
import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
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
import java.time.OffsetDateTime;
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
    public LayerResponse getLayerDetail(final Long id, boolean isZzim) {
        Layer layer = layerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 레이어가 존재하지 않습니다. id=" + id));

        // 삭제된 레이어는 조회 불가
        if (layer.getDeletedAt() != null) {
            throw new EntityNotFoundException("삭제된 레이어입니다. id=" + id);
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

        // 소유자 검증
        if (!layer.getMember().getId().equals(memberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body (ApiResponse.of(ResponseCode.FORBIDDEN, "본인만 삭제 가능합니다."));
        }

        // 참조 무결성 검사 (마커/찜 에서 사용중인지 확인)
        log.info("[DEBUG] getReferencedWarning 시작 - 레이어 ID: {}", layer.getId());
        ReferencedWarning warning = getReferencedWarning(layer.getId());
        log.info("[DEBUG] getReferencedWarning 결과: {}", warning != null ? warning.getKey() : "null");

        if (warning != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.of(ResponseCode.CONFLICT,
                    "해당 레이어는 다른 객체에서 참조중입니다: " + warning.getKey()));
        }

        // 🔍 [디버깅] 찜 여부 확인
        List<LayerLibrary> libs = layerLibraryRepository.findAll();
        libs.forEach(lib -> {
            if (lib.getLayer() != null) {
                log.info("[DEBUG] 찜된 layerId = {}", lib.getLayer().getId());
            } else {
                log.warn("[DEBUG] LayerLibrary 객체에 layer가 null입니다. libId = {}", lib.getId());
            }
        });

        // 삭제 처리 (서버 시간으로 soft delete)
        OffsetDateTime now = OffsetDateTime.now();
        layer.setDeletedAt(now);

        log.info(">>> 레이어 삭제 저장 직전 ID: {}, 삭제 시간: {}", layer.getId(), now);

        // 찜한 기록 soft delete 추가
        int deletedZzim = layerLibraryRepository.softDeleteAllByLayer(layer.getId());
        log.info("찜 기록 soft delete 된 수: {}", deletedZzim);

        // 레이어 소프트 딜리트
        layerRepository.save(layer);
        layerRepository.flush(); // 강제로 DB 반영
        layerLibraryRepository.flush();

        Layer check = layerRepository.findById(layer.getId()).orElseThrow();
        log.info("✅ 저장 후 deletedAt 확인: {}", check.getDeletedAt());

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.OK, "레이어 삭제 완료"));
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
        log.info("[DEBUG] 찜 기록 검사 시작 - 레이어 ID: {}", layer.getId());
        final List<LayerLibrary> layerLibraries = layerLibraryRepository.findValidZzim(layer.getId());
        log.info("[DEBUG] 찜 기록 검사 결과: 찜 기록 {}개 발견", layerLibraries.size());
        
        if (!layerLibraries.isEmpty()) {
            LayerLibrary layerLibrary = layerLibraries.get(0);
            log.info("[DEBUG] 찜 기록 상세: LibraryId={}, LayerId={}, MemberId={}, IsZzim={}", 
                layerLibrary.getId(), layerLibrary.getLayer().getId(), 
                layerLibrary.getMember().getId(), layerLibrary.isZzim());
            
            referencedWarning.setKey("layer.layerLibrary.layer.referenced");
            referencedWarning.addParam(layerLibrary.getId());
            return referencedWarning;
        }

        // 참조 없으면 null 반환 → 삭제 가능
        return null;
    }
}