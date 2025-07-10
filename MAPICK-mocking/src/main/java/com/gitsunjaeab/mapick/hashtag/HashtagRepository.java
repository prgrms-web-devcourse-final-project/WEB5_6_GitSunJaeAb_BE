package com.gitsunjaeab.mapick.hashtag;

import com.gitsunjaeab.mapick.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
