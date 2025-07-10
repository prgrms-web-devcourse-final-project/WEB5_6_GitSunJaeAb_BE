package com.gitsunjaeab.mapick.member_quest;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member_quest.entity.MemberQuest;
import com.gitsunjaeab.mapick.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberQuestRepository extends JpaRepository<MemberQuest, Long> {

    MemberQuest findFirstByMember(Member member);

    MemberQuest findFirstByQuest(Quest quest);

}
