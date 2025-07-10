package com.gitsunjaeab.mapick.bookmark;

import com.gitsunjaeab.mapick.bookmark.dto.BookmarkDTO;
import com.gitsunjaeab.mapick.bookmark.entity.Bookmark;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
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
                .map(bookmark -> mapToDTO(bookmark, new BookmarkDTO()))
                .toList();
    }

    public BookmarkDTO get(final Long id) {
        return bookmarkRepository.findById(id)
                .map(bookmark -> mapToDTO(bookmark, new BookmarkDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BookmarkDTO bookmarkDTO) {
        final Bookmark bookmark = new Bookmark();
        mapToEntity(bookmarkDTO, bookmark);
        return bookmarkRepository.save(bookmark).getId();
    }

    public void update(final Long id, final BookmarkDTO bookmarkDTO) {
        final Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookmarkDTO, bookmark);
        bookmarkRepository.save(bookmark);
    }

    public void delete(final Long id) {
        bookmarkRepository.deleteById(id);
    }

    private BookmarkDTO mapToDTO(final Bookmark bookmark, final BookmarkDTO bookmarkDTO) {
        bookmarkDTO.setId(bookmark.getId());
        bookmarkDTO.setCreatedAt(bookmark.getCreatedAt());
        bookmarkDTO.setMap(bookmark.getRoadmap() == null ? null : bookmark.getRoadmap().getId());
        bookmarkDTO.setMember(bookmark.getMember() == null ? null : bookmark.getMember().getId());
        return bookmarkDTO;
    }

    private Bookmark mapToEntity(final BookmarkDTO bookmarkDTO, final Bookmark bookmark) {
        bookmark.setCreatedAt(bookmarkDTO.getCreatedAt());
        final Roadmap roadmap = bookmarkDTO.getMap() == null ? null : roadmapRepository.findById(bookmarkDTO.getMap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        bookmark.setRoadmap(roadmap);
        final Member member = bookmarkDTO.getMember() == null ? null : memberRepository.findById(bookmarkDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        bookmark.setMember(member);
        return bookmark;
    }

}
