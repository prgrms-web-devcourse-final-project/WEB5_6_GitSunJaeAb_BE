package com.gitsunjaeab.mapick.application.domain.roadmap.bookmark;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.bookmark.internal.BookmarkDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.roadmap.response.RoadmapListResponse;
import com.gitsunjaeab.mapick.application.domain.notification.NotificationService;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.infra.common.EntityFinder;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.notification.code.NotificationType;
import com.gitsunjaeab.mapick.infra.error.exceptions.DuplicatedBookmarkException;
import com.gitsunjaeab.mapick.infra.error.exceptions.ForbiddenException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final NotificationService notificationService;
    private final EntityFinder entityFinder;

    @Transactional
    public Long create(Long roadmapId, Long memberId) {
        Roadmap roadmap = entityFinder.findRoadmapById(roadmapId);
        Member member = entityFinder.findMemberById(memberId);

        if (bookmarkRepository.existsByMemberIdAndRoadmapId(member.getId(), roadmap.getId())) {
            throw new DuplicatedBookmarkException(ResponseCode.BOOKMARK_DUPLICATED);
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setRoadmap(roadmap);
        bookmark.setMember(member);
        bookmark.setCreatedAt(OffsetDateTime.now());
        roadmap.setLikeCount(roadmap.getLikeCount() + 1);

        // === 알림 발송 로직 (북마크 시)===
        Member roadmapOwner = roadmap.getMember();
        if (!roadmapOwner.getId().equals(memberId)) {
            notificationService.createNotification(
                roadmapOwner,           // 알림 수신자
                NotificationType.BOOKMARK,  // 알림 타입
                roadmap,                // 로드맵
                null,                   // 레이어
                null,                   // 레이어 라이브러리
                null,                   // 퀘스트
                null,                   // 멤버퀘스트
                null,                   // 댓글
                bookmark                // 북마크
            );
        }
        Bookmark saved = bookmarkRepository.save(bookmark);
        return saved.getId();
    }

    @Transactional
    public void delete(Long bookmarkId, Long memberId) {
        Bookmark bookmark = entityFinder.findBookmarkById(bookmarkId);

        if (!bookmark.getMember().getId().equals(memberId)) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        Roadmap roadmap = bookmark.getRoadmap();
        Integer currentLikeCount = roadmap.getLikeCount();
        if (currentLikeCount != null && currentLikeCount > 0) {
            roadmap.setLikeCount(currentLikeCount - 1);
        }

        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getBookmarkedRoadmaps(Long memberId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllWithAllRoadmapRelationsByMemberId(memberId);

        List<Roadmap> roadmaps = bookmarks.stream()
                .map(Bookmark::getRoadmap)
                .toList();

        Map<Long, Long> roadmapIdToBookmarkIdMap = bookmarks.stream()
                .collect(Collectors.toMap(
                        b -> b.getRoadmap().getId(),
                        Bookmark::getId
                ));

        return RoadmapListResponse.of(roadmaps, Collections.emptyMap(), roadmapIdToBookmarkIdMap);
    }

    public List<BookmarkDTO> findAll() {
        final List<Bookmark> bookmarks = bookmarkRepository.findAll(Sort.by("id"));
        return bookmarks.stream()
            .map(bookmark -> roadmapToDTO(bookmark, new BookmarkDTO()))
            .toList();
    }

    private BookmarkDTO roadmapToDTO(final Bookmark bookmark, final BookmarkDTO bookmarkDTO) {
        bookmarkDTO.setId(bookmark.getId());
        bookmarkDTO.setCreatedAt(bookmark.getCreatedAt());
        bookmarkDTO.setRoadmap(
            bookmark.getRoadmap() == null ? null : bookmark.getRoadmap().getId());
        bookmarkDTO.setMember(bookmark.getMember() == null ? null : bookmark.getMember().getId());
        return bookmarkDTO;
    }

}
