package com.gitsunjaeab.mapick.infra.common;

import com.gitsunjaeab.mapick.application.domain.achievement.Achievement;
import com.gitsunjaeab.mapick.application.domain.achievement.AchievementRepository;
import com.gitsunjaeab.mapick.application.domain.category.Category;
import com.gitsunjaeab.mapick.application.domain.category.CategoryRepository;
import com.gitsunjaeab.mapick.application.domain.comment.Comment;
import com.gitsunjaeab.mapick.application.domain.comment.CommentRepository;
import com.gitsunjaeab.mapick.application.domain.member.Member;
import com.gitsunjaeab.mapick.application.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.application.domain.quest.MemberQuest;
import com.gitsunjaeab.mapick.application.domain.quest.MemberQuestRepository;
import com.gitsunjaeab.mapick.application.domain.quest.Quest;
import com.gitsunjaeab.mapick.application.domain.quest.QuestRank;
import com.gitsunjaeab.mapick.application.domain.quest.QuestRankRepository;
import com.gitsunjaeab.mapick.application.domain.quest.QuestRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.bookmark.Bookmark;
import com.gitsunjaeab.mapick.application.domain.roadmap.bookmark.BookmarkRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.hashtag.Hashtag;
import com.gitsunjaeab.mapick.application.domain.roadmap.hashtag.HashtagRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.Marker;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.MarkerCustomImage;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.MarkerCustomImageRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.marker.MarkerRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.Roadmap;
import com.gitsunjaeab.mapick.application.domain.roadmap.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.Layer;
import com.gitsunjaeab.mapick.application.domain.roadmap.layer.LayerRepository;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
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
    private final BookmarkRepository bookmarkRepository;
    private final MemberQuestRepository memberQuestRepository;
    private final HashtagRepository hashtagRepository;
    private final QuestRankRepository questRankRepository;

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

    public Hashtag findByHashId(Long hashtagId) {
        return hashtagRepository.findById(hashtagId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 해시태그가 없습니다."));
    }

    public Layer findLayerById(Long layerId) {
        return layerRepository.findById(layerId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 레이어가 없습니다."));
    }

    public Layer findLayerByTempId(Long layerTempId) {
        return layerRepository.findByLayerTempId(layerTempId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 레이어가 없습니다."));
    }
    public Quest findQuestById(Long questId) {
        return questRepository.findById(questId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 퀘스트가 없습니다."));
    }

    public Quest findWithMemberById(Long questId) {
        return questRepository.findWithMemberById(questId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 퀘스트가 없습니다."));
    }

    public MemberQuest findWithQuestAndMemberById(Long memberQuestId) {
        return memberQuestRepository
            .findWithQuestAndMemberById(memberQuestId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "참여 내역을 찾을 수 없습니다."));
    }

    public MemberQuest findByMemberQuestId(Long memberQuestId) {
        return memberQuestRepository
            .findById(memberQuestId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "참여 내역을 찾을 수 없습니다."));
    }

    public QuestRank findByQuestRankId(Long questRankId) {
        return questRankRepository
            .findById(questRankId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "참여 내역을 찾을 수 없습니다."));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 댓글이 없습니다."));
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 카테고리가 없습니다."));
    }

    public Bookmark findBookmarkById(Long bookmarkId){
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND, "해당하는 북마크가 없습니다."));
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

