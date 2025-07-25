package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestCreateRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestCreateResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestJudgeRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestJudgeResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestUpdateRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestUpdateResponse;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestResponse;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQuestService {

    private final MemberQuestRepository memberQuestRepository;
    private final QuestRepository questRepository;
//    private final MemberRepository memberRepository;

//    public MemberQuestService(
//        final MemberQuestRepository memberQuestRepository,
//        final QuestRepository questRepository,
//        final MemberRepository memberRepository
//    ) {
//        this.memberQuestRepository = memberQuestRepository;
//        this.questRepository = questRepository;
//        this.memberRepository = memberRepository;
//    }

    // 전체 참여 목록 조회 //확인
    public List<MemberQuestResponse> findAll() {
        final List<MemberQuest> memberQuests = memberQuestRepository.findAll(Sort.by("id"));
        return memberQuests.stream()
                .map(this::toResponse)
                .toList();
    }

    // 특정 퀘스트의 참여자 목록 조회 //확인
    public List<MemberQuestResponse> findByQuestId(final Long questId) {
        final List<MemberQuest> memberQuests = memberQuestRepository.findWithMemberByQuestId(questId);
        return memberQuests.stream()
                .map(this::toResponse)
                .map(MemberQuestResponse::ofGetList)
                .toList();
    }

    // 단일 참여 정보 조회 //확인
    public MemberQuestResponse get(final Long id) {
        return memberQuestRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(NotFoundException::new);
    }
    //-------------------------------------------------------------------------

    //(참여자) 퀘스트 참여
    public MemberQuestCreateResponse createMemberQuest(final MemberQuestCreateRequest request, final Member member) {
        final Quest quest = questRepository.findById(request.getQuestId())
            .orElseThrow(() -> new NotFoundException("퀘스트를 찾을 수 없습니다."));

        final MemberQuest memberQuest = new MemberQuest();
        memberQuest.setQuest(quest);
        memberQuest.setMember(member);
        memberQuest.setTitle(request.getTitle());
        memberQuest.setAnswer(request.getAnswer());
        memberQuest.setImageUrl(request.getEvidenceImage());
        memberQuest.setDescription(request.getDescription());
        memberQuest.setSubmitAt(OffsetDateTime.now());
        memberQuest.setStatus(true);
        memberQuest.setIsRecognized("N");

        MemberQuest saved = memberQuestRepository.save(memberQuest);
        return MemberQuestCreateResponse.of(saved);

    }


    // (참여자)퀘스트 참여 정보 수정(= 증빙자료나 내용 수정)
    public MemberQuestUpdateResponse updateMemberQuest(
        final Long id,
        final MemberQuestUpdateRequest request,
        final Member member
    ) {
        final MemberQuest memberQuest = memberQuestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("참여 내역을 찾을 수 없습니다."));


        // 권한 확인 : 본인것만 수정 가능하게끔
        if (!memberQuest.getMember().getId().equals(member.getId())) {
            throw new NotFoundException("본인의 참여 정보만 수정할 수 있습니다.");
        }
        memberQuest.setTitle(request.getTitle());
        memberQuest.setAnswer(request.getAnswer());
        memberQuest.setImageUrl(request.getEvidenceImage());
        memberQuest.setDescription(request.getDescription());
        memberQuest.setUpdatedAt(OffsetDateTime.now());

        return MemberQuestUpdateResponse.of(memberQuestRepository.save(memberQuest));
    }

    //(제출자)퀘스트 정답 판정
    public MemberQuestJudgeResponse judgeMemberQuest(
        final MemberQuestJudgeRequest request,
        final Member judgeMember
    ) {
        final MemberQuest memberQuest = memberQuestRepository.findById(request.getMemberQuestId())
            .orElseThrow(() -> new NotFoundException("참여 내역을 찾을 수 없습니다."));

        // 권한 확인: judgeMember = 제출자 (제출자인지 확인)
        if (!memberQuest.getQuest().getMember().getId().equals(judgeMember.getId())) {
            throw new NotFoundException("퀘스트 판정 권한이 없습니다.");
        }

        // 정답 여부 설정
        memberQuest.setIsRecognized(request.getIsRecognized() ? "Y" : "N");
        memberQuest.setUpdatedAt(OffsetDateTime.now());

        // 저장 후 DTO로 반환
        return MemberQuestJudgeResponse.of(memberQuestRepository.save(memberQuest));
    }

    // 참여 취소 이건 추후 상황봐서
    public void deleteMemberQuest(final Long id) {
        memberQuestRepository.deleteById(id);
    }

    // Entity → Response 변환
    private MemberQuestResponse toResponse(final MemberQuest memberQuest) {
        MemberQuestResponse response = new MemberQuestResponse();
        response.setId(memberQuest.getId());
        response.setStatus(memberQuest.getStatus());
        response.setTitle(memberQuest.getTitle());
        response.setAnswer(memberQuest.getAnswer());
        response.setIsRecognized(memberQuest.getIsRecognized());
        response.setImageUrl(memberQuest.getImageUrl());
        response.setDescription(memberQuest.getDescription());
        response.setSubmitAt(memberQuest.getSubmitAt());
        response.setCreatedAt(memberQuest.getCreatedAt());
        response.setCompletedAt(memberQuest.getCompletedAt());
        response.setUpdatedAt(memberQuest.getUpdatedAt());
        response.setDeletedAt(memberQuest.getDeletedAt());
        response.setMember(memberQuest.getMember() == null ? null : new MemberSimpleDTO(memberQuest.getMember()));
        response.setQuest(memberQuest.getQuest() == null ? null : memberQuest.getQuest().getId());
        return response;
    }

//    // Request → Entity 변환
//    private void requestToEntity(final MemberQuestRequest request, final MemberQuest memberQuest) {
//        // 기본 상태 설정
//        memberQuest.setStatus(true); // 대기 상태로 초기화
//        memberQuest.setIsRecognized("N"); // 인정 여부 초기값
//
//        //요청값 반영
//        memberQuest.setTitle(request.getTitle());
//        memberQuest.setAnswer(request.getAnswer());
//        memberQuest.setImageUrl(request.getEvidenceImage());
//        memberQuest.setDescription(request.getDescription());
//        memberQuest.setSubmitAt(java.time.OffsetDateTime.now());


//        // 연관 엔티티 설정
//        final Quest quest = questRepository.findById(request.getQuest())
//                .orElseThrow(() -> new NotFoundException("quest not found"));
//        memberQuest.setQuest(quest);
//
////        memberQuest.setMember(member);
////        //기존 코드
//        final Member member = memberRepository.findById(request.getMember())
//                .orElseThrow(() -> new NotFoundException("member not found"));
//        memberQuest.setMember(member);
//    }
}
