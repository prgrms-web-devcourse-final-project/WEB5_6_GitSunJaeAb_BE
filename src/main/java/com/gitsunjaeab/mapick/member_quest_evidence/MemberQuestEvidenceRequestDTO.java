package com.gitsunjaeab.mapick.member_quest_evidence;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberQuestEvidenceRequestDTO {

//    private Long id;

    @NotNull
    @Size(max = 255)
    private String imageUrl;

    private String description;

//    private OffsetDateTime createdAt;
//
//    private OffsetDateTime updatedAt;
//
//    private OffsetDateTime deletedAt;
//
//    private Long memberQuest;

}
