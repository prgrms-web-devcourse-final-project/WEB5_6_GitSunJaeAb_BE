package com.gitsunjaeab.mapick.api.member.dto;

import com.gitsunjaeab.mapick.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class SimpleMessageResponse implements BaseApiResponse {

    // 커스텀 응답 필드들
    private String code;
    private String message;
    private LocalDateTime timestamp;

    public SimpleMessageResponse(String code, String message, LocalDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static SimpleMessageResponse of(String message) {
        return new SimpleMessageResponse(
            ResponseCode.OK.getCode(),
            message,
            LocalDateTime.now()
        );
    }
} 