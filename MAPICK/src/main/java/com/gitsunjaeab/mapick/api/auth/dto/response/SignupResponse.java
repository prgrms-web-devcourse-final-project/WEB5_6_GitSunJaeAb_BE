package com.gitsunjaeab.mapick.api.auth.dto.response;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
// 클라이언트 응답용 DTO
public class SignupResponse implements BaseApiResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;

    public static SignupResponse signup() {
        return new SignupResponse(
                ResponseCode.SIGNUP_SUCCESS.getCode(),
                ResponseCode.SIGNUP_SUCCESS.getMessage(),
                OffsetDateTime.now()
        );
    }


}
