package com.gitsunjaeab.mapick.application.api.auth.dto.response;

import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
// 클라이언트 응답용 DTO
public class LogoutResponse implements BaseApiResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;

    public static LogoutResponse logout() {
        return new LogoutResponse(
                ResponseCode.LOGOUT_SUCCESS.getCode(),
                ResponseCode.LOGOUT_SUCCESS.getMessage(),
                OffsetDateTime.now()
        );
    }


}
