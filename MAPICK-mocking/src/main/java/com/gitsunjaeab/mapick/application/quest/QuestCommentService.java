package com.gitsunjaeab.mapick.application.quest;

import com.gitsunjaeab.mapick.domain.quest.QuestCommentRepository;
import com.gitsunjaeab.mapick.api.quest.dto.QuestCommentRequest;
import com.gitsunjaeab.mapick.api.quest.dto.QuestCommentResponse;
import com.gitsunjaeab.mapick.domain.quest.QuestComment;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.util.NotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class QuestCommentService {

    private final QuestCommentRepository questCommentRepository;
    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;

    public QuestCommentService(final QuestCommentRepository questCommentRepository,
            final QuestRepository questRepository, final MemberRepository memberRepository) {
        this.questCommentRepository = questCommentRepository;
        this.questRepository = questRepository;
        this.memberRepository = memberRepository;
    }

    // 모든 퀘스트 댓글 조회
    @Transactional(readOnly = true)
    public List<QuestCommentResponse> findAll() {
        final List<QuestComment> questComments = questCommentRepository.findAll(Sort.by("createdAt").descending());
        return questComments.stream()
                .map(this::toResponse)
                .toList();
    }

    // 특정 퀘스트의 모든 댓글 조회
    @Transactional(readOnly = true)
    public List<QuestCommentResponse> findByQuestId(final Long questId) {
        final Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new NotFoundException("퀘스트를 찾을 수 없습니다."));
        
        final List<QuestComment> questComments = questCommentRepository.findByQuestOrderByCreatedAtDesc(quest);
        return questComments.stream()
                .map(this::toResponse)
                .toList();
    }

    // 특정 댓글 조회
    @Transactional(readOnly = true)
    public QuestCommentResponse get(final Long id) {
        return questCommentRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

    // 댓글 생성
    public Long create(final QuestCommentRequest questCommentRequest) {
        final QuestComment questComment = new QuestComment();
        requestToEntity(questCommentRequest, questComment);
        questComment.setCreatedAt(OffsetDateTime.now());
        return questCommentRepository.save(questComment).getId();
    }

    // 댓글 수정
    public void update(final Long id, final QuestCommentRequest questCommentRequest) {
        final QuestComment questComment = questCommentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        
        requestToEntity(questCommentRequest, questComment);
        questComment.setUpdatedAt(OffsetDateTime.now());
        questCommentRepository.save(questComment);
    }

    // 댓글 삭제
    public void delete(final Long id) {
        if (!questCommentRepository.existsById(id)) {
            throw new NotFoundException("댓글을 찾을 수 없습니다.");
        }
        questCommentRepository.deleteById(id);
    }

    // QuestComment 엔티티를 QuestCommentResponse로 변환
    private QuestCommentResponse toResponse(final QuestComment questComment) {
        final QuestCommentResponse response = new QuestCommentResponse();
        response.setId(questComment.getId());
        response.setContent(questComment.getContent());
        response.setCreatedAt(questComment.getCreatedAt());
        response.setUpdatedAt(questComment.getUpdatedAt());
        response.setQuest(questComment.getQuest() != null ? questComment.getQuest().getId() : null);
        response.setMember(questComment.getMember() != null ? questComment.getMember().getId() : null);
        response.setMemberName(questComment.getMember() != null ? questComment.getMember().getName() : null);
        return response;
    }

    // QuestCommentRequest를 QuestComment 엔티티로 변환
    private void requestToEntity(final QuestCommentRequest request, final QuestComment questComment) {
        questComment.setContent(request.getContent());
        
        if (request.getQuest() != null) {
            final Quest quest = questRepository.findById(request.getQuest())
                    .orElseThrow(() -> new NotFoundException("퀘스트를 찾을 수 없습니다."));
            questComment.setQuest(quest);
        }
        
        if (request.getMember() != null) {
            final Member member = memberRepository.findById(request.getMember())
                    .orElseThrow(() -> new NotFoundException("멤버를 찾을 수 없습니다."));
            questComment.setMember(member);
        }
    }

} 