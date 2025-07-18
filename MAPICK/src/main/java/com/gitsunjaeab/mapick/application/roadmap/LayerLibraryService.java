package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerZzimResponse;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.ZzimException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LayerLibraryService {

    private final LayerLibraryRepository layerLibraryRepository;
    private final LayerRepository layerRepository;
    private final MemberRepository memberRepository;

    public LayerLibraryService(final LayerLibraryRepository layerLibraryRepository,
        LayerRepository layerRepository, MemberRepository memberRepository) {
        this.layerLibraryRepository = layerLibraryRepository;
        this.layerRepository = layerRepository;
        this.memberRepository = memberRepository;
    }



    // ===== 마이페이지 : 레이어 찜 관리  =====

    // 찜 조회
    @Transactional(readOnly = true)
    public LayerZzimListResponse findAllMemberLayers(Member member) {
        List<Long> layerIds = layerLibraryRepository.findLayerIdsByMemberId(member.getId());

        if (layerIds.isEmpty()) {
            return LayerZzimListResponse.of(member, Collections.emptyList());
        }

        List<Layer> layers = layerRepository.findAllByIdWithMember(layerIds);

        return LayerZzimListResponse.of(member, layers);
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