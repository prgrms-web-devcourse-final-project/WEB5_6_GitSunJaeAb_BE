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
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRank;
import com.gitsunjaeab.mapick.domain.quest.QuestRankRepository;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.storage.SupabaseStorageService;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final ReportRepository reportRepository;
    private final MemberQuestRepository memberQuestRepository;
    private final QuestRankRepository questRankRepository;
    private final MemberRepository memberRepository;
    private final MemberAchievementRepository memberAchievementRepository;
    private final AchievementRepository achievementRepository;
    //Supabase 사용을 위해 추가
    private final SupabaseStorageService supabaseStorageService;

    public QuestService(final QuestRepository questRepository,
            final MemberRepository memberRepository,
            final ReportRepository reportRepository,
            final MemberQuestRepository memberQuestRepository,
            final QuestRankRepository questRankRepository,
            final SupabaseStorageService supabaseStorageService,

        MemberAchievementRepository memberAchievementRepository,
        AchievementRepository achievementRepository) {
        //수정예정
        this.questRepository = questRepository;
        this.reportRepository = reportRepository;
        this.memberQuestRepository = memberQuestRepository;
        this.questRankRepository = questRankRepository;
        this.memberRepository = memberRepository;
        this.memberAchievementRepository = memberAchievementRepository;
        this.achievementRepository = achievementRepository;
        this.supabaseStorageService = supabaseStorageService;
    }
    //전체 퀘스트 조회
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

    //특정 퀘스트 조회
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


    public void delete(final Long id, final String currentMemberEmail) {
        final Quest quest = questRepository.findWithMemberById(id)
                .orElseThrow(NotFoundException::new);

        if (!quest.getMember().getEmail().equals(currentMemberEmail)) {
            throw new RuntimeException("작성자만 퀘스트를 삭제 할 수 있습니다.");
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

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Quest quest = questRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Report questReport = reportRepository.findFirstByQuest(quest);
        if (questReport != null) {
            referencedWarning.setKey("quest.report.quest.referenced");
            referencedWarning.addParam(questReport.getId());
            return referencedWarning;
        }
        final MemberQuest questMemberQuest = memberQuestRepository.findFirstByQuest(quest);
        if (questMemberQuest != null) {
            referencedWarning.setKey("quest.memberQuest.quest.referenced");
            referencedWarning.addParam(questMemberQuest.getId());
            return referencedWarning;
        }
        final QuestRank questQuestRank = questRankRepository.findFirstByQuest(quest);
        if (questQuestRank != null) {
            referencedWarning.setKey("quest.questRank.quest.referenced");
            referencedWarning.addParam(questQuestRank.getId());
            return referencedWarning;
        }
        return null;
    }

}
