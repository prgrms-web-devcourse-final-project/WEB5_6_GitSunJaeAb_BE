//package com.gitsunjaeab.mapick.api.roadmap;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.gitsunjaeab.mapick.application.roadmap.BookmarkService;
//import com.gitsunjaeab.mapick.domain.category.Category;
//import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerLibraryRepository;
//import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerLibraryRepository.RoadmapCitationProjection;
//import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
//import java.util.HashSet;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(BookmarkController.class)
//public class BookmarkControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private BookmarkService bookmarkService;
//
//    @MockitoBean
//    private LayerLibraryRepository layerLibraryRepository;
//
//    @Test
//    void getMyBookmarkedRoadmaps_ReturnsList() throws Exception {
//        // given
//        Long memberId = 1L;
//
//        Roadmap roadmap = new Roadmap();
//        roadmap.setId(1L);
//        Category category = new Category();
//        category.setName("음식");
//        roadmap.setCategory(category);
//        roadmap.setTitle("잠실 맛집 로드맵");
//        roadmap.setDescription("서울 토박이가 알려주는 잠실 맛집 정보");
//        roadmap.setLikeCount(10);
//        roadmap.setViewCount(20);
//
//
//        List<Roadmap> roadmaps = List.of(roadmap);
//
//        RoadmapCitationProjection projection = new RoadmapCitationProjection() {
//            @Override
//            public Long getRoadmapId() {
//                return 1L;
//            }
//
//            @Override
//            public Long getCitationCount() {
//                return 5L;
//            }
//        };
//
//        Mockito.when(bookmarkService.getBookmarkedRoadmaps(memberId)).thenReturn(roadmaps);
//        Mockito.when(layerLibraryRepository.countDistinctMemberByRoadmapIds(List.of(1L)))
//            .thenReturn(List.of(projection));
//
//        // when & then
//        mockMvc.perform(get("/bookmarks/bookmarkedRoadmaps")
//                .contentType(MediaType.APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.code").value("2000"))
//            .andExpect(jsonPath("$.roadmaps[0].title").value("잠실 맛집 로드맵"))
//            .andExpect(jsonPath("$.roadmaps[0].citationCount").value(5));
//    }
//}
