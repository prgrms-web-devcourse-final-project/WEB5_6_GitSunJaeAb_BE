package com.gitsunjaeab.mapick.application.domain.roadmap.hashtag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(@NotNull @Size(max = 255) String name);
}
