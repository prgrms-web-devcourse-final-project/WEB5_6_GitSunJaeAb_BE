package com.gitsunjaeab.mapick.common.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse implements BaseApiResponse {
    private String code;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;

    // 데이터 없이 상태코드와 메시지만 반환
    public static ApiResponse of(ResponseCode responseCode, String message) {
        return new ApiResponse(
            responseCode.getCode(),
            message,
            LocalDateTime.now()
        );
    }

    public static ApiResponse of(ResponseCode responseCode) {
        return new ApiResponse(
            responseCode.getCode(),
            responseCode.getMessage(),
            LocalDateTime.now()
        );
    }
}

