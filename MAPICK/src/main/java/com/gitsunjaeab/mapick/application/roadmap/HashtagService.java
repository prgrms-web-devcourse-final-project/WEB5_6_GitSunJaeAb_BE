package com.gitsunjaeab.mapick.application.roadmap;


import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagDTO;
import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagListResponse;
import com.gitsunjaeab.mapick.api.roadmap.dto.hashtag.HashtagRequest;
import com.gitsunjaeab.mapick.api.roadmap.dto.layer.LayerListResponse;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Hashtag;
import com.gitsunjaeab.mapick.domain.roadmap.HashtagRepository;
import com.gitsunjaeab.mapick.domain.roadmap.Layer;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapHashtagRelationRepository;
import com.gitsunjaeab.mapick.domain.roadmap.RoadmapHashtagRelation;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class HashtagService {

    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;
    private final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository;

    public HashtagListResponse findAllHashtagsOnRoadmap(Long roadmapId) {
        final List<Long> hashtagsId = roadmapHashtagRelationRepository.findAllByRoadmap_Id(roadmapId);

        if (hashtagsId.isEmpty()) {
            return HashtagListResponse.of(Collections.emptyList());
        }

        List<Hashtag> hashtags = hashtagRepository.findAllById(hashtagsId);
        return HashtagListResponse.of(hashtags);
    }

    public HashtagDTO get(final Long id) {
        return hashtagRepository.findById(id)
                .map(hashtag -> roadmapToDTO(hashtag, new HashtagDTO()))
                .orElseThrow(NotFoundException::new);
    }

//    public Long create(final HashtagRequest hashtagDTO) {
//        final Hashtag hashtag = new Hashtag();
//        roadmapToEntity(hashtagDTO, hashtag);
//        return hashtagRepository.save(hashtag).getId();
//    }

    public void update(final Long id, final HashtagDTO hashtagDTO) {
        final Hashtag hashtag = hashtagRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roadmapToEntity(hashtagDTO, hashtag);
        hashtagRepository.save(hashtag);
    }

    public void delete(final Long id) {
        hashtagRepository.deleteById(id);
    }

    private HashtagDTO roadmapToDTO(final Hashtag hashtag, final HashtagDTO hashtagDTO) {
        hashtagDTO.setId(hashtag.getId());
        hashtagDTO.setName(hashtag.getName());
        return hashtagDTO;
    }

    private Hashtag roadmapToEntity(final HashtagDTO hashtagDTO, final Hashtag hashtag) {
        hashtag.setName(hashtagDTO.getName());
        return hashtag;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Hashtag hashtag = hashtagRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final RoadmapHashtagRelation hashtagMapHashtagRelation = roadmapHashtagRelationRepository.findFirstByHashtag(hashtag);
        if (hashtagMapHashtagRelation != null) {
            referencedWarning.setKey("hashtag.mapHashtagRelation.hashtag.referenced");
            referencedWarning.addParam(hashtagMapHashtagRelation.getId());
            return referencedWarning;
        }
        return null;
    }

    @Transactional
    public List<Hashtag> findOrCreateHashtags(List<HashtagRequest> hashtagDto) {
        if (hashtagDto == null || hashtagDto.isEmpty()) {
            return List.of();
        }

        List<Hashtag> result = new ArrayList<>();

        for (HashtagRequest dto : hashtagDto) {
            String hashTagName = dto.getName();
            if (hashTagName == null || hashTagName.isBlank()) {
                continue;
            }
            Hashtag tag = hashtagRepository.findByName(hashTagName.trim())
                    .orElseGet(() -> hashtagRepository.save(new Hashtag(hashTagName.trim())));

            result.add(tag);
        }

        return result;
    }
}
