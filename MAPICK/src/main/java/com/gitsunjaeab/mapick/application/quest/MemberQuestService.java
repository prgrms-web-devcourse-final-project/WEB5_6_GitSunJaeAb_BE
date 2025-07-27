package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestCreateRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestCreateResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestJudgeRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestJudgeResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestUpdateRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestUpdateResponse;
import com.gitsunjaeab.mapick.application.notification.NotificationService;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberQuestService {

    private final MemberQuestRepository memberQuestRepository;
    private final QuestRepository questRepository;
    private final NotificationService notificationService;
    private final SupabaseStorageService supabaseStorageService;
    private final QuestRankService questRankService;


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
    @Transactional
    public MemberQuestCreateResponse createMemberQuest(
        final MemberQuestCreateRequest request,
        final Member member,
        final MultipartFile imageFile
    ) {
        final Quest quest = questRepository.findById(request.getQuestId())
            .orElseThrow(() -> new NotFoundException("퀘스트를 찾을 수 없습니다."));


        final MemberQuest memberQuest = new MemberQuest();
        memberQuest.setQuest(quest);
        memberQuest.setMember(member);
        memberQuest.setTitle(request.getTitle());
        memberQuest.setAnswer(request.getAnswer());
        if(imageFile != null && !imageFile.isEmpty()){
            String uploadedUrl = supabaseStorageService.upload(imageFile);
            memberQuest.setImageUrl(uploadedUrl);
        }

        memberQuest.setDescription(request.getDescription());
        memberQuest.setSubmitAt(OffsetDateTime.now(ZoneId.of("Asia/Seoul")));
        memberQuest.setStatus(true);
        memberQuest.setIsRecognized("N");

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
        return MemberQuestCreateResponse.of(savedQuest);
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


    // (참여자)퀘스트 참여 정보 수정(= 증빙자료나 내용 수정)
    public MemberQuestUpdateResponse updateMemberQuest(
        final Long id,
        final MemberQuestUpdateRequest request,
        final Member member,
        final MultipartFile imageFile
    ) {
        final MemberQuest memberQuest = memberQuestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("참여 내역을 찾을 수 없습니다."));


        // 권한 확인 : 본인것만 수정 가능하게끔
        if (!memberQuest.getMember().getId().equals(member.getId())) {
            throw new NotFoundException("본인의 참여 정보만 수정할 수 있습니다.");
        }
        memberQuest.setTitle(request.getTitle());
        memberQuest.setAnswer(request.getAnswer());

        if(imageFile != null && !imageFile.isEmpty()){
            String uploadedUrl = supabaseStorageService.upload(imageFile);
            memberQuest.setImageUrl(uploadedUrl);
        }

        memberQuest.setDescription(request.getDescription());
        memberQuest.setUpdatedAt(OffsetDateTime.now(ZoneId.of("Asia/Seoul")));

        return MemberQuestUpdateResponse.of(memberQuestRepository.save(memberQuest));
    }

    //(제출자)퀘스트 정답 판정
    public MemberQuestJudgeResponse judgeMemberQuest(
        final MemberQuestJudgeRequest request,
        final Member judgeMember
    ) {
        final MemberQuest memberQuest = memberQuestRepository
            .findWithQuestAndMemberById(request.getMemberQuestId())
            .orElseThrow(() -> new NotFoundException("참여 내역을 찾을 수 없습니다."));


        // 권한 확인: judgeMember = 제출자 (제출자인지 확인)
        if (!memberQuest.getQuest().getMember().getId().equals(judgeMember.getId())) {
            throw new NotFoundException("퀘스트 판정 권한이 없습니다.");
        }


        //Null값 확인
        Boolean recognized = request.getIsRecognized();

        if (recognized == null){
            throw new IllegalStateException("정답여부를 판단해 주세요");
        }

        // 정답 여부 설정
        memberQuest.setIsRecognized(recognized ? "Y" : "N");
        memberQuest.setUpdatedAt(OffsetDateTime.now(ZoneId.of("Asia/Seoul")));

        // 정답일 경우 점수를 부여
        if (recognized){
            final Member submitter = memberQuest.getMember();
            final Quest quest = memberQuest.getQuest();
            questRankService.addScore(submitter,quest,100); //100점씩
        }

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
