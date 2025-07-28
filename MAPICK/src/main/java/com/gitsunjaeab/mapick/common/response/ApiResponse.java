package com.gitsunjaeab.mapick.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> implements BaseApiResponse {

    private String code;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private OffsetDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 데이터 없이 상태코드와 메시지만 반환
    public static ApiResponse of(ResponseCode responseCode, String message) {
        return new ApiResponse(
            responseCode.getCode(),
            message,
            OffsetDateTime.now(),
            null
        );
    }

    public static ApiResponse of(ResponseCode responseCode) {
        return new ApiResponse(
            responseCode.getCode(),
            responseCode.getMessage(),
            OffsetDateTime.now(),
            null
        );
    }

    // 임시
    public static <T> ApiResponse<T> of(ResponseCode responseCode, String message, T data) {
        return new ApiResponse<>(
            responseCode.getCode(),
            message,
            OffsetDateTime.now(),
            data
        );
    }
}

