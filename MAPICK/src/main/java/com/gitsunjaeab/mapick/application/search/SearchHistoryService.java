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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchHistoryService {

    private final SearchRepository searchRepository;
    private final MemberRepository memberRepository;

    // 최근 검색어 목록 조회
    // complete
    public List<SearchHistoryDTO> getSearchHistories(Long memberId) {

        final List<Search> searches = searchRepository.findAllByMemberIdAndDeletedAtIsNull(memberId);

        List<SearchHistoryDTO> SearchHistoryDTOs = searches.stream()
                .map(SearchHistoryDTO::of)
                .toList();

        return SearchHistoryDTOs;
    }

    // 최근 검색어 저장
    // complete
    @Transactional
    public void saveSearchHistory(Long memberId,SearchRequest searchRequest) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        Optional<Search> optionalSearch = searchRepository.findByMemberIdAndKeywordIs(memberId,searchRequest.getKeyword());

        if(optionalSearch.isEmpty()){ // 결과가 없는 경우

            Search search = Search.builder()
                    .keyword(searchRequest.getKeyword())
                    .member(member)
                    .build();

            try {
                searchRepository.save(search);
            } catch (DataIntegrityViolationException e) {
                throw new CommonException(ResponseCode.SAVE_FAILED);
            }

        } else{ // 결과가 있는 경우

            Search search = optionalSearch.get();

            if(search.getDeletedAt() != null){ // 지워졌던 결과 라면
                search.setDeletedAt(null);
                search.setUpdatedAt(OffsetDateTime.now());
            }
            else{ // 존재하는 결과라면

                search.setUpdatedAt(OffsetDateTime.now());
            }
        }
    }


    // 최근 검색어 삭제
    @Transactional
    public void deleteSearchHistory(Long memberId,String keyword) {

        Search search = searchRepository.findByMemberIdAndKeywordIs(memberId,keyword)
                .orElseThrow(() -> new CommonException(ResponseCode.SEARCH_NOT_FOUND));

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
