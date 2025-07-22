package com.gitsunjaeab.mapick.api.search.dto;

import com.gitsunjaeab.mapick.api.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.roadmap.RoadmapDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResultResponse implements BaseApiResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;

    private List<RoadmapDTO> roadmaps;
    private List<QuestDTO> quests;

}
