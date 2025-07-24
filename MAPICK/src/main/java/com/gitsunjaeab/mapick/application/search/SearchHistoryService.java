package com.gitsunjaeab.mapick.application.search;

import com.gitsunjaeab.mapick.api.search.dto.SearchHistoryDTO;
import com.gitsunjaeab.mapick.domain.search.Search;
import com.gitsunjaeab.mapick.domain.search.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchRepository searchRepository;

    public List<SearchHistoryDTO> getSearchHistories() {

        final List<Search> searches = searchRepository.findAll();

        List<SearchHistoryDTO> SearchHistoryDTOs = searches.stream()
                .map(SearchHistoryDTO::of)
                .toList();

        return SearchHistoryDTOs;

    }
}
