package com.gitsunjaeab.mapick.api.auth.dto.response;

import com.gitsunjaeab.mapick.api.auth.dto.internal.TokenDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
// 클라이언트 응답용 DTO
public class PasswordChangeResponse implements BaseApiResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private TokenDTO tokenDTO;

    public static PasswordChangeResponse of(TokenDTO tokenDTO) {
        return new PasswordChangeResponse(
            ResponseCode.CHANGE_PASSWORD_SUCCESS.getCode(),
            ResponseCode.CHANGE_PASSWORD_SUCCESS.getMessage(),
            OffsetDateTime.now(),
            tokenDTO
        );
    }
}
