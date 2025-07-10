package com.gitsunjaeab.mapick.member_quest_evidence;

import com.gitsunjaeab.mapick.member_quest.MemberQuest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberQuestEvidenceRepository extends JpaRepository<MemberQuestEvidence, Long> {

    MemberQuestEvidence findFirstByMemberQuest(MemberQuest memberQuest);

}
