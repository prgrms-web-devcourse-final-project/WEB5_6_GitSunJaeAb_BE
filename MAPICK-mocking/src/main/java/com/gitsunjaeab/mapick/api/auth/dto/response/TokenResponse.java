package com.gitsunjaeab.mapick.api.auth.dto.response;

import com.gitsunjaeab.mapick.infra.auth.token.code.GrantType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private GrantType grantType;
    private Long expiresIn;
}
