package com.gitsunjaeab.mapick.application.api.quest.dto.request;


import lombok.Getter;
import lombok.Setter;

//생성이랑 구조가 같아서 차후 바뀔지도? 헷갈려서 일단 다 분리함
//사용자 제출한 증빙자료 수정
@Getter
@Setter
public class MemberQuestUpdateRequest {

    private Long memberQuestId;

    private String title; //수정된 증빙 제목
    private String description; // 수정된 설명
    private String answer; // 수정된 정답


}
