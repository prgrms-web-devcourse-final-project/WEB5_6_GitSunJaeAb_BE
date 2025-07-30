package com.gitsunjaeab.mapick.application.domain.roadmap.roadmap;

import com.gitsunjaeab.mapick.application.api.achievement.dto.AchievementDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag.HashtagRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.RoadmapAchievementDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.request.RoadmapRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.request.SharedRoadmapCreateRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.request.SharedRoadmapUpdateRequest;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.response.RoadmapListResponse;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.response.RoadmapResponse;
import com.gitsunjaeab.mapick.application.domain.roadmap.hashtag.RoadmapHashtagRelation;
import com.gitsunjaeab.mapick.application.domain.roadmap.hashtag.RoadmapHashtagRelationRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.bookmark.Bookmark;
import com.gitsunjaeab.mapick.application.domain.roadmap.bookmark.BookmarkRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.hashtag.Hashtag;
import com.gitsunjaeab.mapick.application.domain.roadmap.hashtag.HashtagRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.hashtag.HashtagService;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerService;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmapeditor.RoadmapEditorRepository;
import com.gitsunjaeab.mapick.infra.common.EntityFinder;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.application.domain.achievement.AchievementRepository;
import com.gitsunjaeab.mapick.application.domain.achievement.MemberAchievement;
import com.gitsunjaeab.mapick.application.domain.achievement.MemberAchievementRepository;
import com.gitsunjaeab.mapick.application.domain.category.Category;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibraryRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerLibraryRepository.RoadmapCitationProjection;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.error.exceptions.InvalidRoadmapTypeException;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final BookmarkRepository bookmarkRepository;
    private final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository;
    private final LayerService layerService;
    private final HashtagService hashtagService;
    private final HashtagRepository hashtagRepository;
    private final EntityFinder entityFinder;
    private final SupabaseStorageService supabaseStorageService;
    private final LayerLibraryRepository layerLibraryRepository;
    private final MemberAchievementRepository memberAchievementRepository;
    private final AchievementRepository achievementRepository;

    // 공유 지도 생성
    @Transactional
    public RoadmapAchievementDTO createSharedRoadmap(SharedRoadmapCreateRequest request, Long memberId, MultipartFile imageFile) {
        Member member = entityFinder.findMemberById(memberId);

        Category category = entityFinder.findCategoryById(request.getCategoryId());

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
            roadmap.setThumbnail(uploadImage(imageFile));
        }

        roadmapRepository.save(roadmap);
        // 로드맵 생성 시 해시태그
        createHashtags(request.getHashtags(), roadmap);

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
        Roadmap roadmap = entityFinder.findRoadmapById(roadmapId);
        Category category = entityFinder.findCategoryById(request.getCategoryId());
        // 본인 소유인지 검증
        if (!roadmap.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("이 로드맵을 수정할 권한이 없습니다.");
        }

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
            roadmap.setThumbnail(uploadImage(imageFile));
        }

        if (request.getHashtags() != null) {
            updateHashtags(roadmap, request.getHashtags());
        }
    }

    // 개인 로드맵 생성
    @Transactional
    public RoadmapAchievementDTO createRoadmap(@Valid RoadmapRequest request, Long memberId, MultipartFile imageFile) {
        Category category = entityFinder.findCategoryById(request.getCategoryId());
        Member member = entityFinder.findMemberById(memberId);

        Roadmap roadmap = new Roadmap();
        roadmap.setCategory(category);
        roadmap.setTitle(request.getTitle());
        roadmap.setDescription(request.getDescription());
        roadmap.setIsPublic(request.getIsPublic());
        roadmap.setIsAnimated(false);
        roadmap.setRoadmapType(RoadmapType.PERSONAL);
        roadmap.setCreatedAt(OffsetDateTime.now());
        roadmap.setLikeCount(0);
        roadmap.setViewCount(0);
        roadmap.setMember(member);

        if (imageFile != null && !imageFile.isEmpty()) {
            roadmap.setThumbnail(uploadImage(imageFile));
        }

        roadmapRepository.save(roadmap);
        createHashtags(request.getHashtags(), roadmap);

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
        Roadmap roadmap = entityFinder.findRoadmapById(roadmapId);
        Category category = entityFinder.findCategoryById(request.getCategoryId());

        // 본인 소유인지 검증
        if (!roadmap.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("이 로드맵을 수정할 권한이 없습니다.");
        }

        if (roadmap.getRoadmapType() != RoadmapType.PERSONAL) {
            throw new InvalidRoadmapTypeException("개인 로드맵이 아닙니다.");
        }

        roadmap.setTitle(request.getTitle());
        roadmap.setDescription(request.getDescription());
        roadmap.setIsPublic(request.getIsPublic());
        roadmap.setCategory(category);
        roadmap.setUpdatedAt(OffsetDateTime.now());
        if (imageFile != null && !imageFile.isEmpty()) {
            roadmap.setThumbnail(uploadImage(imageFile));
        }

        // 해시태그 수정: null이 아닐 때만 처리
        if (request.getHashtags() != null) {
            updateHashtags(roadmap, request.getHashtags());
        }
    }

    // 로드맵 삭제
    @Transactional
    public void delete(Long roadmapId, Member member) {
        Roadmap roadmap = entityFinder.findRoadmapById(roadmapId);
        boolean isOwner = roadmap.getMember().getId().equals(member.getId());
        boolean isAdmin = member.getRole().equals("ROLE_ADMIN");

        if (!(isOwner || (isAdmin))) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        roadmapHashtagRelationRepository.deleteByRoadmap(roadmap);
        List<Layer> layers = layerRepository.findByRoadmapIdAndDeletedAtIsNull(roadmapId);
        for (Layer layer : layers) {
            layerService.delete(layer.getId(), member.getId());
        }
        roadmap.setDeletedAt(OffsetDateTime.now());
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getPersonalRoadmaps(Member member, Long categoryId) {
        List<Roadmap> roadmaps;

        if (categoryId == null) {
            roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndDeletedAtIsNull(RoadmapType.PERSONAL);
        } else {
            Category category = entityFinder.findCategoryById(categoryId);
            roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndCategoryId(RoadmapType.PERSONAL, category.getId());
        }

        return buildRoadmapListResponse(roadmaps, member);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getSharedRoadmaps(Member member, Long categoryId) {
        List<Roadmap> roadmaps;

        if (categoryId == null) {
            roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndDeletedAtIsNull(RoadmapType.SHARED);
        } else {
            Category category = entityFinder.findCategoryById(categoryId);
            roadmaps = roadmapRepository.findAllByIsPublicTrueAndRoadmapTypeAndCategoryId(RoadmapType.SHARED, category.getId());
        }

        return buildRoadmapListResponse(roadmaps, member);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse findAllRoadmapsByMember(Member member) {
        List<Roadmap> roadmaps = roadmapRepository.findAllByMember_IdAndDeletedAtIsNull(member.getId());
        return buildRoadmapListResponse(roadmaps, member);
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
    public RoadmapListResponse buildRoadmapListResponse(List<Roadmap> roadmaps, Member member) {
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

        Map<Long, Long> roadmapIdToBookmarkIdMap = Collections.emptyMap();
        if (member != null) {
            List<Bookmark> bookmarks = bookmarkRepository.findByMemberId(member.getId());
            roadmapIdToBookmarkIdMap = bookmarks.stream()
                    .collect(Collectors.toMap(
                            b -> b.getRoadmap().getId(),
                            Bookmark::getId
                    ));
        }
        return RoadmapListResponse.of(roadmaps, citationCountMap, roadmapIdToBookmarkIdMap);
    }

    @Transactional
    public RoadmapResponse get(final Long id, final Member member) {
        Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 로드맵이 존재하지 않습니다. id=" + id));
        roadmap.setViewCount(roadmap.getViewCount() + 1);

        return RoadmapResponse.of(roadmap);
    }

    private String uploadImage(MultipartFile imageFile) {
        try {
            return supabaseStorageService.upload(imageFile);
        } catch (RuntimeException e) {
            throw new CommonException(ResponseCode.FILE_UPLOAD_FAILED);
        }
    }

    private void createHashtags(List<HashtagRequest> hashtagRequests, Roadmap roadmap) {
        if (hashtagRequests == null || hashtagRequests.isEmpty()) return;

        Set<Long> seen = new HashSet<>();
        List<Hashtag> hashtags = hashtagService.findOrCreateHashtags(hashtagRequests);

        List<RoadmapHashtagRelation> relations = new ArrayList<>();
        for (Hashtag tag : hashtags) {
            if (seen.add(tag.getId())) {
                RoadmapHashtagRelation relation = new RoadmapHashtagRelation(roadmap, tag);
                relations.add(relation);
            }
        }

        roadmap.getRoadmapMapHashtags().addAll(relations);
        roadmapHashtagRelationRepository.saveAll(relations);
    }


    private void updateHashtags(Roadmap roadmap, List<HashtagRequest> hashtagRequests) {
        roadmapHashtagRelationRepository.deleteByRoadmap(roadmap);

        if (hashtagRequests.isEmpty()) return;

        List<RoadmapHashtagRelation> newRelations = hashtagRequests.stream()
                .map(dto -> {
                    Hashtag hashtag = hashtagRepository.findByName(dto.getName())
                            .orElseGet(() -> hashtagRepository.save(new Hashtag(dto.getName())));
                    return new RoadmapHashtagRelation(roadmap, hashtag);
                })
                .collect(Collectors.toList());

        roadmapHashtagRelationRepository.saveAll(newRelations);
    }

}
