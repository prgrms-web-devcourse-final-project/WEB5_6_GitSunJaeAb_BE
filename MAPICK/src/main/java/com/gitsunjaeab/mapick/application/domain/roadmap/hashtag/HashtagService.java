package com.gitsunjaeab.mapick.application.domain.roadmap.hashtag;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag.HashtagDTO;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.hashtag.HashtagRequest;
import com.gitsunjaeab.mapick.infra.common.EntityFinder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final EntityFinder entityFinder;

    // 해쉬태그 조회
    public HashtagDTO get(final Long hashtagId) {
        Hashtag hashtag = entityFinder.findByHashId(hashtagId);
        return entityToDTO(hashtag, new HashtagDTO());
    }

    // 해쉬태그 수정
    public void update(final Long hashtagId, final HashtagDTO hashtagDTO) {
        final Hashtag hashtag = entityFinder.findByHashId(hashtagId);

        dtoToEntity(hashtagDTO, hashtag);

        hashtagRepository.save(hashtag);
    }

    // 해쉬태그 삭제
    public void delete(final Long hashtagId) {
        hashtagRepository.deleteById(hashtagId);
    }

    // entity -> dto
    private HashtagDTO entityToDTO(final Hashtag hashtag, final HashtagDTO hashtagDTO) {
        hashtagDTO.setId(hashtag.getId());
        hashtagDTO.setName(hashtag.getName());
        return hashtagDTO;
    }

    // dto -> entity
    private Hashtag dtoToEntity(final HashtagDTO hashtagDTO, final Hashtag hashtag) {
        hashtag.setName(hashtagDTO.getName());
        return hashtag;
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
