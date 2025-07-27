package com.gitsunjaeab.mapick.application.comment;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import com.gitsunjaeab.mapick.api.comment.dto.CommentAchievementResponse;
import com.gitsunjaeab.mapick.api.comment.dto.CommentDTO;
import com.gitsunjaeab.mapick.api.comment.dto.CommentListResponse;
import com.gitsunjaeab.mapick.api.comment.dto.CommentRequest;
import com.gitsunjaeab.mapick.application.notification.NotificationService;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.domain.achievement.AchievementRepository;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievement;
import com.gitsunjaeab.mapick.domain.achievement.MemberAchievementRepository;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.comment.CommentRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.notification.NotificationType;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.util.NotFoundException;
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
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;
    private final QuestRepository questRepository;
    private final NotificationService notificationService;
    private final MemberAchievementRepository memberAchievementRepository;
    private final AchievementRepository achievementRepository;

    @Transactional
    public CommentAchievementResponse create(final CommentRequest request, final Long memberId) {
        final Comment comment = new Comment();
        DtoToEntity(request, comment, memberId);

        // === 알림 발송 로직 ===
        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        // 알림 전송
        if (request.getRoadmapId() != null) {
            // 로드맵 댓글
            Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId())
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당 지도가 없습니다."));
            Member roadmapOwner = roadmap.getMember();

            // 본인 댓글은 알림 제외
            if (!roadmapOwner.getId().equals(memberId)) {
                notificationService.createNotification(
                    roadmapOwner,              // 알림 수신인
                    NotificationType.COMMENT,  // 알림 타입
                    roadmap,                   // 로드맵
                    null,                      // 레이어
                    null,                      // 레이어 라이브러리
                    null,                      // 퀘스트
                    null,                      // 멤버퀘스트
                    savedComment,              // 댓글
                    null                       // 북마크
                );
            }

        } else if (request.getQuestId() != null) {
            // 퀘스트 댓글
            Quest quest = questRepository.findById(request.getQuestId())
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당 퀘스트가 없습니다."));
            Member questOwner = quest.getMember();

            // 본인 댓글은 알림 제외
            if (!questOwner.getId().equals(memberId)) {
                notificationService.createNotification(
                    questOwner,                // 알림 수신인
                    NotificationType.COMMENT,  // 알림 타입
                    null,                      // 로드맵
                    null,                      // 레이어
                    null,                      // 레이어 라이브러리
                    quest,                     // 퀘스트
                    null,                      // 멤버퀘스트
                    savedComment,              // 댓글
                    null                       // 북마크
                );
            }
        }

        // 댓글 업적
        Long commentCount = commentRepository.countByMemberId(memberId);
        final Long ACHIEVEMENT_ID = 103L;

        if (commentCount == 10) {
            boolean alreadyHas = memberAchievementRepository.existsByMemberIdAndAchievementId(memberId, ACHIEVEMENT_ID);
            if (!alreadyHas) {
                Member member = memberRepository.findById(memberId)
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

                return new CommentAchievementResponse(new CommentDTO(savedComment), true, new AchievementDTO(achievement));
            }
        }

        // 업적 미달성 or 이미 달성
        return new CommentAchievementResponse(new CommentDTO(savedComment), false, null);
    }

    @Transactional
    public CommentListResponse findAllCommentsInRoadmaps(Long roadmapId) {
        roadmapRepository.findById(roadmapId)
            .orElseThrow(() -> new NotFoundException("해당 로드맵이 존재하지 않습니다."));
        final List<Comment> comments = commentRepository.findAllByRoadmap_Id(roadmapId);

        return CommentListResponse.of(comments);
    }

    @Transactional
    public CommentListResponse findAllCommentsInQuest(Long questId) {
        questRepository.findById(questId)
            .orElseThrow(() -> new NotFoundException("해당 퀘스트가 존재하지 않습니다."));
        final List<Comment> comments = commentRepository.findAllByQuest_Id(questId);

        return CommentListResponse.of(comments);
    }

    @Transactional
    public CommentDTO getComment(final Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 댓글이 존재하지 않습니다."));

        return new CommentDTO(comment);
    }

    @Transactional
    public CommentDTO update(Long commentId, @Valid CommentRequest request) {
        final Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글 ID 입니다."));
        comment.setContent(request.getContent());
        comment.setUpdatedAt(OffsetDateTime.now());

        commentRepository.save(comment);

        return new CommentDTO(comment);
    }

    public void delete(final Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public List<Comment> findAllCommentsByMember(Long memberId) {

        return commentRepository.findAllByMember_Id(memberId);
    }

    private void DtoToEntity(final CommentRequest request, final Comment comment,
        final Long memberId) {
        comment.setContent(request.getContent());
        comment.setCreatedAt(OffsetDateTime.now());
        comment.setUpdatedAt(null);

        // 둘 다 있는 경우 예외
        if (request.getRoadmapId() != null && request.getQuestId() != null) {
            throw new IllegalArgumentException("로드맵과 퀘스트 중 하나에만 댓글을 달 수 있습니다.");
        }

        // 둘 다 null 인 경우 예외
        if (request.getRoadmapId() == null && request.getQuestId() == null) {
            throw new IllegalArgumentException("로드맵 또는 퀘스트 ID 중 하나는 반드시 있어야 합니다.");
        }

        // 로드맵 댓글 처리
        if (request.getRoadmapId() != null) {
            Roadmap roadmap = roadmapRepository.findById(request.getRoadmapId())
                .orElseThrow(() -> new NotFoundException("해당하는 지도가 없습니다"));
            comment.setRoadmap(roadmap);
            comment.setQuest(null); // 퀘스트는 명시적으로 null
        }

        // 퀘스트 댓글 처리
        if (request.getQuestId() != null) {
            Quest quest = questRepository.findById(request.getQuestId())
                .orElseThrow(() -> new NotFoundException("해당하는 퀘스트가 없습니다"));
            comment.setQuest(quest);
            comment.setRoadmap(null); // 로드맵은 명시적으로 null
        }

        // 사용자 처리
        Member member = Optional.ofNullable(memberId)
            .flatMap(memberRepository::findById)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));
        comment.setMember(member);
    }
}
