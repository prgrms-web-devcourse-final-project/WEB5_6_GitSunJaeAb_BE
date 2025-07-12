package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestResponse;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MemberQuestService {

    private final MemberQuestRepository memberQuestRepository;
    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;

    public MemberQuestService(final MemberQuestRepository memberQuestRepository,
            final QuestRepository questRepository, final MemberRepository memberRepository) {
        this.memberQuestRepository = memberQuestRepository;
        this.questRepository = questRepository;
        this.memberRepository = memberRepository;
    }

    // 전체 참여 목록 조회
    public List<MemberQuestResponse> findAll() {
        final List<MemberQuest> memberQuests = memberQuestRepository.findAll(Sort.by("id"));
        return memberQuests.stream()
                .map(this::toResponse)
                .toList();
    }

    // 특정 퀘스트의 참여자 목록 조회
    public List<MemberQuestResponse> findByQuestId(final Long questId) {
        final List<MemberQuest> memberQuests = memberQuestRepository.findByQuestId(questId);
        return memberQuests.stream()
                .map(this::toResponse)
                .toList();
    }

    // 단일 참여 정보 조회
    public MemberQuestResponse get(final Long id) {
        return memberQuestRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(NotFoundException::new);
    }

    // 퀘스트 참여 신청
    public Long create(final MemberQuestRequest request) {
        final MemberQuest memberQuest = new MemberQuest();
        requestToEntity(request, memberQuest);
        return memberQuestRepository.save(memberQuest).getId();
    }

    // 참여 정보 수정
    public void update(final Long id, final MemberQuestRequest request) {
        final MemberQuest memberQuest = memberQuestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        requestToEntity(request, memberQuest);
        memberQuestRepository.save(memberQuest);
    }

    // 참여 삭제
    public void delete(final Long id) {
        memberQuestRepository.deleteById(id);
    }

    // Entity → Response 변환
    private MemberQuestResponse toResponse(final MemberQuest memberQuest) {
        MemberQuestResponse response = new MemberQuestResponse();
        response.setId(memberQuest.getId());
        response.setStatus(memberQuest.getStatus());
        response.setAnswer(memberQuest.getAnswer());
        response.setIsRecognized(memberQuest.getIsRecognized());
        response.setCreatedAt(memberQuest.getCreatedAt());
        response.setCompletedAt(memberQuest.getCompletedAt());
        response.setUpdatedAt(memberQuest.getUpdatedAt());
        response.setDeletedAt(memberQuest.getDeletedAt());
        response.setMember(memberQuest.getMember() == null ? null : memberQuest.getMember().getId());
        response.setQuest(memberQuest.getQuest() == null ? null : memberQuest.getQuest().getId());
        return response;
    }

    // Request → Entity 변환
    private void requestToEntity(final MemberQuestRequest request, final MemberQuest memberQuest) {
        // 기본 상태 설정
        memberQuest.setStatus("PENDING"); // 대기 상태로 초기화
        memberQuest.setAnswer(""); // 참여 신청 시에는 빈 답변으로 설정 (증빙 제출 시 업데이트)
        memberQuest.setIsRecognized("N"); // 인정 여부 초기값
        
        // 연관 엔티티 설정
        final Quest quest = questRepository.findById(request.getQuest())
                .orElseThrow(() -> new NotFoundException("quest not found"));
        memberQuest.setQuest(quest);
        
        final Member member = memberRepository.findById(request.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        memberQuest.setMember(member);
    }
}
