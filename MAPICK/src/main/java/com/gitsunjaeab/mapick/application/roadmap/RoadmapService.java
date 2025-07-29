package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.*;
import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagRequest;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.domain.achievement.AchievementRepository;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievement;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievementRepository;
import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.comment.CommentRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.domain.roadmap.*;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository.RoadmapCitationProjection;
import com.gitsunjaeab.mapick.infra.auth.AuthHelper;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.error.exceptions.InvalidRoadmapTypeException;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthenticatedException;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


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
    private final MarkerRepository markerRepository;
    @Autowired
    private LayerLibraryRepository layerLibraryRepository;
    private final CategoryRepository categoryRepository;
    private final HashtagService hashtagService;
    private final HashtagRepository hashtagRepository;
    private final AuthHelper authHelper;
    private final SupabaseStorageService supabaseStorageService;
    @Autowired
    private MemberAchievementRepository memberAchievementRepository;
    @Autowired
    private AchievementRepository achievementRepository;

    // 공유 지도 생성
    @Transactional
    public RoadmapAchievementDTO createSharedRoadmap(SharedRoadmapCreateRequest request, Long memberId, MultipartFile imageFile) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("멤버 없음"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("카테고리 없음"));

        if (request.getRegionLatitude() == null || request.getRegionLongitude() == null || request.getParticipationEnd() == null) {
            throw new IllegalArgumentException("공유 로드맵 생성 시 region 좌표 및 참여 종료일은 필수입니다.");
        }

        Roadmap roadmap = new Roadmap();
        roadmap.setCategory(category);
        roadmap.setTitle(request.getTitle());
        roadmap.setDescription(request.getDescription());
        roadmap.setIsPublic(request.getIsPublic());
        roadmap.setIsAnimated(false);
        roadmap.setRoadmapType(RoadmapType.SHARED);
        roadmap.setCreatedAt(OffsetDateTime.now());
        roadmap.setLikeCount(0);
        roadmap.setViewCount(0);
        roadmap.setMember(member);
        roadmap.setRegionLatitude(request.getRegionLatitude());
        roadmap.setRegionLongitude(request.getRegionLongitude());
        roadmap.setParticipationEnd(request.getParticipationEnd());
        roadmap.setAddress(request.getAddress());
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            roadmap.setThumbnail(imageUrl);
        }

        roadmapRepository.save(roadmap);
        // 로드맵 생성 시 해시태그
        List<HashtagRequest> hashtagDto = request.getHashtags();
        if (hashtagDto != null && !hashtagDto.isEmpty()) {
            Set<Long> seen = new HashSet<>();
            List<Hashtag> hashtags = hashtagService.findOrCreateHashtags(hashtagDto);

            for (Hashtag tag : hashtags) {
                if (!seen.add(tag.getId())) continue;
                RoadmapHashtagRelation relation = new RoadmapHashtagRelation();
                relation.setRoadmap(roadmap);
                relation.setHashtag(tag);

                roadmap.getRoadmapMapHashtags().add(relation);
                tag.getRoadmapHashtags().add(relation);
            }

            roadmapHashtagRelationRepository.saveAll(roadmap.getRoadmapMapHashtags());
        }

        // 공유지도 첫 생성 업적
        Long roadmapCount = roadmapRepository.countByMemberIdAndRoadmapType(memberId, RoadmapType.SHARED);
        final Long ACHIEVEMENT_ID = 102L;

        if (roadmapCount == 1) {
            boolean alreadyHas = memberAchievementRepository.existsByMemberIdAndAchievementId(memberId, ACHIEVEMENT_ID);
            if (!alreadyHas) {
                Member user = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 회원이 없습니다."));
                Achievement achievement = achievementRepository.findById(ACHIEVEMENT_ID)
                    .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 업적이 없습니다."));

                memberAchievementRepository.save(
                    MemberAchievement.builder()
                        .member(user)
                        .achievement(achievement)
                        .achievedAt(OffsetDateTime.now())
                        .build()
                );
                return new RoadmapAchievementDTO(roadmap.getId(), true, new AchievementDTO(achievement));
            }
        }

        return new RoadmapAchievementDTO(roadmap.getId(), false, null);
    }

    // 공유 지도 수정
    @Transactional
    public void updateSharedRoadmap(@Valid SharedRoadmapUpdateRequest request, Long roadmapId, Long memberId, MultipartFile imageFile) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 로드맵입니다."));

        // 본인 소유인지 검증
        if (!roadmap.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("이 로드맵을 수정할 권한이 없습니다.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        if (roadmap.getRoadmapType() != RoadmapType.SHARED) {
            throw new InvalidRoadmapTypeException("공유 로드맵이 아닙니다.");
        }

        roadmap.setTitle(request.getTitle());
        roadmap.setDescription(request.getDescription());
        roadmap.setRegionLatitude(request.getRegionLatitude());
        roadmap.setRegionLongitude(request.getRegionLongitude());
        roadmap.setParticipationEnd(request.getParticipationEnd());
        roadmap.setIsPublic(request.getIsPublic());
        roadmap.setCategory(category);
        roadmap.setUpdatedAt(OffsetDateTime.now());
        roadmap.setAddress(request.getAddress());
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            roadmap.setThumbnail(imageUrl);
        }


        // 해시태그 수정: null이 아닐 때만 처리
        if (request.getHashtags() != null) {
            // 기존 연결 삭제
            roadmapHashtagRelationRepository.deleteByRoadmap(roadmap);

            // 새 해시태그 연결 (비어있으면 아무것도 연결하지 않음)
            List<RoadmapHashtagRelation> newRelations = request.getHashtags().stream()
                    .map(dto -> {
                        Hashtag hashtag = hashtagRepository.findByName(dto.getName())
                                .orElseGet(() -> hashtagRepository.save(new Hashtag(dto.getName())));
                        return new RoadmapHashtagRelation(roadmap, hashtag);
                    })
                    .collect(Collectors.toList());

            roadmapHashtagRelationRepository.saveAll(newRelations);
        }
    }

    // 개인 로드맵 생성
    @Transactional
    public RoadmapAchievementDTO createRoadmap(@Valid RoadmapRequest request, Long memberId ,MultipartFile imageFile) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Roadmap roadmap = new Roadmap();
        roadmap.setCategory(category);
        roadmap.setTitle(request.getTitle());
        roadmap.setDescription(request.getDescription());
        roadmap.setIsPublic(request.getIsPublic());
        roadmap.setIsAnimated(false); // 기본값
        roadmap.setRoadmapType(RoadmapType.PERSONAL); // 개인 로드맵
        roadmap.setCreatedAt(OffsetDateTime.now());
        roadmap.setLikeCount(0);
        roadmap.setViewCount(0);
        roadmap.setMember(member);
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            roadmap.setThumbnail(imageUrl);
        }

        roadmapRepository.save(roadmap);
        // 로드맵 생성 시 해시태그
        List<HashtagRequest> hashtagDto = request.getHashtags();
        if (hashtagDto != null && !hashtagDto.isEmpty()) {
            Set<Long> seen = new HashSet<>();
            List<Hashtag> hashtags = hashtagService.findOrCreateHashtags(hashtagDto);

            for (Hashtag tag : hashtags) {
                if (!seen.add(tag.getId())) continue;
                RoadmapHashtagRelation relation = new RoadmapHashtagRelation();
                relation.setRoadmap(roadmap);
                relation.setHashtag(tag);

                roadmap.getRoadmapMapHashtags().add(relation);
                tag.getRoadmapHashtags().add(relation);
            }

            roadmapHashtagRelationRepository.saveAll(roadmap.getRoadmapMapHashtags());
        }

        // 로드맵 첫 생성 업적
        Long roadmapCount = roadmapRepository.countByMemberId(memberId);
        final Long ACHIEVEMENT_ID = 100L;

        if (roadmapCount == 1) {
            boolean alreadyHas = memberAchievementRepository.existsByMemberIdAndAchievementId(memberId, ACHIEVEMENT_ID);
            if (!alreadyHas) {
                Member user = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 회원이 없습니다."));
                Achievement achievement = achievementRepository.findById(ACHIEVEMENT_ID)
                    .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 업적이 없습니다."));

                memberAchievementRepository.save(
                    MemberAchievement.builder()
                        .member(user)
                        .achievement(achievement)
                        .achievedAt(OffsetDateTime.now())
                        .build()
                );
                return new RoadmapAchievementDTO(roadmap.getId(), true, new AchievementDTO(achievement));
            }
        }

        return new RoadmapAchievementDTO(roadmap.getId(), false, null);
    }

    // 개인 로드맵 수정
    @Transactional
    public void updateRoadmap(@Valid RoadmapRequest request, Long roadmapId, Long memberId, MultipartFile imageFile) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 로드맵입니다."));

        // 본인 소유인지 검증
        if (!roadmap.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("이 로드맵을 수정할 권한이 없습니다.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        if (roadmap.getRoadmapType() != RoadmapType.PERSONAL) {
            throw new InvalidRoadmapTypeException("개인 로드맵이 아닙니다.");
        }

        roadmap.setTitle(request.getTitle());
        roadmap.setDescription(request.getDescription());
        roadmap.setIsPublic(request.getIsPublic());
        roadmap.setCategory(category);
        roadmap.setUpdatedAt(OffsetDateTime.now());
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = supabaseStorageService.upload(imageFile);
            roadmap.setThumbnail(imageUrl);
        }

        // 해시태그 수정: null이 아닐 때만 처리
        if (request.getHashtags() != null) {
            // 기존 연결 삭제
            roadmapHashtagRelationRepository.deleteByRoadmap(roadmap);

            // 새 해시태그 연결 (비어있으면 아무것도 연결하지 않음)
            List<RoadmapHashtagRelation> newRelations = request.getHashtags().stream()
                    .map(dto -> {
                        Hashtag hashtag = hashtagRepository.findByName(dto.getName())
                                .orElseGet(() -> hashtagRepository.save(new Hashtag(dto.getName())));
                        return new RoadmapHashtagRelation(roadmap, hashtag);
                    })
                    .collect(Collectors.toList());

            roadmapHashtagRelationRepository.saveAll(newRelations);
        }
    }

    // 로드맵 삭제
    @Transactional
    public void delete(Long roadmapId, Member member) {
        Roadmap roadmap = roadmapRepository.findByIdAndDeletedAtIsNull(roadmapId)
                .orElseThrow(() -> new UnauthenticatedException(ResponseCode.NOT_FOUND));

        boolean isOwner = roadmap.getMember().getId().equals(member.getId());
        boolean isAdmin = member.getRole().equals("ROLE_ADMIN");
        boolean isReported = reportRepository.existsByRoadmapId(roadmapId);

        if (!(isOwner || (isAdmin && isReported))) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        roadmapHashtagRelationRepository.deleteByRoadmap(roadmap);
        List<Layer> layers = layerRepository.findByRoadmapIdAndDeletedAtIsNull(roadmapId);
        for (Layer layer : layers) {
            markerRepository.deleteByLayerId(layer.getId()); // 직접 삭제 쿼리 필요

            layer.setRoadmap(null);
            layer.setDeletedAt(OffsetDateTime.now());
        }
        roadmap.setDeletedAt(OffsetDateTime.now());
    }


    @Transactional(readOnly = true)
    public RoadmapListResponse getAllRoadmaps() {
        List<Roadmap> roadmaps = roadmapRepository.findAll();
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getAllPersonalRoadmapsWithCitation(Member member) {
        List<Roadmap> roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndDeletedAtIsNull(RoadmapType.PERSONAL);
        Set<Long> bookmarkedIds = bookmarkRepository.findRoadmapIdsByMemberId(member.getId()).stream().collect(Collectors.toSet());
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getPersonalRoadmapsByCategory(Long categoryId, Member member) {
        List<Roadmap> roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndCategoryId(RoadmapType.PERSONAL, categoryId);
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getAllSharedRoadmapsWithCitation() {
        List<Roadmap> roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndDeletedAtIsNull(RoadmapType.SHARED);
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getSharedRoadmapsByCategory(Long categoryId) {
        List<Roadmap> roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndCategoryId(RoadmapType.SHARED, categoryId);
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse findAllRoadmapsByMember(Long memberId) {
        List<Roadmap> roadmaps = roadmapRepository.findAllByMember_IdAndDeletedAtIsNull(memberId);
        return buildRoadmapListResponse(roadmaps);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getMyParticipatedSharedRoadmapsWithCitation(Long memberId) {
        // 사용자가 참여한 공유지도 조회
        List<Roadmap> roadmaps = roadmapEditorRepository.findParticipatedSharedRoadmaps(memberId);
        System.out.println("참여한 로드맵 개수: " + roadmaps.size());

        if (roadmaps.isEmpty()) {
            return RoadmapListResponse.of(Collections.emptyList(), Collections.emptyMap(), Collections.emptyMap());
        }

        List<Long> roadmapIds = roadmaps.stream()
                .map(Roadmap::getId)
                .collect(Collectors.toList());

        // 인용 수 조회
        List<RoadmapCitationProjection> projections =
                layerLibraryRepository.countDistinctMemberByRoadmapIds(roadmapIds);
        Map<Long, Long> citationCountMap = projections.stream()
                .collect(Collectors.toMap(
                        RoadmapCitationProjection::getRoadmapId,
                        RoadmapCitationProjection::getCitationCount
                ));

        // 북마크 정보 조회
        List<Bookmark> bookmarks = bookmarkRepository.findByMemberId(memberId);
        Map<Long, Long> roadmapIdToBookmarkIdMap = bookmarks.stream()
                .filter(b -> roadmapIds.contains(b.getRoadmap().getId()))
                .collect(Collectors.toMap(
                        b -> b.getRoadmap().getId(),
                        Bookmark::getId
                ));

        return RoadmapListResponse.of(roadmaps, citationCountMap, roadmapIdToBookmarkIdMap);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse buildRoadmapListResponse(List<Roadmap> roadmaps) {
        List<Long> roadmapIds = roadmaps.stream()
                .map(Roadmap::getId)
                .collect(Collectors.toList());

        List<RoadmapCitationProjection> projections =
                layerLibraryRepository.countDistinctMemberByRoadmapIds(roadmapIds);

        Map<Long, Long> citationCountMap = projections.stream()
                .collect(Collectors.toMap(
                        RoadmapCitationProjection::getRoadmapId,
                        RoadmapCitationProjection::getCitationCount
                ));

        Long memberId = authHelper.getCurrentMemberId();
        List<Bookmark> bookmarks = bookmarkRepository.findByMemberId(memberId);

        Map<Long, Long> roadmapIdToBookmarkIdMap = bookmarks.stream()
                .collect(Collectors.toMap(
                        b -> b.getRoadmap().getId(),
                        Bookmark::getId
                ));

        return RoadmapListResponse.of(roadmaps, citationCountMap, roadmapIdToBookmarkIdMap);
    }

    @Transactional
    public RoadmapResponse get(final Long id, final Member member) {
        Roadmap roadmap = roadmapRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 로드맵이 존재하지 않습니다. id=" + id));
        roadmap.setViewCount(roadmap.getViewCount()+1);

        return RoadmapResponse.of(roadmap);
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
        roadmapDTO.setMember(roadmap.getMember() == null ? null : new MemberSimpleDTO(roadmap.getMember()));
        return roadmapDTO;
    }

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
        final Layer mapLayer = layerRepository.findFirstNotDeletedByRoadmap(roadmap);
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
        // roadMap 삭제 기능 시 roadmap 과 hashtag 사이의 중간 테이블 칼럼 삭제
//        final RoadmapHashtagRelation mapRoadmapHashtagRelation = roadmapHashtagRelationRepository.findFirstByRoadmap(roadmap);
//        if (mapRoadmapHashtagRelation != null) {
//            referencedWarning.setKey("roadmap.mapHashtagRelation.roadmap.referenced");
//            referencedWarning.addParam(mapRoadmapHashtagRelation.getId());
//            return referencedWarning;
//        }
        final Report mapReport = reportRepository.findFirstByRoadmap(roadmap);
        if (mapReport != null) {
            referencedWarning.setKey("roadmap.report.roadmap.referenced");
            referencedWarning.addParam(mapReport.getId());
            return referencedWarning;
        }
        return null;
    }



}
