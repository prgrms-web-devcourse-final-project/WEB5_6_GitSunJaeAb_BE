package com.gitsunjaeab.mapick.domain.quest;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberQuestEvidenceRepository extends JpaRepository<MemberQuestEvidence, Long> {

    MemberQuestEvidence findFirstByMemberQuest(MemberQuest memberQuest);

}
