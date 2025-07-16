package com.gitsunjaeab.mapick.application.comment;

import com.gitsunjaeab.mapick.api.comment.dto.CommentDTO;
import com.gitsunjaeab.mapick.api.comment.dto.CommentListResponse;
import com.gitsunjaeab.mapick.api.comment.dto.CommentRequest;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.comment.CommentRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
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

    @Transactional
    public Long create(final CommentRequest request, final Long memberId) {
        final Comment comment = new Comment();
        DtoToEntity(request, comment, memberId);
        return commentRepository.save(comment).getId();
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

//    public CommentDTO get(final Long id) {
//        return commentRepository.findById(id)
//                .map(comment -> roadmapToDTO(comment, new CommentDTO()))
//                .orElseThrow(NotFoundException::new);
//    }


/*    public void update(final Long id, final CommentDTO commentDTO) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        DtoToEntity(commentDTO, comment);
        commentRepository.save(comment);
    }*/

    public void delete(final Long id) {
        commentRepository.deleteById(id);
    }

    private void DtoToEntity(final CommentRequest request, final Comment comment, final Long memberId) {
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


    private CommentDTO roadmapToDTO(final Comment comment, final CommentDTO commentDTO) {
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());
        commentDTO.setRoadmap(comment.getRoadmap() == null ? null : comment.getRoadmap().getId());
        commentDTO.setMember(comment.getMember() == null ? null : comment.getMember().getId());
        return commentDTO;
    }
}
