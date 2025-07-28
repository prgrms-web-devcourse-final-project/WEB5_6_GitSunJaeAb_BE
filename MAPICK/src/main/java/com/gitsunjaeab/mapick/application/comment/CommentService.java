package com.gitsunjaeab.mapick.application.comment;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import com.gitsunjaeab.mapick.api.comment.dto.CommentAchievementDTO;
import com.gitsunjaeab.mapick.api.comment.dto.CommentDTO;
import com.gitsunjaeab.mapick.api.comment.dto.CommentRequest;
import com.gitsunjaeab.mapick.application.notification.NotificationService;
import com.gitsunjaeab.mapick.common.EntityFinder;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievement;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievementRepository;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.comment.CommentRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthorizedAccessException;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final MemberAchievementRepository memberAchievementRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public CommentAchievementDTO create( @Valid final CommentRequest request, final Long memberId) {
        final Comment comment = new Comment();
        DtoToEntity(request, comment, memberId);

        final Comment savedComment = commentRepository.save(comment);
        notifyRoadmapOrQuestOwner(savedComment, memberId);
        Optional<AchievementDTO> achievementDTO = checkAndGrantCommentAchievement(memberId);

        return new CommentAchievementDTO(
            new CommentDTO(savedComment),
            achievementDTO.isPresent(),
            achievementDTO.orElse(null)
        );
    }

    @Transactional
    public List<CommentDTO> findAllCommentsInRoadmaps(final Long roadmapId) {
        entityFinder.findRoadmapById(roadmapId);
        final List<Comment> comments = commentRepository.findAllByRoadmap_Id(roadmapId);

        return comments.stream()
            .map(CommentDTO::new)
            .toList();
    }

    @Transactional
    public List<CommentDTO> findAllCommentsInQuest(final Long questId) {
        entityFinder.findQuestById(questId);
        final List<Comment> comments = commentRepository.findAllByQuest_Id(questId);

        return comments.stream()
            .map(CommentDTO::new)
            .toList();
    }

    @Transactional
    public CommentDTO getComment(final Long commentId) {
        final Comment comment = entityFinder.findCommentById(commentId);

        return new CommentDTO(comment);
    }

    @Transactional
    public CommentDTO update(final Long commentId, final Long memberId, @Valid final CommentRequest request) {
        final Comment comment = entityFinder.findCommentById(commentId);

        if (!comment.getMember().getId().equals(memberId)) {
            throw new UnauthorizedAccessException("댓글 수정은 해당 댓글의 작성자만 가능합니다.");
        }

        comment.setContent(request.getContent());
        comment.setUpdatedAt(OffsetDateTime.now());
        commentRepository.save(comment);

        return new CommentDTO(comment);
    }

    @Transactional
    public void delete(final Long commentId, final Long memberId) {
        final Comment comment = entityFinder.findCommentById(commentId);

        if (!comment.getMember().getId().equals(memberId)) {
            throw new UnauthorizedAccessException("댓글 삭제는 해당 댓글의 작성자만 가능합니다.");
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public List<Comment> findAllCommentsByMember(final Long memberId) {

        return commentRepository.findAllByMember_Id(memberId);
    }

    private void DtoToEntity(final CommentRequest request, final Comment comment,
        final Long memberId) {
        comment.setContent(request.getContent());
        comment.setCreatedAt(OffsetDateTime.now());
        comment.setUpdatedAt(null);

        // 둘 다 있는 경우
        if (request.getRoadmapId() != null && request.getQuestId() != null) {
            throw new CommonException(ResponseCode.INVALID_INPUT, "로드맵과 퀘스트 중 하나에만 댓글을 달 수 있습니다.");
        }

        // 둘 다 null 인 경우
        if (request.getRoadmapId() == null && request.getQuestId() == null) {
            throw new IllegalArgumentException("로드맵 또는 퀘스트 ID 중 하나는 반드시 있어야 합니다.");
        }

        if (request.getRoadmapId() != null) {
            Roadmap roadmap = entityFinder.findRoadmapById(request.getRoadmapId());
            comment.setRoadmap(roadmap);
            comment.setQuest(null);
        }

        if (request.getQuestId() != null) {
            Quest quest = entityFinder.findQuestById(request.getQuestId());
            comment.setQuest(quest);
            comment.setRoadmap(null);
        }

        // 사용자 처리
        Member member = entityFinder.findMemberById(memberId);
        comment.setMember(member);
    }

    private void notifyRoadmapOrQuestOwner(final Comment comment, final Long commenterId) {
        if (comment.getRoadmap() != null) {
            Member owner = comment.getRoadmap().getMember();
            if (!owner.getId().equals(commenterId)) {
                notificationService.createNotification(
                    owner, NotificationType.COMMENT, comment.getRoadmap(),
                    null, null, null, null, comment, null);
            }
        } else if (comment.getQuest() != null) {
            Member owner = comment.getQuest().getMember();
            if (!owner.getId().equals(commenterId)) {
                notificationService.createNotification(
                    owner, NotificationType.COMMENT, null,
                    null, null, comment.getQuest(), null, comment, null);
            }
        }
    }

    private Optional<AchievementDTO> checkAndGrantCommentAchievement(final Long memberId) {
        final long commentCount = commentRepository.countByMemberId(memberId);
        final long ACHIEVEMENT_ID = 103L;

        if (commentCount == 10 &&
            !memberAchievementRepository.existsByMemberIdAndAchievementId(memberId, ACHIEVEMENT_ID)) {

            Member member = entityFinder.findMemberById(memberId);
            Achievement achievement = entityFinder.findAchievementById(ACHIEVEMENT_ID);

            memberAchievementRepository.save(
                MemberAchievement.builder()
                    .member(member)
                    .achievement(achievement)
                    .achievedAt(OffsetDateTime.now())
                    .build()
            );
            return Optional.of(new AchievementDTO(achievement));
        }

        return Optional.empty();
    }
}
