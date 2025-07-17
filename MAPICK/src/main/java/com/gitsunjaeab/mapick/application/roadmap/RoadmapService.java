package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Bookmark;
import com.gitsunjaeab.mapick.domain.roadmap.BookmarkRepository;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.comment.CommentRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository.RoadmapCitationProjection;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapEditor;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapEditorRepository;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapHashtagRelation;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapHashtagRelationRepository;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapType;
import com.gitsunjaeab.mapick.infra.error.exceptions.RoadMapDeleteException;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import jakarta.persistence.EntityNotFoundException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;
    private final RoadmapEditorRepository roadmapEditorRepository;
    private final LayerRepository layerRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository;
    private final ReportRepository reportRepository;
    @Autowired
    private LayerLibraryRepository layerLibraryRepository;
    private final CategoryRepository categoryRepository;

    // 개인 로드맵 생성
    @Transactional
    public void create(@Valid RoadmapRequest request, Long memberId) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Roadmap roadmap = new Roadmap();
        roadmap.setCategory(category);
        roadmap.setMember(member);
        roadmap.setTitle(request.getTitle());
        roadmap.setDescription(request.getDescription());
        roadmap.setThumbnail(request.getThumbnail());
        roadmap.setIsPublic(request.getIsPublic());
        roadmap.setIsAnimated(false); // 기본값
        roadmap.setLikeCount(0);
        roadmap.setViewCount(0);
        roadmap.setRoadmapType(RoadmapType.PERSONAL); // 개인 로드맵
        roadmap.setCreatedAt(OffsetDateTime.now());

        roadmapRepository.save(roadmap);
    }

    // 개인 로드맵 수정
    @Transactional
    public void update(@Valid RoadmapRequest request, Long roadmapId, Long memberId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 로드맵입니다."));

        // 본인 소유인지 검증
        if (!roadmap.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("이 로드맵을 수정할 권한이 없습니다.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        roadmap.setTitle(request.getTitle());
        roadmap.setDescription(request.getDescription());
        roadmap.setThumbnail(request.getThumbnail());
        roadmap.setIsPublic(request.getIsPublic());
        roadmap.setCategory(category);
    }

    // 로드맵 삭제
    @Transactional
    public void delete(Long roadmapId, Member member) {
        Roadmap roadmap = (Roadmap) roadmapRepository.findByIdAndDeletedAtIsNull(roadmapId)
                .orElseThrow(() -> new RoadMapDeleteException(ResponseCode.NOT_FOUND));

        boolean isOwner = roadmap.getMember().getId().equals(member.getId());
        boolean isAdmin = member.getRole().equals("ROLE_ADMIN");

        boolean isReported = reportRepository.existsByRoadmapId(roadmapId);

        if (!(isOwner || (isAdmin && isReported))) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        roadmap.setDeletedAt(OffsetDateTime.now());
    }


    @Transactional(readOnly = true)
    public RoadmapListResponse getAllRoadmaps() {
        List<Roadmap> roadmaps = roadmapRepository.findAll();
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getAllPersonalRoadmapsWithCitation() {
        List<Roadmap> roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapType(RoadmapType.PERSONAL);
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getPersonalRoadmapsByCategory(Long categoryId) {
        List<Roadmap> roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndCategoryId(RoadmapType.PERSONAL, categoryId);
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getAllSharedRoadmapsWithCitation() {
        List<Roadmap> roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapType(RoadmapType.SHARED);
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getSharedRoadmapsByCategory(Long categoryId) {
        List<Roadmap> roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndCategoryId(RoadmapType.SHARED, categoryId);
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse findAllRoadmapsByMember(Long memberId) {
        List<Roadmap> roadmaps = roadmapRepository.findAllByMember_Id(memberId);
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse buildRoadmapListResponse(List<Roadmap> roadmaps) {
        List<Long> roadmapIds = roadmaps.stream()
            .map(Roadmap::getId)
            .collect(Collectors.toList());

        List<RoadmapCitationProjection> projections =
            layerLibraryRepository.countDistinctMemberByRoadmapIds(roadmapIds);

        // 로드맵 ID (key), 인용수 (value)
        Map<Long, Long> citationCountMap = projections.stream()
            .collect(Collectors.toMap(
                RoadmapCitationProjection::getRoadmapId,
                RoadmapCitationProjection::getCitationCount
            ));

        return RoadmapListResponse.of(roadmaps, citationCountMap);
    }

    @Transactional
    public RoadmapResponse get(final Long id) {
        Roadmap roadmap = roadmapRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 로드맵이 존재하지 않습니다. id=" + id));

        return RoadmapResponse.of(roadmap);
    }

//    @Transactional
//    public Long create(final RoadmapRequest request) {
//        final Roadmap roadmap =  new Roadmap();
//        roadmapToEntity(request, roadmap);
//        return roadmapRepository.save(roadmap).getId();
//    }

//    public void update(final Long id, final RoadmapRequest request) {
//        final Roadmap roadmap = roadmapRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        roadmapToEntity(request, roadmap);
//        roadmapRepository.save(roadmap);
//    }



    private RoadmapDTO roadmapToDTO(final Roadmap roadmap, final RoadmapDTO roadmapDTO) {
        roadmapDTO.setId(roadmap.getId());
        roadmapDTO.setTitle(roadmap.getTitle());
        roadmapDTO.setDescription(roadmap.getDescription());
        roadmapDTO.setThumbnail(roadmap.getThumbnail());
        roadmapDTO.setIsPublic(roadmap.getIsPublic());
        roadmapDTO.setIsAnimated(roadmap.getIsAnimated());
        roadmapDTO.setLikeCount(roadmap.getLikeCount());
        roadmapDTO.setViewCount(roadmap.getViewCount());
        roadmapDTO.setRoadmapType(roadmap.getRoadmapType());
        roadmapDTO.setCreatedAt(roadmap.getCreatedAt());
        roadmapDTO.setUpdatedAt(roadmap.getUpdatedAt());
        roadmapDTO.setDeletedAt(roadmap.getDeletedAt());
        roadmapDTO.setMember(roadmap.getMember() == null ? null : new MemberSimpleDTO(roadmap.getMember()));
        return roadmapDTO;
    }

//    private Roadmap roadmapToEntity(final RoadmapRequest request, final Roadmap roadmap) {
//        roadmap.setTitle(request.getTitle());
//        roadmap.setDescription(request.getDescription());
//        roadmap.setThumbnail(request.getThumbnail());
//        roadmap.setIsPublic(request.getIsPublic());
//
//        Set<RoadmapHashtagRelation> relations = request.getHashtags().stream()
//            .map(hashtagDTO -> {
//                RoadmapHashtagRelation relation = new RoadmapHashtagRelation();
//                relation.setHashtag(new Hashtag(hashtagDTO.getId()));
//                relation.setRoadmap(roadmap);
//                return relation;
//            })
//            .collect(Collectors.toSet());
//        roadmap.setRoadmapMapHashtags(relations);
//
//        roadmap.setIsAnimated(request.getIsAnimated());
//        roadmap.setLikeCount(request.getLikeCount());
//        roadmap.setViewCount(request.getViewCount());
//        roadmap.setRoadmapType(RoadmapType.valueOf(request.getRoadmapType()));
//        roadmap.setCreatedAt(request.getCreatedAt());
//        roadmap.setUpdatedAt(request.getUpdatedAt());
//        roadmap.setDeletedAt(request.getDeletedAt());
//
//        final Member member = request.getMember() == null ? null : memberRepository.findById(request.getMember())
//            .orElseThrow(() -> new NotFoundException("member not found"));
//        roadmap.setMember(member);
//
//        final Roadmap originalMap = request.getOriginalRoadmap() == null ? null : roadmapRepository.findById(request.getOriginalRoadmap())
//            .orElseThrow(() -> new NotFoundException("originalMap not found"));
//        roadmap.setOriginalRoadmap(originalMap);
//
//        return roadmap;
//    }


    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(NotFoundException::new);
//        final Roadmap originalMapMap = roadmapRepository.findFirstByOriginalRoadmapAndIdNot(roadmap, roadmap.getId());
//        if (originalMapMap != null) {
//            referencedWarning.setKey("roadmap.roadmap.originalMap.referenced");
//            referencedWarning.addParam(originalMapMap.getId());
//            return referencedWarning;
//        }
        final RoadmapEditor mapMapEditor = roadmapEditorRepository.findFirstByRoadmap(roadmap);
        if (mapMapEditor != null) {
            referencedWarning.setKey("map.mapEditor.map.referenced");
            referencedWarning.addParam(mapMapEditor.getId());
            return referencedWarning;
        }
        final Layer mapLayer = layerRepository.findFirstByRoadmap(roadmap);
        if (mapLayer != null) {
            referencedWarning.setKey("roadmap.layer.roadmap.referenced");
            referencedWarning.addParam(mapLayer.getId());
            return referencedWarning;
        }
        final Comment mapComment = commentRepository.findFirstByRoadmap(roadmap);
        if (mapComment != null) {
            referencedWarning.setKey("roadmap.comment.roadmap.referenced");
            referencedWarning.addParam(mapComment.getId());
            return referencedWarning;
        }
        final Bookmark mapBookmark = bookmarkRepository.findFirstByRoadmap(roadmap);
        if (mapBookmark != null) {
            referencedWarning.setKey("roadmap.bookmark.roadmap.referenced");
            referencedWarning.addParam(mapBookmark.getId());
            return referencedWarning;
        }
        final RoadmapHashtagRelation mapRoadmapHashtagRelation = roadmapHashtagRelationRepository.findFirstByRoadmap(roadmap);
        if (mapRoadmapHashtagRelation != null) {
            referencedWarning.setKey("roadmap.mapHashtagRelation.roadmap.referenced");
            referencedWarning.addParam(mapRoadmapHashtagRelation.getId());
            return referencedWarning;
        }
        final Report mapReport = reportRepository.findFirstByRoadmap(roadmap);
        if (mapReport != null) {
            referencedWarning.setKey("roadmap.report.roadmap.referenced");
            referencedWarning.addParam(mapReport.getId());
            return referencedWarning;
        }
        return null;
    }



}
