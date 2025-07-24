package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapListResponse;
import com.gitsunjaeab.mapick.application.notification.NotificationService;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import com.gitsunjaeab.mapick.domain.roadmap.Bookmark;
import com.gitsunjaeab.mapick.domain.roadmap.BookmarkRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.DuplicatedBookmarkException;
import com.gitsunjaeab.mapick.infra.error.exceptions.ForbiddenException;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public BookmarkService(final BookmarkRepository bookmarkRepository,
        final RoadmapRepository roadmapRepository, final MemberRepository memberRepository,
        NotificationService notificationService) {
        this.bookmarkRepository = bookmarkRepository;
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void create(Long roadmapId, Long memberId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
            .orElseThrow(() -> new NotFoundException("로드맵을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        if (bookmarkRepository.existsByMemberAndRoadmap(member, roadmap)) {
            throw new DuplicatedBookmarkException(ResponseCode.BOOKMARK_DUPLICATED);
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setRoadmap(roadmap);
        bookmark.setMember(member);
        bookmark.setCreatedAt(OffsetDateTime.now());

        // === 알림 발송 로직 (북마크 시)===
        Member roadmapOwner = roadmap.getMember();
        if (!roadmapOwner.getId().equals(memberId)) {
            notificationService.createNotification(
                roadmapOwner,           // 알림 수신자
                NotificationType.POST,  // 알림 타입
                roadmap,                // 로드맵
                null,                   // 레이어
                null,                   // 레이어 라이브러리
                null,                   // 퀘스트
                null,                   // 멤버퀘스트
                null,                   // 댓글
                bookmark                // 북마크
            );
        }
        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void delete(Long bookmarkId, Long memberId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NotFoundException("북마크가 존재하지 않습니다."));

        if (!bookmark.getMember().getId().equals(memberId)) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getBookmarkedRoadmaps(Long memberId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllWithAllRoadmapRelationsByMemberId(memberId);

        List<Roadmap> roadmaps = bookmarks.stream()
            .map(Bookmark::getRoadmap)
            .toList();

        Set<Long> bookmarkedIds = roadmaps.stream()
                .map(Roadmap::getId)
                .collect(Collectors.toSet());
        return RoadmapListResponse.of(roadmaps, Collections.emptyMap(), bookmarkedIds);
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
