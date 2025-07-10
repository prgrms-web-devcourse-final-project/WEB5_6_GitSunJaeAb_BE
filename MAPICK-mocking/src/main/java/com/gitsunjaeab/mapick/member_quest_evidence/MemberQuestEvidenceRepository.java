package com.gitsunjaeab.mapick.member_quest_evidence;

import com.gitsunjaeab.mapick.member_quest.entity.MemberQuest;
import com.gitsunjaeab.mapick.member_quest_evidence.entity.MemberQuestEvidence;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberQuestEvidenceRepository extends JpaRepository<MemberQuestEvidence, Long> {

    MemberQuestEvidence findFirstByMemberQuest(MemberQuest memberQuest);

}
