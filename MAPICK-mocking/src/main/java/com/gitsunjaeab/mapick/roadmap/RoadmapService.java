package com.gitsunjaeab.mapick.roadmap;

import com.gitsunjaeab.mapick.bookmark.entity.Bookmark;
import com.gitsunjaeab.mapick.bookmark.BookmarkRepository;
import com.gitsunjaeab.mapick.comment.entity.Comment;
import com.gitsunjaeab.mapick.comment.CommentRepository;
import com.gitsunjaeab.mapick.layer.entity.Layer;
import com.gitsunjaeab.mapick.layer.LayerRepository;
import com.gitsunjaeab.mapick.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap_category_relation.entity.RoadmapCategoryRelation;
import com.gitsunjaeab.mapick.roadmap_category_relation.RoadmapCategoryRelationRepository;
import com.gitsunjaeab.mapick.roadmap_editor.RoadmapEditorRepository;
import com.gitsunjaeab.mapick.roadmap_editor.entity.RoadmapEditor;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.entity.RoadmapHashtagRelation;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.RoadmapHashtagRelationRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.report.entity.Report;
import com.gitsunjaeab.mapick.report.ReportRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;
    private final RoadmapEditorRepository roadmapEditorRepository;
    private final LayerRepository layerRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final RoadmapCategoryRelationRepository roadmapCategoryRelationRepository;
    private final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository;
    private final ReportRepository reportRepository;

    public RoadmapService(final RoadmapRepository roadmapRepository, final MemberRepository memberRepository,
            final RoadmapEditorRepository roadmapEditorRepository, final LayerRepository layerRepository,
            final CommentRepository commentRepository, final BookmarkRepository bookmarkRepository,
            final RoadmapCategoryRelationRepository roadmapCategoryRelationRepository,
            final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository,
            final ReportRepository reportRepository) {
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
        this.roadmapEditorRepository = roadmapEditorRepository;
        this.layerRepository = layerRepository;
        this.commentRepository = commentRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.roadmapCategoryRelationRepository = roadmapCategoryRelationRepository;
        this.roadmapHashtagRelationRepository = roadmapHashtagRelationRepository;
        this.reportRepository = reportRepository;
    }

    public List<RoadmapDTO> findAll() {
        final List<Roadmap> roadmaps = roadmapRepository.findAll(Sort.by("id"));
        return roadmaps.stream()
                .map(map -> roadmapToDTO(map, new RoadmapDTO()))
                .toList();
    }

    public RoadmapDTO get(final Long id) {
        return roadmapRepository.findById(id)
                .map(map -> roadmapToDTO(map, new RoadmapDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoadmapDTO roadmapDTO) {
        final Roadmap roadmap =  new Roadmap();
        roadmapToEntity(roadmapDTO, roadmap);
        return roadmapRepository.save(roadmap).getId();
    }

    public void update(final Long id, final RoadmapDTO roadmapDTO) {
        final Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(roadmapDTO, roadmap);
        roadmapRepository.save(roadmap);
    }

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

    private Roadmap roadmapToEntity(final RoadmapDTO roadmapDTO, final Roadmap roadmap) {
        roadmap.setTitle(roadmapDTO.getTitle());
        roadmap.setDescription(roadmapDTO.getDescription());
        roadmap.setThumbnail(roadmapDTO.getThumbnail());
        roadmap.setIsPublic(roadmapDTO.getIsPublic());
        roadmap.setIsAnimated(roadmapDTO.getIsAnimated());
        roadmap.setLikeCount(roadmapDTO.getLikeCount());
        roadmap.setViewCount(roadmapDTO.getViewCount());
        roadmap.setRoadmapType(roadmapDTO.getRoadmapType());
        roadmap.setCreatedAt(roadmapDTO.getCreatedAt());
        roadmap.setUpdatedAt(roadmapDTO.getUpdatedAt());
        roadmap.setDeletedAt(roadmapDTO.getDeletedAt());
        final Member member = roadmapDTO.getMember() == null ? null : memberRepository.findById(
                roadmapDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        roadmap.setMember(member);
        final Roadmap originalMap = roadmapDTO.getOriginalRoadmap() == null ? null : roadmapRepository.findById(
                roadmapDTO.getOriginalRoadmap())
                .orElseThrow(() -> new NotFoundException("originalMap not found"));
        roadmap.setOriginalRoadmap(originalMap);
        return roadmap;
    }

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
        final RoadmapCategoryRelation mapRoadmapCategoryRelation = roadmapCategoryRelationRepository.findFirstByRoadmap(roadmap);
        if (mapRoadmapCategoryRelation != null) {
            referencedWarning.setKey("roadmap.mapCategoryRelation.roadmap.referenced");
            referencedWarning.addParam(mapRoadmapCategoryRelation.getId());
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
