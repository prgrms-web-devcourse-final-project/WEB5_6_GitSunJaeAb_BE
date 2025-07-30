package com.gitsunjaeab.mapick.application.domain.auth;

import com.gitsunjaeab.mapick.application.domain.member.Member;
import jakarta.persistence.*;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String jti;

    private String refreshToken;

    public RefreshToken(Member member, String jti) {
        this.member = member;
        this.jti = jti;
        this.refreshToken = UUID.randomUUID().toString();
    }
}