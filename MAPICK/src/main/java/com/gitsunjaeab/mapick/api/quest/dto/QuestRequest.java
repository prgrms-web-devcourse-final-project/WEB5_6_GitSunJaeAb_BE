package com.gitsunjaeab.mapick.api.quest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "퀘스트 요청 정보")
public class QuestRequest {

//    private Long id;

    @Schema(description = "퀘스트 제목", example = "도전! 한강 인증샷")
    @NotNull
    @Size(max = 255)
    private String title;


    @Schema(description = "퀘스트 설명", example = "한강에서 찍은 인증샷을 제출하세요.")
    private String description;

    @Schema(description = "퀘스트 마감일", example = "2025-08-01T23:59:59+09:00")
    private OffsetDateTime deadline;

    @Schema(description = "퀘스트 활성화 여부", example = "true")
    @NotNull
    @JsonProperty("isActive")
    private Boolean isActive;

//    @Size(max = 500)
//    private String questImage;
    //파일
//    private MultipartFile imageFile;


//    private OffsetDateTime createdAt; // 퀘스트 생성날짜
//
//    private OffsetDateTime completedAt;  // 퀘스트 완료 처리된 시간
//
//    private OffsetDateTime updatedAt;
//
//
//    private OffsetDateTime deletedAt;
//
//
//    private Long member;

}