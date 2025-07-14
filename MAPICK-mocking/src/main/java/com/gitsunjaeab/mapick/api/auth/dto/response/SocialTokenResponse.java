package com.gitsunjaeab.mapick.api.auth.dto.response;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.domain.auth.TokenDTO;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
// 클라이언트 응답용 DTO
public class SocialTokenResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private TokenResponse token;

    public static SocialTokenResponse of(TokenResponse tokenDTO) {
        return new SocialTokenResponse(
            ResponseCode.SOCIAL_SIGNIN_SUCCESS.getCode(),
            ResponseCode.SOCIAL_SIGNIN_SUCCESS.getMessage(),
            LocalDateTime.now(),
            tokenDTO
        );
    }
}
