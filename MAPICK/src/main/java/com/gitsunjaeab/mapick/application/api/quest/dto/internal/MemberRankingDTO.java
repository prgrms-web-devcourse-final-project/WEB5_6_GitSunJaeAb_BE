package com.gitsunjaeab.mapick.application.api.quest.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRankingDTO {
    private int rank;
    private String nickname;
    private String profileImageUrl;
}
