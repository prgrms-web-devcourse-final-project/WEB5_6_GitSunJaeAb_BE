package com.gitsunjaeab.mapick.map;

import com.gitsunjaeab.mapick.bookmark.Bookmark;
import com.gitsunjaeab.mapick.bookmark.BookmarkRepository;
import com.gitsunjaeab.mapick.comment.Comment;
import com.gitsunjaeab.mapick.comment.CommentRepository;
import com.gitsunjaeab.mapick.layer.Layer;
import com.gitsunjaeab.mapick.layer.LayerRepository;
import com.gitsunjaeab.mapick.map_category_relation.MapCategoryRelation;
import com.gitsunjaeab.mapick.map_category_relation.MapCategoryRelationRepository;
import com.gitsunjaeab.mapick.map_editor.MapEditor;
import com.gitsunjaeab.mapick.map_editor.MapEditorRepository;
import com.gitsunjaeab.mapick.map_hashtag_relation.entity.MapHashtagRelation;
import com.gitsunjaeab.mapick.map_hashtag_relation.MapHashtagRelationRepository;
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
public class MapService {

    private final MapRepository mapRepository;
    private final MemberRepository memberRepository;
    private final MapEditorRepository mapEditorRepository;
    private final LayerRepository layerRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MapCategoryRelationRepository mapCategoryRelationRepository;
    private final MapHashtagRelationRepository mapHashtagRelationRepository;
    private final ReportRepository reportRepository;

    public MapService(final MapRepository mapRepository, final MemberRepository memberRepository,
            final MapEditorRepository mapEditorRepository, final LayerRepository layerRepository,
            final CommentRepository commentRepository, final BookmarkRepository bookmarkRepository,
            final MapCategoryRelationRepository mapCategoryRelationRepository,
            final MapHashtagRelationRepository mapHashtagRelationRepository,
            final ReportRepository reportRepository) {
        this.mapRepository = mapRepository;
        this.memberRepository = memberRepository;
        this.mapEditorRepository = mapEditorRepository;
        this.layerRepository = layerRepository;
        this.commentRepository = commentRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.mapCategoryRelationRepository = mapCategoryRelationRepository;
        this.mapHashtagRelationRepository = mapHashtagRelationRepository;
        this.reportRepository = reportRepository;
    }

    public List<MapDTO> findAll() {
        final List<Map> maps = mapRepository.findAll(Sort.by("id"));
        return maps.stream()
                .map(map -> mapToDTO(map, new MapDTO()))
                .toList();
    }

    public MapDTO get(final Long id) {
        return mapRepository.findById(id)
                .map(map -> mapToDTO(map, new MapDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MapDTO mapDTO) {
        final Map map = new Map();
        mapToEntity(mapDTO, map);
        return mapRepository.save(map).getId();
    }

    public void update(final Long id, final MapDTO mapDTO) {
        final Map map = mapRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(mapDTO, map);
        mapRepository.save(map);
    }

    public void delete(final Long id) {
        mapRepository.deleteById(id);
    }

    private MapDTO mapToDTO(final Map map, final MapDTO mapDTO) {
        mapDTO.setId(map.getId());
        mapDTO.setTitle(map.getTitle());
        mapDTO.setDescription(map.getDescription());
        mapDTO.setThumbnail(map.getThumbnail());
        mapDTO.setIsPublic(map.getIsPublic());
        mapDTO.setIsAnimated(map.getIsAnimated());
        mapDTO.setLikeCount(map.getLikeCount());
        mapDTO.setViewCount(map.getViewCount());
        mapDTO.setMapType(map.getMapType());
        mapDTO.setCreatedAt(map.getCreatedAt());
        mapDTO.setUpdatedAt(map.getUpdatedAt());
        mapDTO.setDeletedAt(map.getDeletedAt());
        mapDTO.setMember(map.getMember() == null ? null : map.getMember().getId());
        mapDTO.setOriginalMap(map.getOriginalMap() == null ? null : map.getOriginalMap().getId());
        return mapDTO;
    }

    private Map mapToEntity(final MapDTO mapDTO, final Map map) {
        map.setTitle(mapDTO.getTitle());
        map.setDescription(mapDTO.getDescription());
        map.setThumbnail(mapDTO.getThumbnail());
        map.setIsPublic(mapDTO.getIsPublic());
        map.setIsAnimated(mapDTO.getIsAnimated());
        map.setLikeCount(mapDTO.getLikeCount());
        map.setViewCount(mapDTO.getViewCount());
        map.setMapType(mapDTO.getMapType());
        map.setCreatedAt(mapDTO.getCreatedAt());
        map.setUpdatedAt(mapDTO.getUpdatedAt());
        map.setDeletedAt(mapDTO.getDeletedAt());
        final Member member = mapDTO.getMember() == null ? null : memberRepository.findById(mapDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        map.setMember(member);
        final Map originalMap = mapDTO.getOriginalMap() == null ? null : mapRepository.findById(mapDTO.getOriginalMap())
                .orElseThrow(() -> new NotFoundException("originalMap not found"));
        map.setOriginalMap(originalMap);
        return map;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Map map = mapRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Map originalMapMap = mapRepository.findFirstByOriginalMapAndIdNot(map, map.getId());
        if (originalMapMap != null) {
            referencedWarning.setKey("map.map.originalMap.referenced");
            referencedWarning.addParam(originalMapMap.getId());
            return referencedWarning;
        }
        final MapEditor mapMapEditor = mapEditorRepository.findFirstByMap(map);
        if (mapMapEditor != null) {
            referencedWarning.setKey("map.mapEditor.map.referenced");
            referencedWarning.addParam(mapMapEditor.getId());
            return referencedWarning;
        }
        final Layer mapLayer = layerRepository.findFirstByMap(map);
        if (mapLayer != null) {
            referencedWarning.setKey("map.layer.map.referenced");
            referencedWarning.addParam(mapLayer.getId());
            return referencedWarning;
        }
        final Comment mapComment = commentRepository.findFirstByMap(map);
        if (mapComment != null) {
            referencedWarning.setKey("map.comment.map.referenced");
            referencedWarning.addParam(mapComment.getId());
            return referencedWarning;
        }
        final Bookmark mapBookmark = bookmarkRepository.findFirstByMap(map);
        if (mapBookmark != null) {
            referencedWarning.setKey("map.bookmark.map.referenced");
            referencedWarning.addParam(mapBookmark.getId());
            return referencedWarning;
        }
        final MapCategoryRelation mapMapCategoryRelation = mapCategoryRelationRepository.findFirstByMap(map);
        if (mapMapCategoryRelation != null) {
            referencedWarning.setKey("map.mapCategoryRelation.map.referenced");
            referencedWarning.addParam(mapMapCategoryRelation.getId());
            return referencedWarning;
        }
        final MapHashtagRelation mapMapHashtagRelation = mapHashtagRelationRepository.findFirstByMap(map);
        if (mapMapHashtagRelation != null) {
            referencedWarning.setKey("map.mapHashtagRelation.map.referenced");
            referencedWarning.addParam(mapMapHashtagRelation.getId());
            return referencedWarning;
        }
        final Report mapReport = reportRepository.findFirstByMap(map);
        if (mapReport != null) {
            referencedWarning.setKey("map.report.map.referenced");
            referencedWarning.addParam(mapReport.getId());
            return referencedWarning;
        }
        return null;
    }

}
