package com.gitsunjaeab.mapick.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // 성공
    OK("2000", "성공", HttpStatus.OK),

    // 클라이언트 에러
    BAD_REQUEST("4000", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("4010", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("4030", "접근이 거부되었습니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND("4040", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 서버 에러
    INTERNAL_SERVER_ERROR("5000", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
