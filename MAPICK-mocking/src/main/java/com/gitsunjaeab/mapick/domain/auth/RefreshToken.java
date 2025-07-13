package com.gitsunjaeab.mapick.domain.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String email;
    private String accessTokenId;

    private String token;

    public RefreshToken(String email, String accessTokenId) {
        this.email = email;
        this.accessTokenId = accessTokenId;
        this.token = UUID.randomUUID().toString();
    }
}