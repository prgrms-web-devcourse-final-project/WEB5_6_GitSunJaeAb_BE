package com.gitsunjaeab.mapick.api.auth.dto.response;

import com.gitsunjaeab.mapick.api.auth.dto.internal.TokenDTO;
import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
// 클라이언트 응답용 DTO
public class SigninResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private TokenDTO token;

    public static SigninResponse social(TokenDTO tokenDTO) {
        return new SigninResponse(
                ResponseCode.SOCIAL_SIGNIN_SUCCESS.getCode(),
                ResponseCode.SOCIAL_SIGNIN_SUCCESS.getMessage(),
                LocalDateTime.now(),
                tokenDTO
        );
    }

    public static SigninResponse local(TokenDTO tokenDTO) {
        return new SigninResponse(
                ResponseCode.SIGNIN_SUCCESS.getCode(),
                ResponseCode.SIGNIN_SUCCESS.getMessage(),
                LocalDateTime.now(),
                tokenDTO
        );
    }
}
