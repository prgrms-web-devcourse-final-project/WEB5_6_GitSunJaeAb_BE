package com.gitsunjaeab.mapick.application.member;

import com.gitsunjaeab.mapick.api.member.dto.MemberListResponse;
import com.gitsunjaeab.mapick.domain.roadmap.Bookmark;
import com.gitsunjaeab.mapick.domain.roadmap.BookmarkRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Comment;
import com.gitsunjaeab.mapick.domain.roadmap.CommentRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.LayerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibrary;
import com.gitsunjaeab.mapick.domain.roadmap.LayerLibraryRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapEditor;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapEditorRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.api.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.domain.member.MemberInterest;
import com.gitsunjaeab.mapick.domain.member.MemberInterestRepository;
import com.gitsunjaeab.mapick.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.quest.QuestRank;
import com.gitsunjaeab.mapick.domain.quest.QuestRankRepository;
import com.gitsunjaeab.mapick.domain.report.Report;
import com.gitsunjaeab.mapick.domain.report.ReportRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;
    private final RoadmapEditorRepository roadmapEditorRepository;
    private final LayerRepository layerRepository;
    private final MarkerRepository markerRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final ReportRepository reportRepository;
    private final QuestRepository questRepository;
    private final MemberQuestRepository memberQuestRepository;
    private final QuestRankRepository questRankRepository;
    private final LayerLibraryRepository layerLibraryRepository;

    public MemberService(final MemberRepository memberRepository, final RoadmapRepository roadmapRepository,
            final RoadmapEditorRepository roadmapEditorRepository, final LayerRepository layerRepository,
            final MarkerRepository markerRepository, final CommentRepository commentRepository,
            final BookmarkRepository bookmarkRepository,
            final MemberInterestRepository memberInterestRepository,
            final ReportRepository reportRepository, final QuestRepository questRepository,
            final MemberQuestRepository memberQuestRepository,
            final QuestRankRepository questRankRepository,
            final LayerLibraryRepository layerLibraryRepository) {
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
        this.roadmapEditorRepository = roadmapEditorRepository;
        this.layerRepository = layerRepository;
        this.markerRepository = markerRepository;
        this.commentRepository = commentRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.memberInterestRepository = memberInterestRepository;
        this.reportRepository = reportRepository;
        this.questRepository = questRepository;
        this.memberQuestRepository = memberQuestRepository;
        this.questRankRepository = questRankRepository;
        this.layerLibraryRepository = layerLibraryRepository;
    }

    public MemberListResponse findAll() {
        final List<Member> members = memberRepository.findAll(Sort.by("id"));
        return MemberListResponse.of(members);
    }

    public MemberDTO get(final Long id) {
        return memberRepository.findById(id)
                .map(member -> roadmapToDTO(member, new MemberDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberDTO memberDTO) {
        final Member member = new Member();
        roadmapToEntity(memberDTO, member);
        return memberRepository.save(member).getId();
    }

    public void update(final Long id, final MemberDTO memberDTO) {
        final Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(memberDTO, member);
        memberRepository.save(member);
    }

    public void delete(final Long id) {
        memberRepository.deleteById(id);
    }

    private MemberDTO roadmapToDTO(final Member member, final MemberDTO memberDTO) {
        memberDTO.setId(member.getId());
        memberDTO.setName(member.getName());
        memberDTO.setNickname(member.getNickname());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPassword(member.getPassword());
        memberDTO.setLoginType(member.getLoginType());
        memberDTO.setProvider(member.getProvider());
        memberDTO.setRole(member.getRole());
        memberDTO.setStatus(member.getStatus());
        memberDTO.setProfileImage(member.getProfileImage());
        memberDTO.setLastLogin(member.getLastLogin());
        memberDTO.setCreatedAt(member.getCreatedAt());
        memberDTO.setUpdatedAt(member.getUpdatedAt());
        memberDTO.setDeletedAt(member.getDeletedAt());
        return memberDTO;
    }

    private Member roadmapToEntity(final MemberDTO memberDTO, final Member member) {
        member.setName(memberDTO.getName());
        member.setNickname(memberDTO.getNickname());
        member.setEmail(memberDTO.getEmail());
        member.setPassword(memberDTO.getPassword());
        member.setLoginType(memberDTO.getLoginType());
        member.setProvider(memberDTO.getProvider());
        member.setRole(memberDTO.getRole());
        member.setStatus(memberDTO.getStatus());
        member.setProfileImage(memberDTO.getProfileImage());
        member.setLastLogin(memberDTO.getLastLogin());
        member.setCreatedAt(memberDTO.getCreatedAt());
        member.setUpdatedAt(memberDTO.getUpdatedAt());
        member.setDeletedAt(memberDTO.getDeletedAt());
        return member;
    }

    public boolean emailExists(final String email) {
        return memberRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Roadmap memberMap = roadmapRepository.findFirstByMember(member);
        if (memberMap != null) {
            referencedWarning.setKey("member.map.member.referenced");
            referencedWarning.addParam(memberMap.getId());
            return referencedWarning;
        }
        final RoadmapEditor memberMapEditor = roadmapEditorRepository.findFirstByMember(member);
        if (memberMapEditor != null) {
            referencedWarning.setKey("member.mapEditor.member.referenced");
            referencedWarning.addParam(memberMapEditor.getId());
            return referencedWarning;
        }
        final RoadmapEditor invitedByMapEditor = roadmapEditorRepository.findFirstByInvitedBy(member);
        if (invitedByMapEditor != null) {
            referencedWarning.setKey("member.mapEditor.invitedBy.referenced");
            referencedWarning.addParam(invitedByMapEditor.getId());
            return referencedWarning;
        }
        final Layer memberLayer = layerRepository.findFirstByMember(member);
        if (memberLayer != null) {
            referencedWarning.setKey("member.layer.member.referenced");
            referencedWarning.addParam(memberLayer.getId());
            return referencedWarning;
        }
        final Marker memberMarker = markerRepository.findFirstByMember(member);
        if (memberMarker != null) {
            referencedWarning.setKey("member.marker.member.referenced");
            referencedWarning.addParam(memberMarker.getId());
            return referencedWarning;
        }
        final Comment memberComment = commentRepository.findFirstByMember(member);
        if (memberComment != null) {
            referencedWarning.setKey("member.comment.member.referenced");
            referencedWarning.addParam(memberComment.getId());
            return referencedWarning;
        }
        final Bookmark memberBookmark = bookmarkRepository.findFirstByMember(member);
        if (memberBookmark != null) {
            referencedWarning.setKey("member.bookmark.member.referenced");
            referencedWarning.addParam(memberBookmark.getId());
            return referencedWarning;
        }
        final MemberInterest memberMemberInterest = memberInterestRepository.findFirstByMember(member);
        if (memberMemberInterest != null) {
            referencedWarning.setKey("member.memberInterest.member.referenced");
            referencedWarning.addParam(memberMemberInterest.getId());
            return referencedWarning;
        }
        final Report reporterReport = reportRepository.findFirstByReporter(member);
        if (reporterReport != null) {
            referencedWarning.setKey("member.report.reporter.referenced");
            referencedWarning.addParam(reporterReport.getId());
            return referencedWarning;
        }
        final Report reportedMemberReport = reportRepository.findFirstByReportedMember(member);
        if (reportedMemberReport != null) {
            referencedWarning.setKey("member.report.reportedMember.referenced");
            referencedWarning.addParam(reportedMemberReport.getId());
            return referencedWarning;
        }
        final Quest memberQuest = questRepository.findFirstByMember(member);
        if (memberQuest != null) {
            referencedWarning.setKey("member.quest.member.referenced");
            referencedWarning.addParam(memberQuest.getId());
            return referencedWarning;
        }
        final MemberQuest memberMemberQuest = memberQuestRepository.findFirstByMember(member);
        if (memberMemberQuest != null) {
            referencedWarning.setKey("member.memberQuest.member.referenced");
            referencedWarning.addParam(memberMemberQuest.getId());
            return referencedWarning;
        }
        final QuestRank memberQuestRank = questRankRepository.findFirstByMember(member);
        if (memberQuestRank != null) {
            referencedWarning.setKey("member.questRank.member.referenced");
            referencedWarning.addParam(memberQuestRank.getId());
            return referencedWarning;
        }
        final LayerLibrary memberLayerLibrary = layerLibraryRepository.findFirstByMember(member);
        if (memberLayerLibrary != null) {
            referencedWarning.setKey("member.layerLibrary.member.referenced");
            referencedWarning.addParam(memberLayerLibrary.getId());
            return referencedWarning;
        }
        return null;
    }

    // 마이페이지 - 회원 정보 조회
    public Member getMemberProfile(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));
    }

    // 마이페이지 - 회원 정보 수정
    public Member updateMemberProfile(Long memberId, String nickname, String profileImage, String intro, String phone) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        if (nickname != null && !nickname.trim().isEmpty()) {
            member.setNickname(nickname);
        }
        if (profileImage != null) {
            member.setProfileImage(profileImage);
        }
        if (intro != null) {
            member.setIntro(intro);
        }
        if (phone != null) {
            member.setPhone(phone);
        }

        member.updateTimestamp();
        return memberRepository.save(member);
    }

    // 마이페이지 - 비밀번호 확인
    public boolean verifyPassword(Long memberId, String currentPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));
        
        // 실제 구현에서는 암호화된 비밀번호와 비교해야 함
        return member.getPassword().equals(currentPassword);
    }

    // 마이페이지 - 비밀번호 수정
    public void updatePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        // 실제 구현에서는 비밀번호를 암호화해야 함
        member.setPassword(newPassword);
        member.updateTimestamp();
        memberRepository.save(member);
    }

    // 마이페이지 - 회원 탈퇴
    public void withdrawMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        member.setDeletedAt(OffsetDateTime.now());
        member.setStatus("WITHDRAWN");
        memberRepository.save(member);
    }

    // 마이페이지 - 회원 지도 목록 조회
    public List<Roadmap> getMemberRoadmaps(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        return roadmapRepository.findByMember(member);
    }

    // 마이페이지 - 회원 레이어 목록 조회
    public List<LayerLibrary> getMemberLayers(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        return layerLibraryRepository.findByMember(member);
    }

}
