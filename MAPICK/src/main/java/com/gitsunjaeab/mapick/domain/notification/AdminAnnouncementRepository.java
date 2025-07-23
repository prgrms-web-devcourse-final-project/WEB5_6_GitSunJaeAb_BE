package com.gitsunjaeab.mapick.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAnnouncementRepository extends JpaRepository<Announcement, Long> {

    // 공지와 작성자(member)를 fetch join으로 한 번에 조회
    @org.springframework.data.jpa.repository.Query("SELECT a FROM Announcement a JOIN FETCH a.member WHERE a.deletedAt IS NULL")
    java.util.List<Announcement> findAllWithMember();
}
