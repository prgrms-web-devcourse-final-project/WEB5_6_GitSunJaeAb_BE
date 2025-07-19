package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.api.member.dto.MemberSimpleDTO;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.api.quest.dto.QuestRequest;
import com.gitsunjaeab.mapick.api.quest.dto.QuestResponse;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRank;
import com.gitsunjaeab.mapick.domain.quest.QuestRankRepository;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class QuestService {

    private final QuestRepository questRepository;
//    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final MemberQuestRepository memberQuestRepository;
    private final QuestRankRepository questRankRepository;
    private final MemberRepository memberRepository;

    public QuestService(final QuestRepository questRepository,
            final MemberRepository memberRepository,
            final ReportRepository reportRepository,
            final MemberQuestRepository memberQuestRepository,
            final QuestRankRepository questRankRepository
//        ,MemberRepository memberRepository 임시로 주석처리
     ) {
        this.questRepository = questRepository;
//        this.memberRepository = memberRepository;
        this.reportRepository = reportRepository;
        this.memberQuestRepository = memberQuestRepository;
        this.questRankRepository = questRankRepository;
        this.memberRepository = memberRepository;
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

    public Long create (final QuestRequest questRequest, final Member member) {
        final Quest quest = new Quest();
        requestToEntity(questRequest, quest);
        quest.setMember(member); // 작성자
        quest.setCreatedAt(OffsetDateTime.now());

        return questRepository.save(quest).getId();
    }

    //퀘스트 수정
    public void update(final Long id, final QuestRequest questRequest, final String currentMemberEmail) {
        final Quest quest = questRepository.findWithMemberById(id)
                .orElseThrow(NotFoundException::new);

        if (!quest.getMember().getEmail().equals(currentMemberEmail)) {
            throw new RuntimeException("작성자만 퀘스트를 수정할 수 있습니다.");
        }

        requestToEntity(questRequest, quest);
        quest.setUpdatedAt(OffsetDateTime.now());
        questRepository.save(quest);
    }

    public void delete(final Long id, final String currentMemberEmail) {
        final Quest quest = questRepository.findWithMemberById(id)
                .orElseThrow(NotFoundException::new);

        if (!quest.getMember().getEmail().equals(currentMemberEmail)) {
            throw new RuntimeException("작성자만 퀘스트를 삭제 할 수 있습니다.");
        }

        quest.setDeletedAt(OffsetDateTime.now()); // DeletedAt의 값이 들어있는 것을 통해 판단
        questRepository.save(quest); // 변경 내용 명시적으로 저장
//        questRepository.deleteById(id); //SoftDelete임을 가정하고 일단 주석처리
    }

    private QuestResponse questToResponse(final Quest quest) {
        QuestResponse questResponse = new QuestResponse();
        questResponse.setId(quest.getId());
        questResponse.setTitle(quest.getTitle());
        questResponse.setQuestImage(quest.getQuestImage());
        questResponse.setDescription(quest.getDescription());
        questResponse.setDeadline(quest.getDeadline());
        questResponse.setIsActive(quest.getIsActive());
        questResponse.setCreatedAt(quest.getCreatedAt());
        questResponse.setCompletedAt(quest.getCompletedAt());
        questResponse.setUpdatedAt(quest.getUpdatedAt());
        questResponse.setDeletedAt(quest.getDeletedAt());
        questResponse.setMember(MemberSimpleDTO.from(quest.getMember()));
        return questResponse;
    }

    private Quest requestToEntity(final QuestRequest questRequest, final Quest quest) {
        quest.setTitle(questRequest.getTitle());
        quest.setQuestImage(questRequest.getQuestImage());
        quest.setDescription(questRequest.getDescription());
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
