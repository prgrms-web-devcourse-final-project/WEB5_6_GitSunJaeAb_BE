package com.gitsunjaeab.mapick.infra.error;

import com.gitsunjaeab.mapick.application.api.roadmap.dto.bookmark.BookmarkCreateResponse;
import com.gitsunjaeab.mapick.application.api.roadmap.dto.bookmark.BookmarkDeleteResponse;
import com.gitsunjaeab.mapick.infra.common.response.ApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.BaseApiResponse;
import com.gitsunjaeab.mapick.infra.common.response.ErrorResponse;
import com.gitsunjaeab.mapick.infra.common.response.ResponseCode;
import com.gitsunjaeab.mapick.infra.error.exceptions.CommonException;
import com.gitsunjaeab.mapick.infra.error.exceptions.DuplicatedBookmarkException;
import com.gitsunjaeab.mapick.infra.error.exceptions.ForbiddenException;
import com.gitsunjaeab.mapick.infra.error.exceptions.InvalidRoadmapTypeException;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthenticatedException;
import com.gitsunjaeab.mapick.infra.error.exceptions.UnauthorizedAccessException;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.gitsunjaeab.mapick")
@Slf4j
public class RestExceptionAdvice {

    // 비즈니스 로직에서 발생하는 공통 예외 처리
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse> handleCommonException(CommonException e) {
        e.printStackTrace();
        log.info("RestExceptionAdvice에서 처리");
        return ResponseEntity.ok(ApiResponse.of(e.code()));
    }

    // 인가 실패(403) 발생 시 처리 (ex. 권한 없는 리소스 접근)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse handleAccessDenied(AccessDeniedException e) {
        e.printStackTrace();
        log.info("RestExceptionAdvice에서 처리");
        return ApiResponse.of(ResponseCode.FORBIDDEN);
    }

    // 인증 실패(401) 발생 시 처리 (ex. 로그인 안 된 사용자, 잘못된 인증 토큰 등)
    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse handleAuthentication(AuthenticationException e) {
        e.printStackTrace();
        log.info("RestExceptionAdvice에서 처리");
        return ApiResponse.of(ResponseCode.UNAUTHORIZED); // 401
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("잘못된 요청 매개변수 오류: {}", e.getMessage());
        log.info("RestExceptionAdvice에서 처리");
        return ResponseEntity
            .status(ResponseCode.INVALID_INPUT.getStatus())
            .body(ApiResponse.of(ResponseCode.INVALID_INPUT, e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(
        DataIntegrityViolationException e) {
        log.error("DB 제약 조건 위반 오류 발생: {}", e.getMessage());
        log.info("RestExceptionAdvice에서 처리");

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

    // request 요청 유효값 검증
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getDefaultMessage())
                .orElse("요청이 유효하지 않습니다.");
        log.info("RestExceptionAdvice에서 처리");
        log.info("✅ 요청 유효성 검증 실패: {}", errorMessage);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.INVALID_INPUT, errorMessage));
    }

    // 기타 처리되지 않은 예외 처리 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ApiResponse handleEtcException(Exception e) {
        e.printStackTrace(); // 디버깅용 로그
        log.info("RestExceptionAdvice에서 처리");
        return ApiResponse.of(ResponseCode.INTERNAL_ERROR);
    }

    /**
     * 커스텀 예외
     * */

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiResponse> handleUnauthenticatedException(UnauthenticatedException e) {
        return ResponseEntity
            .status(e.getCode().getStatus())
            .body(ApiResponse.of(e.getCode()));
    }

    @ExceptionHandler(DuplicatedBookmarkException.class)
    public ResponseEntity<BaseApiResponse> handleDuplicatedBookmark(
        DuplicatedBookmarkException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new BookmarkCreateResponse("4001", ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<BaseApiResponse> handleForbiddenFromBookMarkDelete(
        ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new BookmarkDeleteResponse("4001", ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(InvalidRoadmapTypeException.class)
    public ResponseEntity<BaseApiResponse> handleInvalidType(InvalidRoadmapTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new BookmarkDeleteResponse(ResponseCode.INVALID_ROADMAP_TYPE.getCode(),
                ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<BaseApiResponse> handleUnauthorized(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            new ErrorResponse(
                ResponseCode.FORBIDDEN.getCode(),
                ex.getMessage(),
                OffsetDateTime.now()
            )
        );
    }




}