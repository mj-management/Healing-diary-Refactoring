package com.ssafy.healingdiary.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    BAD_REQUEST(HttpStatus.BAD_REQUEST, -1,"잘못된 요청입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, -2,"허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -3,"내부 서버 오류입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, -4, "권한이 없는 사용자입니다."),
    ENTITY_NOT_FOUND(HttpStatus.OK, -5,"엔티티를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.OK, -6,"댓글을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.OK, -7,"사용자를 찾을 수 없습니다."),
    CLUB_NOT_FOUND(HttpStatus.OK, -8,"소모임을 찾을 수 없습니다."),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, -9,"토큰이 유효하지 않습니다."),
    NOT_FOUND_DATA(HttpStatus.OK, -10,"요청하신 데이터가 없습니다."),
    RECORD_NOT_FOUND(HttpStatus.OK, -11,"음성 파일이 없거나 유효기간이 지났습니다."),
    TAG_NOT_FOUND(HttpStatus.OK, -12,"태그를 찾을 수 없습니다."),
    DIARY_NOT_FOUND(HttpStatus.OK, -13,"일기를 찾을 수 없습니다."),

    LOG_OUT(HttpStatus.OK, -14,"로그아웃을 해주세요."),

    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, -15,"만료된 Access Token 입니다."),
    NOTICE_NOT_FOUND(HttpStatus.OK, -16, "알림을 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, -409, "이미 가입된 회원입니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;
}
