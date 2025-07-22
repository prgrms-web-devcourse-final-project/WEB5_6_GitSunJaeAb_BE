//package com.gitsunjaeab.mapick.application.quest;
//
//import com.gitsunjaeab.mapick.domain.quest.MemberQuestEvidenceRepository;
//import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestEvidenceRequest;
//import com.gitsunjaeab.mapick.api.quest.dto.MemberQuestEvidenceResponse;
//import com.gitsunjaeab.mapick.domain.quest.MemberQuestEvidence;
//import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
//import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
//import com.gitsunjaeab.mapick.util.NotFoundException;
//import java.util.List;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//@Service
//public class MemberQuestEvidenceService {
//
//    private final MemberQuestEvidenceRepository memberQuestEvidenceRepository;
//    private final MemberQuestRepository memberQuestRepository;
//
//    public MemberQuestEvidenceService(final MemberQuestEvidenceRepository memberQuestEvidenceRepository,
//            final MemberQuestRepository memberQuestRepository) {
//        this.memberQuestEvidenceRepository = memberQuestEvidenceRepository;
//        this.memberQuestRepository = memberQuestRepository;
//    }
//
//    // 전체 증빙 목록 조회
//    public List<MemberQuestEvidenceResponse> findAll() {
//        final List<MemberQuestEvidence> evidences = memberQuestEvidenceRepository.findAll(Sort.by("id"));
//        return evidences.stream()
//                .map(this::toResponse)
//                .toList();
//    }
//
//    // 특정 참여자의 증빙 목록 조회
//    public List<MemberQuestEvidenceResponse> findByMemberQuestId(final Long memberQuestId) {
//        final List<MemberQuestEvidence> evidences = memberQuestEvidenceRepository.findByMemberQuestId(memberQuestId);
//        return evidences.stream()
//                .map(this::toResponse)
//                .toList();
//    }
//
//    // 단일 증빙 조회
//    public MemberQuestEvidenceResponse get(final Long id) {
//        return memberQuestEvidenceRepository.findById(id)
//                .map(this::toResponse)
//                .orElseThrow(NotFoundException::new);
//    }
//
//    // 증빙 제출
//    public Long create(final MemberQuestEvidenceRequest request) {
//        final MemberQuestEvidence evidence = new MemberQuestEvidence();
//        requestToEntity(request, evidence);
//
//        // 증빙 제출 시 MemberQuest의 답변도 함께 업데이트
//        final MemberQuest memberQuest = memberQuestRepository.findById(request.getMemberQuest())
//                .orElseThrow(() -> new NotFoundException("memberQuest not found"));
//        memberQuest.setAnswer(request.getAnswer());
//        memberQuestRepository.save(memberQuest);
//
//        return memberQuestEvidenceRepository.save(evidence).getId();
//    }
//
//    // 증빙 수정
//    public void update(final Long id, final MemberQuestEvidenceRequest request) {
//        final MemberQuestEvidence evidence = memberQuestEvidenceRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        requestToEntity(request, evidence);
//        memberQuestEvidenceRepository.save(evidence);
//    }
//
//    // 증빙 삭제
//    public void delete(final Long id) {
//        memberQuestEvidenceRepository.deleteById(id);
//    }
//
//    // Entity → Response 변환
//    private MemberQuestEvidenceResponse toResponse(final MemberQuestEvidence evidence) {
//        return MemberQuestEvidenceResponse.ofCreate(evidence);
//    }
//
//    // Request → Entity 변환
//    private void requestToEntity(final MemberQuestEvidenceRequest request, final MemberQuestEvidence evidence) {
//        // 제목과 설명을 구분하여 description에 저장 (제목: 내용 형태로)
//        String fullDescription = request.getTitle() + ": " + (request.getDescription() != null ? request.getDescription() : "");
//        evidence.setDescription(fullDescription);
//        evidence.setImageUrl(request.getEvidenceImage());
//
//        // 연관 엔티티 설정
//        final MemberQuest memberQuest = memberQuestRepository.findById(request.getMemberQuest())
//                .orElseThrow(() -> new NotFoundException("memberQuest not found"));
//        evidence.setMemberQuest(memberQuest);
//    }
//}
