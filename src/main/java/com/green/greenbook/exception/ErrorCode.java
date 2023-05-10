package com.green.greenbook.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다."),
    ALREADY_REGISTERED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 등록된 닉네임입니다."),

    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),

    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}
