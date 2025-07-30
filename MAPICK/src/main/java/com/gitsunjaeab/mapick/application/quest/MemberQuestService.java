package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestCreateRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestCreateResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestJudgeRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestJudgeResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestRankResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestSubmissionDTO;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestUpdateRequest;
import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestUpdateResponse;
import com.gitsunjaeab.mapick.api.quest.dto.MemberRankingDTO;
import com.gitsunjaeab.mapick.application.notification.NotificationService;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.infra.common.EntityFinder;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberQuestService {

    private final MemberQuestRepository memberQuestRepository;
    private final NotificationService notificationService;
    private final SupabaseStorageService supabaseStorageService;
    private final EntityFinder entityFinder;


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
        MemberQuest memberQuest = entityFinder.findByMemberQuestId(id);
        return toResponse(memberQuest);
    }

    //(참여자) 내가 여태 참여한 퀘스트 조회
    public List<MemberQuestResponse> findByMember(Member member) {
        return memberQuestRepository.findByMember(member).stream()
            .map(this::toResponse)
            .toList();
    }

    //-------------------------------------------------------------------------

    //(참여자) 퀘스트 참여
    @Transactional
    public MemberQuestCreateResponse createMemberQuest(
        final MemberQuestCreateRequest request,
        final Member member,
        final MultipartFile imageFile
    ) {
        final Quest quest = entityFinder.findQuestById(request.getQuestId());


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


        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Seoul"));
        memberQuest.setCreatedAt(now);
        memberQuest.setSubmitAt(now);

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
        }
    }

    // (참여자)퀘스트 참여 정보 수정(= 증빙자료나 내용 수정)
    public MemberQuestUpdateResponse updateMemberQuest(
        final Long id,
        final MemberQuestUpdateRequest request,
        final Member member,
        final MultipartFile imageFile
    ) {
        final MemberQuest memberQuest = entityFinder.findByMemberQuestId(id);

        // 권한 확인 : 본인것만 수정 가능하게끔
        if (!memberQuest.getMember().getId().equals(member.getId())) {
            throw new CommonException(ResponseCode.INVALID_ACCESS, "본인의 참여 정보만 수정할 수 있습니다.");
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
        final MemberQuest memberQuest = entityFinder.findWithQuestAndMemberById(request.getMemberQuestId());
        // 권한 확인: judgeMember = 제출자 (제출자인지 확인)
        if (!memberQuest.getQuest().getMember().getId().equals(judgeMember.getId())) {
            // 퀘스트 판정 권한이 없음
            throw new CommonException(ResponseCode.FORBIDDEN_USER, "퀘스트 판정 권한이 없습니다.");
        }
        //Null값 확인
        Boolean recognized = request.getIsRecognized();
        if (recognized == null){
            throw new IllegalStateException("정답여부를 판단해 주세요");
        }

        // 정답 여부 설정
        memberQuest.setIsRecognized(recognized ? "Y" : "N");
        memberQuest.setUpdatedAt(OffsetDateTime.now(ZoneId.of("Asia/Seoul")));

        // 저장 후 DTO로 반환
        return MemberQuestJudgeResponse.of(memberQuestRepository.save(memberQuest));
    }

    // 퀘스트별 랭킹 정렬 조회
    public List<MemberQuestRankResponse> getRankedMembersByQuest(final Long questId) {
        // 정답 처리된 참여자만 조회
        final List<MemberQuest> recognized = memberQuestRepository.findByQuestIdAndRecognizedTrue(questId);

        // 정렬: createdAt → updatedAt 순
        final List<MemberQuest> sorted = recognized.stream()
            .sorted(Comparator.comparing(memberQuest ->
                memberQuest.getCreatedAt() != null ? memberQuest.getCreatedAt() : memberQuest.getUpdatedAt()
            ))
            .toList();

        // 랭크 매기기
        AtomicInteger rankCounter = new AtomicInteger(1);
        return sorted.stream()
            .map(mq -> new MemberQuestRankResponse(
                rankCounter.getAndIncrement(),
                mq.getMember().getId(),
                mq.getMember().getNickname(),
                mq.getMember().getProfileImage()
            ))
            .toList();
    }

    //top3 선별
    public List<MemberQuestRankResponse> getTop3RankedMembersByQuest(Long questId) {
        List<MemberQuestRankResponse> fullRanking = getRankedMembersByQuest(questId);
        return fullRanking.stream().limit(3).toList();
    }


    // 참여 취소 이건 추후 상황봐서
//    public void deleteMemberQuest(final Long id) {
//        memberQuestRepository.deleteById(id);
//    }

    public List<MemberQuestSubmissionDTO> getSubmissions(Long questId) {

        final List<MemberQuest> memberQuests = memberQuestRepository.findWithMemberByQuestId(questId);

        return memberQuests.stream()
            .map(memberQuest -> new MemberQuestSubmissionDTO(
                memberQuest.getImageUrl(),
                "Y".equalsIgnoreCase(memberQuest.getIsRecognized()),
                memberQuest.getMember().getNickname(),
                memberQuest.getUpdatedAt() != null ? memberQuest.getUpdatedAt() : memberQuest.getCreatedAt(),
                memberQuest.getTitle(),
                memberQuest.getDescription(),
                memberQuest.getMember().getProfileImage()
            ))
            .toList();
    }

    //
    public List<MemberRankingDTO> getRanking(Long questId) {
        final List<MemberQuest> recognized = memberQuestRepository.findByQuestIdAndRecognizedTrue(questId);

        final List<MemberQuest> sorted = recognized.stream()
            .sorted(Comparator.comparing(memberQuest ->
                memberQuest.getCreatedAt() != null ? memberQuest.getCreatedAt() : memberQuest.getUpdatedAt()
            ))
            .toList();

        AtomicInteger rankCounter = new AtomicInteger(1);
        return sorted.stream()
            .map(memberQuest -> new MemberRankingDTO(
                rankCounter.getAndIncrement(),
                memberQuest.getMember().getNickname(),
                memberQuest.getMember().getProfileImage()
            ))
            .toList();
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

}
