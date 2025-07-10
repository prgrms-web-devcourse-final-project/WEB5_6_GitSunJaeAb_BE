package com.gitsunjaeab.mapick.hashtag;

import com.gitsunjaeab.mapick.map_hashtag_relation.entity.MapHashtagRelation;
import com.gitsunjaeab.mapick.map_hashtag_relation.MapHashtagRelationRepository;
import com.gitsunjaeab.mapick.util.NotFoundException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final MapHashtagRelationRepository mapHashtagRelationRepository;

    public HashtagService(final HashtagRepository hashtagRepository,
            final MapHashtagRelationRepository mapHashtagRelationRepository) {
        this.hashtagRepository = hashtagRepository;
        this.mapHashtagRelationRepository = mapHashtagRelationRepository;
    }

    public List<HashtagDTO> findAll() {
        final List<Hashtag> hashtags = hashtagRepository.findAll(Sort.by("id"));
        return hashtags.stream()
                .map(hashtag -> mapToDTO(hashtag, new HashtagDTO()))
                .toList();
    }

    public HashtagDTO get(final Long id) {
        return hashtagRepository.findById(id)
                .map(hashtag -> mapToDTO(hashtag, new HashtagDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final HashtagDTO hashtagDTO) {
        final Hashtag hashtag = new Hashtag();
        mapToEntity(hashtagDTO, hashtag);
        return hashtagRepository.save(hashtag).getId();
    }

    public void update(final Long id, final HashtagDTO hashtagDTO) {
        final Hashtag hashtag = hashtagRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(hashtagDTO, hashtag);
        hashtagRepository.save(hashtag);
    }

    public void delete(final Long id) {
        hashtagRepository.deleteById(id);
    }

    private HashtagDTO mapToDTO(final Hashtag hashtag, final HashtagDTO hashtagDTO) {
        hashtagDTO.setId(hashtag.getId());
        hashtagDTO.setName(hashtag.getName());
        hashtagDTO.setCreatedAt(hashtag.getCreatedAt());
        return hashtagDTO;
    }

    private Hashtag mapToEntity(final HashtagDTO hashtagDTO, final Hashtag hashtag) {
        hashtag.setName(hashtagDTO.getName());
        hashtag.setCreatedAt(hashtagDTO.getCreatedAt());
        return hashtag;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Hashtag hashtag = hashtagRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final MapHashtagRelation hashtagMapHashtagRelation = mapHashtagRelationRepository.findFirstByHashtag(hashtag);
        if (hashtagMapHashtagRelation != null) {
            referencedWarning.setKey("hashtag.mapHashtagRelation.hashtag.referenced");
            referencedWarning.addParam(hashtagMapHashtagRelation.getId());
            return referencedWarning;
        }
        return null;
    }

}
