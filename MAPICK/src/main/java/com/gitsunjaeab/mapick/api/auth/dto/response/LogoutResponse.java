package com.gitsunjaeab.mapick.api.auth.dto.response;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
// 클라이언트 응답용 DTO
public class LogoutResponse implements BaseApiResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;

    public static LogoutResponse logout() {
        return new LogoutResponse(
                ResponseCode.LOGOUT_SUCCESS.getCode(),
                ResponseCode.LOGOUT_SUCCESS.getMessage(),
                LocalDateTime.now()
        );
    }


}
