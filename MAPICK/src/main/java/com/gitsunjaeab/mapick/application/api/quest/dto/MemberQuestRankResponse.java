package com.gitsunjaeab.mapick.application.api.quest.dto;

public record MemberQuestRankResponse(
    int rank,
    Long memberId,
    String memberName,
    String memberProfileImageUrl
) {}
