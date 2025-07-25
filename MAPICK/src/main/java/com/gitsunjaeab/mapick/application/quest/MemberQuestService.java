package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestResponse;
import com.gitsunjaeab.mapick.application.notification.NotificationService;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberQuestService {

    private final MemberQuestRepository memberQuestRepository;
    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public MemberQuestService(
        final MemberQuestRepository memberQuestRepository,
        final QuestRepository questRepository,
        final MemberRepository memberRepository,
        NotificationService notificationService) {
        this.memberQuestRepository = memberQuestRepository;
        this.questRepository = questRepository;
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
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
        final List<MemberQuest> memberQuests = memberQuestRepository.findWithMemberByQuestId(questId);
        return memberQuests.stream()
                .map(this::toResponse)
                .map(MemberQuestResponse::ofGetList)
                .toList();
    }

    // 단일 참여 정보 조회
    public MemberQuestResponse get(final Long id) {
        return memberQuestRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(NotFoundException::new);
    }
    //-------------------------------------------------------------------------

    // 퀘스트 참여
    @Transactional
    public Long create(final MemberQuestRequest request, final Member member) {
        final MemberQuest memberQuest = new MemberQuest();

        requestToEntity(request, memberQuest); // 기본 데이터 입력
        memberQuest.setMember(member); // 로그인된 사용자 정보 직접 주입
        memberQuest.setSubmitAt(OffsetDateTime.now(ZoneId.of("Asia/Seoul"))); // 현재 시간 세팅

        MemberQuest savedQuest = memberQuestRepository.save(memberQuest);

        // === 참여자 본인에게 알림 발송 ===
        notificationService.createNotification(
            member,              // 참여자
            NotificationType.QUEST,  // 알림 타입
            null,                    // 로드맵
            null,                    // 레이어
            null,                    // 레이어 라이브러리
            savedQuest.getQuest(),   // 퀘스트
            savedQuest,              // 멤버퀘스트
            null,                    // 댓글
            null                     // 북마크
        );
        return savedQuest.getId();
    }

    // 퀘스트 마감 알림발송 로직
    @Transactional
    public void sendDeadlineNotification() {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Seoul"));
        List<MemberQuest> allQuests = memberQuestRepository.findAll();

        for (MemberQuest mq : allQuests) {
            Quest quest = mq.getQuest();
            OffsetDateTime deadline = quest.getDeadline();

            // 마감 시간이 없으면 알림 스킵
            if (deadline == null) {
                continue;
            }

            long diffSec = Duration.between(now, deadline).getSeconds();

            // 마감 1분 전 알림 전송 (테스트용)
            if (diffSec >= 0 && diffSec <= 60) {
                notificationService.createNotification(
                    mq.getMember(),          // 참여자
                    NotificationType.QUEST_DEADLINE,  // 알림 타입
                    null, null, null,        // 로드맵, 레이어, 레이어라이브러리
                    quest,                   // 퀘스트
                    mq,                      // 멤버 퀘스트
                    null, null               // 댓글, 북마크
                );
            }

            // 마감 D-1일이면 알림 전송 (배포용)
//            if (diffSec >= 0 && diffSec <= 60) {
//                notificationService.createNotification(
//                    mq.getMember(),               // 참여자 본인
//                    NotificationType.QUEST_DEADLINE,       // 알림 타입
//                    null, null, null,             // 로드맵, 레이어, 레이어라이브러리
//                    quest,                        // 퀘스트
//                    mq,                           // 멤버퀘스트
//                    null, null                     // 댓글, 북마크
//                );
//            }
        }
    }

//    ----------------------------------------------------------------------
//    // 퀘스트 참여 신청
//    public Long create(final MemberQuestRequest request) {
//        final MemberQuest memberQuest = new MemberQuest();
//        requestToEntity(request, memberQuest);
//        return memberQuestRepository.save(memberQuest).getId();
//    }
//
//    // 퀘스트 참여 신청 후 엔티티 반환
//    public MemberQuest createAndReturnEntity(final MemberQuestRequest request) {
//        final MemberQuest memberQuest = new MemberQuest();
//        requestToEntity(request, memberQuest);
//        return memberQuestRepository.save(memberQuest);
//    }
//---------------------------------------------------------------------------


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

    // Request → Entity 변환
    private void requestToEntity(final MemberQuestRequest request, final MemberQuest memberQuest) {
        // 기본 상태 설정
        memberQuest.setStatus(true); // 대기 상태로 초기화
        memberQuest.setIsRecognized("N"); // 인정 여부 초기값

        //요청값 반영
        memberQuest.setTitle(request.getTitle());
        memberQuest.setAnswer(request.getAnswer());
        memberQuest.setImageUrl(request.getEvidenceImage());
        memberQuest.setDescription(request.getDescription());
        memberQuest.setSubmitAt(java.time.OffsetDateTime.now());


        // 연관 엔티티 설정
        final Quest quest = questRepository.findById(request.getQuest())
                .orElseThrow(() -> new NotFoundException("quest not found"));
        memberQuest.setQuest(quest);

//        memberQuest.setMember(member);
//        //기존 코드
        final Member member = memberRepository.findById(request.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        memberQuest.setMember(member);
    }
}
