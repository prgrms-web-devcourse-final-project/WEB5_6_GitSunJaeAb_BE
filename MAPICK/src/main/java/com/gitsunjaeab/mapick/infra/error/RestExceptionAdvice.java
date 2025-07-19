package com.gitsunjaeab.mapick.infra.error;

import com.gitsunjaeab.mapick.common.response.ApiResponse;
import com.gitsunjaeab.mapick.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.error.exceptions.ZzimException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthenticatedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.gitsunjaeab.mapick")
@Slf4j
public class RestExceptionAdvice {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse> handleCommonException(CommonException e) {
        e.printStackTrace();
        return ResponseEntity.ok(ApiResponse.of(e.code()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("잘못된 요청 매개변수 오류: {}", e.getMessage());

        return ResponseEntity
            .status(ResponseCode.INVALID_INPUT.getStatus())
            .body(ApiResponse.of(ResponseCode.INVALID_INPUT, e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DB 제약 조건 위반 오류 발생: {}", e.getMessage());

        // 중복 키 오류 메시지 커스터마이징
        String errorMessage = "DB 제약 조건에 위배되었습니다.";
        if (e.getMessage().contains("duplicate key")) {
            errorMessage = "중복된 데이터가 존재합니다. 다시 시도해주세요.";
        } else if (e.getMessage().contains("foreign key")) {
            errorMessage = "참조 무결성 제약 조건에 위배되었습니다.";
        }

        return ResponseEntity
            .status(ResponseCode.DB_CONSTRAINT_VIOLATION.getStatus())
            .body(ApiResponse.of(ResponseCode.DB_CONSTRAINT_VIOLATION, errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleEtcException(Exception e) {
        e.printStackTrace(); // 디버깅용 로그
        return ApiResponse.of(ResponseCode.INTERNAL_ERROR);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiResponse> handleUnauthenticatedException(UnauthenticatedException e) {
        return ResponseEntity
                .status(e.getCode().getStatus())
                .body(ApiResponse.of(e.getCode()));
    }

    @ExceptionHandler(ZzimException.class)
    public ResponseEntity<ApiResponse> handleZzimException(ZzimException e) {
        return ResponseEntity
            .status(e.code().getStatus())
            .body(ApiResponse.of(e.code()));
    }
}