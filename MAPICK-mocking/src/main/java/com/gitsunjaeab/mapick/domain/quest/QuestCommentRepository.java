package com.gitsunjaeab.mapick.domain.quest;

import com.gitsunjaeab.mapick.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface QuestCommentRepository extends JpaRepository<QuestComment, Long> {

    // 특정 퀘스트의 모든 댓글 조회
    List<QuestComment> findByQuestOrderByCreatedAtDesc(Quest quest);

    // 특정 멤버가 작성한 모든 댓글 조회
    List<QuestComment> findByMemberOrderByCreatedAtDesc(Member member);

    // 특정 퀘스트의 첫 번째 댓글 조회 (삭제 시 참조 체크용)
    QuestComment findFirstByQuest(Quest quest);

    // 특정 멤버의 첫 번째 댓글 조회 (삭제 시 참조 체크용)
    QuestComment findFirstByMember(Member member);

    // 특정 퀘스트의 댓글 개수 조회
    Long countByQuest(Quest quest);

} 