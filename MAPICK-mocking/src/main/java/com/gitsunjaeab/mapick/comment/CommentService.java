package com.gitsunjaeab.mapick.comment;

import com.gitsunjaeab.mapick.map.Map;
import com.gitsunjaeab.mapick.map.MapRepository;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MapRepository mapRepository;
    private final MemberRepository memberRepository;

    public CommentService(final CommentRepository commentRepository,
            final MapRepository mapRepository, final MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.mapRepository = mapRepository;
        this.memberRepository = memberRepository;
    }

    public List<CommentDTO> findAll() {
        final List<Comment> comments = commentRepository.findAll(Sort.by("id"));
        return comments.stream()
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .toList();
    }

    public CommentDTO get(final Long id) {
        return commentRepository.findById(id)
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CommentDTO commentDTO) {
        final Comment comment = new Comment();
        mapToEntity(commentDTO, comment);
        return commentRepository.save(comment).getId();
    }

    public void update(final Long id, final CommentDTO commentDTO) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(commentDTO, comment);
        commentRepository.save(comment);
    }

    public void delete(final Long id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO mapToDTO(final Comment comment, final CommentDTO commentDTO) {
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());
        commentDTO.setMap(comment.getMap() == null ? null : comment.getMap().getId());
        commentDTO.setMember(comment.getMember() == null ? null : comment.getMember().getId());
        return commentDTO;
    }

    private Comment mapToEntity(final CommentDTO commentDTO, final Comment comment) {
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setUpdatedAt(commentDTO.getUpdatedAt());
        final Map map = commentDTO.getMap() == null ? null : mapRepository.findById(commentDTO.getMap())
                .orElseThrow(() -> new NotFoundException("map not found"));
        comment.setMap(map);
        final Member member = commentDTO.getMember() == null ? null : memberRepository.findById(commentDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        comment.setMember(member);
        return comment;
    }

}
