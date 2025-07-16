package com.gitsunjaeab.mapick.application.comment;

import com.gitsunjaeab.mapick.api.comment.dto.CommentDTO;
import com.gitsunjaeab.mapick.api.comment.dto.CommentListResponse;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.comment.CommentRepository;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
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

    public CommentDTO get(final Long id) {
        return commentRepository.findById(id)
                .map(comment -> roadmapToDTO(comment, new CommentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CommentDTO commentDTO) {
        final Comment comment = new Comment();
        roadmapToEntity(commentDTO, comment);
        return commentRepository.save(comment).getId();
    }

    public void update(final Long id, final CommentDTO commentDTO) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(commentDTO, comment);
        commentRepository.save(comment);
    }

    public void delete(final Long id) {
        commentRepository.deleteById(id);
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

    private Comment roadmapToEntity(final CommentDTO commentDTO, final Comment comment) {
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setUpdatedAt(commentDTO.getUpdatedAt());
        final Roadmap roadmap = commentDTO.getRoadmap() == null ? null : roadmapRepository.findById(commentDTO.getRoadmap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        comment.setRoadmap(roadmap);
        final Member member = commentDTO.getMember() == null ? null : memberRepository.findById(commentDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        comment.setMember(member);
        return comment;
    }
}
