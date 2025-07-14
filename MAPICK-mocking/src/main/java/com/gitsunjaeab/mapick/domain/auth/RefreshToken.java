package com.gitsunjaeab.mapick.domain.auth;

import com.gitsunjaeab.mapick.domain.member.Member;
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
    private String accessTokenId;

    private String token;

    public RefreshToken(Member member, String accessTokenId) {
        this.member = member;
        this.accessTokenId = accessTokenId;
        this.token = UUID.randomUUID().toString();
    }
}