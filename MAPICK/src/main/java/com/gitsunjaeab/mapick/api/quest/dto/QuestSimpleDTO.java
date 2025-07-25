package com.gitsunjaeab.mapick.api.quest.dto;

import com.gitsunjaeab.mapick.domain.quest.Quest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestSimpleDTO {

    private Long id;
    private String title;
    private Long memberId;

    public QuestSimpleDTO(Quest quest) {
        this.id = quest.getId();
        this.title = quest.getTitle();
        this.memberId = quest.getMember() != null ? quest.getMember().getId() : null;
    }

}
