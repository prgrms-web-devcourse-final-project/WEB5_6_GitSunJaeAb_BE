package com.gitsunjaeab.mapick.api.auth.dto.response;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
// 클라이언트 응답용 DTO
public class PasswordChangeResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private TokenResponse token;

    public static PasswordChangeResponse of(TokenResponse tokenDTO) {
        return new PasswordChangeResponse(
            ResponseCode.CHANGE_PASSWORD_SUCCESS.getCode(),
            ResponseCode.CHANGE_PASSWORD_SUCCESS.getMessage(),
            LocalDateTime.now(),
            tokenDTO
        );
    }
}
