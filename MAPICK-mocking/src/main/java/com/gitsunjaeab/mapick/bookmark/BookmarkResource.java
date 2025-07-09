package com.gitsunjaeab.mapick.bookmark;

import com.gitsunjaeab.mapick.map.MapRepository;
import com.gitsunjaeab.mapick.member.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/bookmarks", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookmarkResource {

    private final BookmarkService bookmarkService;
    private final MapRepository mapRepository;
    private final MemberRepository memberRepository;

    public BookmarkResource(final BookmarkService bookmarkService,
            final MapRepository mapRepository, final MemberRepository memberRepository) {
        this.bookmarkService = bookmarkService;
        this.mapRepository = mapRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<List<BookmarkDTO>> getAllBookmarks() {
        return ResponseEntity.ok(bookmarkService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmarkDTO> getBookmark(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookmarkService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBookmark(@RequestBody @Valid final BookmarkDTO bookmarkDTO) {
        final Long createdId = bookmarkService.create(bookmarkDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBookmark(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final BookmarkDTO bookmarkDTO) {
        bookmarkService.update(id, bookmarkDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBookmark(@PathVariable(name = "id") final Long id) {
        bookmarkService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mapValues")
    public ResponseEntity<Map<Long, String>> getMapValues() {
        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(com.gitsunjaeab.mapick.map.Map::getId, com.gitsunjaeab.mapick.map.Map::getTitle)));
    }

    @GetMapping("/memberValues")
    public ResponseEntity<Map<Long, String>> getMemberValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

}
