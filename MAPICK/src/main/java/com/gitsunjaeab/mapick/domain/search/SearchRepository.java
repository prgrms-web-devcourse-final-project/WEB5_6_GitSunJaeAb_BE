package com.gitsunjaeab.mapick.domain.search;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SearchRepository extends JpaRepository<Search, Long> {

    List<Search> findAllByMemberIdAndDeletedAtIsNotNull(Long memberId);
}
