package com.gitsunjaeab.mapick.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // 클라이언트 에러
    INVALID_INPUT("4000", HttpStatus.BAD_REQUEST, "잘못된 요청 데이터입니다."),
    EMAIL_ALREADY_REGISTERED_LOCALLY("4014", HttpStatus.BAD_REQUEST, "이미 로컬로 가입된 이메일입니다."),
    PROVIDER_MISMATCH("4014", HttpStatus.BAD_REQUEST, "잘못된 provider 입니다."),

    UNAUTHORIZED("4010", HttpStatus.UNAUTHORIZED, "인증이 필요합니다(토큰확인)"),
    INVALID_TOKEN("4011", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    SECURITY_INCIDENT("4012", HttpStatus.UNAUTHORIZED, "비정상적인 접근이 감지되었습니다."),
    INVALID_PASSWORD("4013", HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),


    FORBIDDEN("4030", HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    NOT_FOUND("4040", HttpStatus.NOT_FOUND, "존재하지 않는 리소스 입니다."),
    EMAIL_NOT_FOUND("4041", HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    MEMBER_NOT_FOUND("4042", HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    INTEREST_NOT_FOUND("4043", HttpStatus.NOT_FOUND, "존재하지 않는 관심분야 입니다."),


    CONFLICT("4090", HttpStatus.CONFLICT, "충돌이 발생하였습니다."),
    NICKNAME_DUPLICATED("4091", HttpStatus.CONFLICT, "중복된 닉네임입니다."),
    ALREADY_REGISTERED_EMAIL("4092", HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),

    DB_CONSTRAINT_VIOLATION("4093", HttpStatus.CONFLICT, "DB 제약 조건에 위배되었습니다."),
    ALREADY_REGISTERED_BLACKLIST("4094", HttpStatus.CONFLICT, "이미 블랙리스트에 등록된 회원 입니다."),
    ALREADY_REGISTERED_ADMIN("4095", HttpStatus.CONFLICT, "이미 관리자로 등록된 회원 입니다."),
    ALREADY_DELETED_USER("4096", HttpStatus.CONFLICT, "이미 삭제된 회원 입니다."),


    // 내부 오류
    INTERNAL_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    FILE_UPLOAD_FAILED("5001", HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),

    // 성공
    OK("2000", HttpStatus.OK, "정상적으로 완료되었습니다."),
    SIGNUP_SUCCESS("2001", HttpStatus.OK, "회원가입 성공입니다."),
    SIGNIN_SUCCESS("2002", HttpStatus.OK, "로그인 성공입니다."),
    SOCIAL_SIGNIN_SUCCESS("2003", HttpStatus.OK, "소셜 로그인 성공입니다."),
    VERITY_PASSWORD_SUCCESS("2004", HttpStatus.OK, "비밀번호 검증 성공 입니다."),
    CHANGE_PASSWORD_SUCCESS("2004", HttpStatus.OK, "비밀번호 변경 성공 입니다."),
    LOGOUT_SUCCESS("200", HttpStatus.OK, "로그아웃 되었습니다.");


    private final String code;
    private final HttpStatus status;
    private final String message;
}
