package com.gitsunjaeab.mapick.application.domain.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "access_token_blacklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenBlacklist {

    @Id
    private String token;

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public AccessTokenBlacklist(String token) {
        this.token = token;
        this.createdAt = OffsetDateTime.now();
    }
}
