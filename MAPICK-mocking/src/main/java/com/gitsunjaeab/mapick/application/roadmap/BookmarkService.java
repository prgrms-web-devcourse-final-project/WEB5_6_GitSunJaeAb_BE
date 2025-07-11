package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.roadmap.dto.bookmark.BookmarkDTO;
import com.gitsunjaeab.mapick.domain.roadmap.BookmarkRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Bookmark;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


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

    public List<BookmarkDTO> findAll() {
        final List<Bookmark> bookmarks = bookmarkRepository.findAll(Sort.by("id"));
        return bookmarks.stream()
                .map(bookmark -> roadmapToDTO(bookmark, new BookmarkDTO()))
                .toList();
    }

    public BookmarkDTO get(final Long id) {
        return bookmarkRepository.findById(id)
                .map(bookmark -> roadmapToDTO(bookmark, new BookmarkDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BookmarkDTO bookmarkDTO) {
        final Bookmark bookmark = new Bookmark();
        roadmapToEntity(bookmarkDTO, bookmark);
        return bookmarkRepository.save(bookmark).getId();
    }

    public void update(final Long id, final BookmarkDTO bookmarkDTO) {
        final Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(bookmarkDTO, bookmark);
        bookmarkRepository.save(bookmark);
    }

    public void delete(final Long id) {
        bookmarkRepository.deleteById(id);
    }

    private BookmarkDTO roadmapToDTO(final Bookmark bookmark, final BookmarkDTO bookmarkDTO) {
        bookmarkDTO.setId(bookmark.getId());
        bookmarkDTO.setCreatedAt(bookmark.getCreatedAt());
        bookmarkDTO.setRoadmap(bookmark.getRoadmap() == null ? null : bookmark.getRoadmap().getId());
        bookmarkDTO.setMember(bookmark.getMember() == null ? null : bookmark.getMember().getId());
        return bookmarkDTO;
    }

    private Bookmark roadmapToEntity(final BookmarkDTO bookmarkDTO, final Bookmark bookmark) {
        bookmark.setCreatedAt(bookmarkDTO.getCreatedAt());
        final Roadmap roadmap = bookmarkDTO.getRoadmap() == null ? null : roadmapRepository.findById(bookmarkDTO.getRoadmap())
                .orElseThrow(() -> new NotFoundException("roadmap not found"));
        bookmark.setRoadmap(roadmap);
        final Member member = bookmarkDTO.getMember() == null ? null : memberRepository.findById(bookmarkDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        bookmark.setMember(member);
        return bookmark;
    }

}
