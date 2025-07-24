package com.gitsunjaeab.mapick.application.search;

import com.gitsunjaeab.mapick.api.search.dto.SearchHistoryDTO;
import com.gitsunjaeab.mapick.api.search.dto.request.SearchRequest;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.member.Member;
import com.gitsunjaeab.mapick.domain.member.MemberRepository;
import com.gitsunjaeab.mapick.domain.search.Search;
import com.gitsunjaeab.mapick.domain.search.SearchRepository;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchHistoryService {

    private final SearchRepository searchRepository;
    private final MemberRepository memberRepository;

    // 최근 검색어 목록 조회
    public List<SearchHistoryDTO> getSearchHistories(Long memberId) {

        final List<Search> searches = searchRepository.findAllByMemberIdAndDeletedAtIsNull(memberId); // todo 삭제 된건 안나오게 수정 필요

        log.info("searches: {}", searches);

        List<SearchHistoryDTO> SearchHistoryDTOs = searches.stream()
                .map(SearchHistoryDTO::of)
                .toList();

        return SearchHistoryDTOs;
    }

    @Transactional
    public void saveSearchHistory(Long memberId,SearchRequest searchRequest) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        Search search = Search.builder()
                .keyword(searchRequest.getKeyword())
                .member(member)
                .build();

        searchRepository.save(search);

    }
}
