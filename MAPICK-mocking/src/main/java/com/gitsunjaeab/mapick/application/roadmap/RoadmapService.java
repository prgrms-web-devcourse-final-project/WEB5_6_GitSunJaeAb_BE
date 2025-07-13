package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.RoadmapListResponse;
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
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
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

    public RoadmapService(final RoadmapRepository roadmapRepository, final MemberRepository memberRepository,
            final RoadmapEditorRepository roadmapEditorRepository, final LayerRepository layerRepository,
            final CommentRepository commentRepository, final BookmarkRepository bookmarkRepository,
            final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository,
            final ReportRepository reportRepository) {
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
        this.roadmapEditorRepository = roadmapEditorRepository;
        this.layerRepository = layerRepository;
        this.commentRepository = commentRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.roadmapHashtagRelationRepository = roadmapHashtagRelationRepository;
        this.reportRepository = reportRepository;
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

//    @Transactional
//    public Long create(final RoadmapRequest request) {
//        final Roadmap roadmap =  new Roadmap();
//        roadmapToEntity(request, roadmap);
//        return roadmapRepository.save(roadmap).getId();
//    }

    public RoadmapDTO get(final Long id) {
        return roadmapRepository.findById(id)
                .map(map -> roadmapToDTO(map, new RoadmapDTO()))
                .orElseThrow(NotFoundException::new);
    }


//    public void update(final Long id, final RoadmapRequest request) {
//        final Roadmap roadmap = roadmapRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        roadmapToEntity(request, roadmap);
//        roadmapRepository.save(roadmap);
//    }

    public void delete(final Long id) {
        roadmapRepository.deleteById(id);
    }

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
        roadmapDTO.setMember(roadmap.getMember() == null ? null : roadmap.getMember().getId());
        roadmapDTO.setOriginalRoadmap(roadmap.getOriginalRoadmap() == null ? null : roadmap.getOriginalRoadmap().getId());
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
        final Roadmap originalMapMap = roadmapRepository.findFirstByOriginalRoadmapAndIdNot(roadmap, roadmap.getId());
        if (originalMapMap != null) {
            referencedWarning.setKey("roadmap.roadmap.originalMap.referenced");
            referencedWarning.addParam(originalMapMap.getId());
            return referencedWarning;
        }
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
