package com.gitsunjaeab.mapick.domain.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); // 이메일 사용자 조회

    Optional<Member> findByEmailAndDeletedAtIsNull(String email);
    // 탈퇴 하지 않은 사용자 중 이메일 사용자 조회
    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);
}