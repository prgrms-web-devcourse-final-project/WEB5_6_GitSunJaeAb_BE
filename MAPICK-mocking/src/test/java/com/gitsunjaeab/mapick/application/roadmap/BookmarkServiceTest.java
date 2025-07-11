package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.domain.roadmap.Bookmark;
import com.gitsunjaeab.mapick.domain.roadmap.BookmarkRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    @Test
    void getBookmarkedRoadmaps_ReturnsRoadmaps() {
        // given
        Long memberId = 1L;

        Roadmap roadmap1 = new Roadmap();
        roadmap1.setId(100L);
        roadmap1.setTitle("Roadmap 1");

        Roadmap roadmap2 = new Roadmap();
        roadmap2.setId(101L);
        roadmap2.setTitle("Roadmap 2");

        Bookmark bookmark1 = new Bookmark();
        bookmark1.setId(10L);
        bookmark1.setRoadmap(roadmap1);

        Bookmark bookmark2 = new Bookmark();
        bookmark2.setId(11L);
        bookmark2.setRoadmap(roadmap2);

        List<Bookmark> bookmarks = List.of(bookmark1, bookmark2);

        // when
        Mockito.when(bookmarkRepository.findByMemberId(memberId)).thenReturn(bookmarks);

        List<Roadmap> result = bookmarkService.getBookmarkedRoadmaps(memberId);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(roadmap1));
        Assertions.assertTrue(result.contains(roadmap2));
    }
}
