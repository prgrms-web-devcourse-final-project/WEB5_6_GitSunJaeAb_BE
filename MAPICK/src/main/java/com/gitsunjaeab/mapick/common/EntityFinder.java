package com.gitsunjaeab.mapick.common;

import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.domain.achievement.AchievementRepository;
import com.gitsunjaeab.mapick.domain.category.Category;
import com.gitsunjaeab.mapick.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.domain.comment.Comment;
import com.gitsunjaeab.mapick.domain.comment.CommentRepository;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.quest.Quest;
import com.gitsunjaeab.mapick.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Marker;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerCustomImage;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerCustomImageRepository;
import com.gitsunjaeab.mapick.domain.roadmap.MarkerRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Roadmap;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.layer.LayerRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFinder {

    private final MemberRepository memberRepository;
    private final AchievementRepository achievementRepository;
    private final RoadmapRepository roadmapRepository;
    private final QuestRepository questRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final MarkerCustomImageRepository markerCustomImageRepository;
    private final MarkerRepository markerRepository;
    private final LayerRepository layerRepository;

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 회원이 없습니다."));
    }

    public Achievement findAchievementById(Long achievementId) {
        return achievementRepository.findById(achievementId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 업적이 없습니다."));
    }

    public Roadmap findRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 로드맵이 없습니다."));
    }

    public Layer findLayerById(Long layerId) {
        return layerRepository.findById(layerId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 레이어가 없습니다."));
    }

    public Quest findQuestById(Long questId) {
        return questRepository.findById(questId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 퀘스트가 없습니다."));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 댓글이 없습니다."));
    }

    public Category findByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 카테고리가 없습니다."));
    }

    public Marker findByMarkerId(Long markerId) {
        return  markerRepository.findById(markerId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 마커가 없습니다."));
    }

    public Marker findByMarkerTempId(Long markerTempId) {
        return markerRepository.findByMarkerTempId(markerTempId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 마커가 없습니다."));
    }

    public MarkerCustomImage findByMarkerCustomId(Long customImageId) {
        return markerCustomImageRepository.findById(customImageId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 커스텀 이미지가 없습니다."));
    }
}

