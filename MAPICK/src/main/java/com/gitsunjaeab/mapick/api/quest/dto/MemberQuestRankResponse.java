package com.gitsunjaeab.mapick.api.quest.dto;

public record MemberQuestRankResponse(
    int rank,
    Long memberId,
    String memberName,
    String memberProfileImageUrl
) {}
