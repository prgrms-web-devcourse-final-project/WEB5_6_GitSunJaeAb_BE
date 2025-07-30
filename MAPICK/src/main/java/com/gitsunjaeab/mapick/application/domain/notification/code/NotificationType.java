package com.gitsunjaeab.mapick.application.domain.notification.code;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum NotificationType {
    BOOKMARK,         // 좋아요
    QUEST,            // 퀘스트 참여
    QUEST_DEADLINE,   // 퀘스트 마감
    COMMENT,          // 댓글
    ZZIM,             // 찜
    FORK,             // 포크 (인용)
    ANNOUNCEMENT,     // 공지
    ETC,               // 기타

    @JsonEnumDefaultValue
    ALL              // 프론트 조회 전용 필터, DB 저장 안되는 값
}