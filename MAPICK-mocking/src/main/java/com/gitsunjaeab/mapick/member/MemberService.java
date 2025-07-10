package com.gitsunjaeab.mapick.member;

import com.gitsunjaeab.mapick.bookmark.entity.Bookmark;
import com.gitsunjaeab.mapick.bookmark.BookmarkRepository;
import com.gitsunjaeab.mapick.comment.entity.Comment;
import com.gitsunjaeab.mapick.comment.CommentRepository;
import com.gitsunjaeab.mapick.layer.entity.Layer;
import com.gitsunjaeab.mapick.layer.LayerRepository;
import com.gitsunjaeab.mapick.layer_library.entity.LayerLibrary;
import com.gitsunjaeab.mapick.layer_library.LayerLibraryRepository;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.roadmap_editor.entity.RoadmapEditor;
import com.gitsunjaeab.mapick.roadmap_editor.RoadmapEditorRepository;
import com.gitsunjaeab.mapick.marker.entity.Marker;
import com.gitsunjaeab.mapick.marker.MarkerRepository;
import com.gitsunjaeab.mapick.member.dto.MemberDTO;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member_interest.entity.MemberInterest;
import com.gitsunjaeab.mapick.member_interest.MemberInterestRepository;
import com.gitsunjaeab.mapick.member_quest.entity.MemberQuest;
import com.gitsunjaeab.mapick.member_quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.quest.entity.Quest;
import com.gitsunjaeab.mapick.quest.QuestRepository;
import com.gitsunjaeab.mapick.quest_rank.entity.QuestRank;
import com.gitsunjaeab.mapick.quest_rank.QuestRankRepository;
import com.gitsunjaeab.mapick.report.entity.Report;
import com.gitsunjaeab.mapick.report.ReportRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
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

    public List<MemberDTO> findAll() {
        final List<Member> members = memberRepository.findAll(Sort.by("id"));
        return members.stream()
                .map(member -> mapToDTO(member, new MemberDTO()))
                .toList();
    }

    public MemberDTO get(final Long id) {
        return memberRepository.findById(id)
                .map(member -> mapToDTO(member, new MemberDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberDTO memberDTO) {
        final Member member = new Member();
        mapToEntity(memberDTO, member);
        return memberRepository.save(member).getId();
    }

    public void update(final Long id, final MemberDTO memberDTO) {
        final Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberDTO, member);
        memberRepository.save(member);
    }

    public void delete(final Long id) {
        memberRepository.deleteById(id);
    }

    private MemberDTO mapToDTO(final Member member, final MemberDTO memberDTO) {
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

    private Member mapToEntity(final MemberDTO memberDTO, final Member member) {
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

}
