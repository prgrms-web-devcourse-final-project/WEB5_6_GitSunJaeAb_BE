package com.gitsunjaeab.mapick.application.domain.search;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SearchRepository extends JpaRepository<Search, Long> {

    List<Search> findAllByMemberIdAndDeletedAtIsNull(Long memberId);

    Optional<Search> findByMemberIdAndKeywordIs(Long memberId, String keyword);

    Search findByMemberIdAndDeletedAtIsNull(Long memberId);
}
