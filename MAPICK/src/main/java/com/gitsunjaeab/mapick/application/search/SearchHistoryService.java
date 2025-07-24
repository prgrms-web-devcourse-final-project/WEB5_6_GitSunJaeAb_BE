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

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchHistoryService {

    private final SearchRepository searchRepository;
    private final MemberRepository memberRepository;

    // 최근 검색어 목록 조회
    public List<SearchHistoryDTO> getSearchHistories(Long memberId) {

        final List<Search> searches = searchRepository.findAllByMemberIdAndDeletedAtIsNull(memberId);

        log.info("searches: {}", searches);

        List<SearchHistoryDTO> SearchHistoryDTOs = searches.stream()
                .map(SearchHistoryDTO::of)
                .toList();

        return SearchHistoryDTOs;
    }

    // todo 기존 이미 같은 검색어가 있으면 저장 하지 않는 로직 처리
    // todo 삭제 처리 되어 있는 놈이면 삭제 null 로 만들고 수정 일자 최신으로 처리
    // 최근 검색어 저장
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


    // 최근 검색어 삭제
    @Transactional
    public void deleteSearchHistory(Long memberId,String keyword) {

        Search search = searchRepository.findByMemberIdAndKeywordIs(memberId,keyword);

                search.setDeletedAt(OffsetDateTime.now());


    }

    // 최근 검색어 목록 삭제
    @Transactional
    public void deleteSearchHistoryList(Long memberId) {

        List<Search> searches = searchRepository.findAllByMemberIdAndDeletedAtIsNull(memberId);

        for (Search search : searches) {
            search.setDeletedAt(OffsetDateTime.now());
        }


    }
}
