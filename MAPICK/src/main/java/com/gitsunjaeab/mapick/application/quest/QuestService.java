package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import com.gitsunjaeab.mapick.api.member.dto.internal.MemberSimpleDTO;
import com.gitsunjaeab.mapick.api.quest.dto.QuestAchievementResponse;
import com.gitsunjaeab.mapick.api.quest.dto.QuestRequest;
import com.gitsunjaeab.mapick.api.quest.dto.QuestResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.domain.achievement.AchievementRepository;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievement;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievementRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import com.gitsunjaeab.mapick.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;
    private final MemberAchievementRepository memberAchievementRepository;
    private final AchievementRepository achievementRepository;
    private final SupabaseStorageService supabaseStorageService;

    // 만료 퀘스트 자동 비활성화
    @Scheduled(cron = "0 */1 * * * *") // 1분마다 확인
    @Transactional
    public void deactivateExpiredQuests() {
        OffsetDateTime now = OffsetDateTime.now();
        List<Quest> expiredQuests = questRepository.findAllByDeadlineBeforeAndIsActiveTrue(now);

        for (Quest quest : expiredQuests) {
            quest.setIsActive(false);
        }

        if (!expiredQuests.isEmpty()) {
            questRepository.saveAll(expiredQuests);
            System.out.println("[스케줄러] 만료 퀘스트 " + expiredQuests.size() + "건 비활성화 완료!");
        }
    }

    // 전체 퀘스트 조회
    public List<QuestResponse> findAll(Boolean isActive) {
        final List<Quest> all = questRepository.findAllWithMember();
        final List<Quest> filtered = new ArrayList<>();

        for (Quest q : all) {
            if (isActive == null || q.getIsActive().equals(isActive)) {
                filtered.add(q);
            }
        }

        return filtered.stream()
            .map(this::questToResponse)
            .toList();
    }

    // 특정 퀘스트 조회
    public QuestResponse get(final Long id) {
        return questRepository.findWithMemberById(id)
                .map(this::questToResponse)
                .orElseThrow(NotFoundException::new);
    }

    public QuestAchievementResponse create (
        final QuestRequest questRequest,
        final Member member,
        final MultipartFile imageFile
    ) {
        final Quest quest = new Quest();
        requestToEntity(questRequest, quest);
        quest.setMember(member); // 작성자
        quest.setCreatedAt(OffsetDateTime.now(ZoneId.of("Asia/Seoul")));

        //이미지 업로드
        if(imageFile != null && !imageFile.isEmpty()){
            String imageUrl = supabaseStorageService.upload(imageFile);
            quest.setQuestImage(imageUrl);
        }

        questRepository.save(quest);

        // 퀘스트 첫 생성 업적
        Long memberId = member.getId();
        Long questCount = questRepository.countByMemberId(memberId);
        final Long ACHIEVEMENT_ID = 101L;

        if (questCount == 1) {
            boolean alreadyHas = memberAchievementRepository.existsByMemberIdAndAchievementId(memberId, ACHIEVEMENT_ID);
            if (!alreadyHas) {
                Member user = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 회원이 없습니다."));
                Achievement achievement = achievementRepository.findById(ACHIEVEMENT_ID)
                    .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 업적이 없습니다."));

                memberAchievementRepository.save(
                    MemberAchievement.builder()
                        .member(member)
                        .achievement(achievement)
                        .achievedAt(OffsetDateTime.now())
                        .build()
                );
                return new QuestAchievementResponse(quest.getId(), true, new AchievementDTO(achievement));
            }
        }

        return new QuestAchievementResponse(quest.getId(), false, null);
    }

    //퀘스트 수정
    public void update(
        final Long id,
        final QuestRequest questRequest,
        final String currentMemberEmail,
        final MultipartFile imageFile
    ) {

        final Quest quest = questRepository.findWithMemberById(id)
                .orElseThrow(NotFoundException::new);

        if (!quest.getMember().getEmail().equals(currentMemberEmail)) {
            throw new RuntimeException("작성자만 퀘스트를 수정할 수 있습니다.");
        }

        requestToEntity(questRequest, quest);

        //이미지 업로드
        if(imageFile != null && !imageFile.isEmpty()){
            String imageUrl = supabaseStorageService.upload(imageFile);
            quest.setQuestImage(imageUrl);
        }

        quest.setUpdatedAt(OffsetDateTime.now(ZoneId.of("Asia/Seoul")));
        questRepository.save(quest);
    }

    public List<QuestResponse> findByWriter(Member member) {
        List<Quest> quests = questRepository.findByMember(member);
        return quests.stream()
            .map(QuestResponse::of)
            .toList();
    }

//  if문을 써서 작성자랑 관리자가 삭제할 수 있게 hasroll() -> 바이크
    public void delete(final Long id, final Member currentMember) {
        final Quest quest;

        if ("ROLE_ADMIN".equals(currentMember.getRole())) {
            quest = questRepository.findIncludingDeletedById(id)
                .orElseThrow(NotFoundException::new);
            questRepository.delete(quest); // 하드 삭제
            return;
        }

        // 일반 작성자는 soft delete만 가능
        quest = questRepository.findWithMemberById(id)
            .orElseThrow(NotFoundException::new);

        if (!quest.getMember().getId().equals(currentMember.getId())) {
            throw new RuntimeException("작성자만 퀘스트를 삭제할 수 있습니다.");
        }


        quest.setDeletedAt(OffsetDateTime.now(ZoneId.of("Asia/Seoul"))); // DeletedAt의 값이 들어있는 것을 통해 판단
        questRepository.save(quest);
    }

    @Transactional
    public QuestResponse getWithViews(final Long id) {
        Quest quest = questRepository.findWithMemberById(id)
            .orElseThrow(NotFoundException::new);
        quest.setViewCount(quest.getViewCount() + 1);
        questRepository.save(quest);

        return questToResponse(quest);
    }

    private QuestResponse questToResponse(final Quest quest) {
        QuestResponse questResponse = new QuestResponse();
        questResponse.setId(quest.getId());
        questResponse.setTitle(quest.getTitle());
        questResponse.setQuestImage(quest.getQuestImage());
        questResponse.setDescription(quest.getDescription());
        questResponse.setHint(quest.getHint());
        questResponse.setDeadline(quest.getDeadline());
        questResponse.setIsActive(quest.getIsActive());
        questResponse.setCreatedAt(quest.getCreatedAt());
        questResponse.setCompletedAt(quest.getCompletedAt());
        questResponse.setUpdatedAt(quest.getUpdatedAt());
        questResponse.setDeletedAt(quest.getDeletedAt());
        questResponse.setMember(MemberSimpleDTO.from(quest.getMember()));
        questResponse.setViewCount(quest.getViewCount());
        return questResponse;
    }

    private Quest requestToEntity(final QuestRequest questRequest, final Quest quest) {
        quest.setTitle(questRequest.getTitle());
        quest.setDescription(questRequest.getDescription());
        quest.setHint(questRequest.getHint());
        quest.setDeadline(questRequest.getDeadline());
        quest.setIsActive(questRequest.getIsActive() != null ? questRequest.getIsActive() : true);
        return quest;
    }
}
