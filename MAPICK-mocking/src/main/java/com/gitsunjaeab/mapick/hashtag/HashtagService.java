package com.gitsunjaeab.mapick.hashtag;

import com.gitsunjaeab.mapick.hashtag.dto.HashtagDTO;
import com.gitsunjaeab.mapick.hashtag.entity.Hashtag;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.RoadmapHashtagRelationRepository;
import com.gitsunjaeab.mapick.roadmap_hashtag_relation.entity.RoadmapHashtagRelation;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository;

    public HashtagService(final HashtagRepository hashtagRepository,
            final RoadmapHashtagRelationRepository roadmapHashtagRelationRepository) {
        this.hashtagRepository = hashtagRepository;
        this.roadmapHashtagRelationRepository = roadmapHashtagRelationRepository;
    }

    public List<HashtagDTO> findAll() {
        final List<Hashtag> hashtags = hashtagRepository.findAll(Sort.by("id"));
        return hashtags.stream()
                .map(hashtag -> roadmapToDTO(hashtag, new HashtagDTO()))
                .toList();
    }

    public HashtagDTO get(final Long id) {
        return hashtagRepository.findById(id)
                .map(hashtag -> roadmapToDTO(hashtag, new HashtagDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final HashtagDTO hashtagDTO) {
        final Hashtag hashtag = new Hashtag();
        roadmapToEntity(hashtagDTO, hashtag);
        return hashtagRepository.save(hashtag).getId();
    }

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
        hashtagDTO.setCreatedAt(hashtag.getCreatedAt());
        return hashtagDTO;
    }

    private Hashtag roadmapToEntity(final HashtagDTO hashtagDTO, final Hashtag hashtag) {
        hashtag.setName(hashtagDTO.getName());
        hashtag.setCreatedAt(hashtagDTO.getCreatedAt());
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

}
