package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Bookmark;
import com.gitsunjaeab.mapick.domain.roadmap.BookmarkRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public BookmarkService(final BookmarkRepository bookmarkRepository,
        final RoadmapRepository roadmapRepository, final MemberRepository memberRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void create(Long roadmapId, Long memberId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
            .orElseThrow(() -> new NotFoundException("로드맵을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // 서비스에서 아래 예외 상황은 발생하지 않을 듯
//        if (bookmarkRepository.existsByRoadmapAndMember(roadmap, member)) {
//            throw new ConflictException("이미 북마크한 로드맵입니다.");
//        }

        Bookmark bookmark = new Bookmark();
        bookmark.setRoadmap(roadmap);
        bookmark.setMember(member);
        bookmark.setCreatedAt(OffsetDateTime.now());

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void delete(final Long id) {
        bookmarkRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public RoadmapListResponse getBookmarkedRoadmaps(Long memberId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllWithAllRoadmapRelationsByMemberId(memberId);

        List<Roadmap> roadmaps = bookmarks.stream()
            .map(Bookmark::getRoadmap)
            .toList();

        return RoadmapListResponse.of(roadmaps, Collections.emptyMap());
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
