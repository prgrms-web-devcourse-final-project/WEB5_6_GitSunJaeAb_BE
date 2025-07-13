package com.gitsunjaeab.mapick.application.roadmap;

import com.gitsunjaeab.mapick.api.comment.dto.CommentDTO;
import com.gitsunjaeab.mapick.api.comment.dto.CommentListResponse;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Comment;
import com.gitsunjaeab.mapick.domain.roadmap.CommentRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    public CommentService(final CommentRepository commentRepository,
            final RoadmapRepository roadmapRepository, final MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.roadmapRepository = roadmapRepository;
        this.memberRepository = memberRepository;
    }

    public CommentListResponse findAllCommentsInRoadmaps(Long roadmapId) {
        final List<Comment> comments = commentRepository.findAllByRoadmap_Id(roadmapId);
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
