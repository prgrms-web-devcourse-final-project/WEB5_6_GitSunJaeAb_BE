package com.gitsunjaeab.mapick.quest;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestRepository extends JpaRepository<Quest, Long> {

    Quest findFirstByMember(Member member);

}
