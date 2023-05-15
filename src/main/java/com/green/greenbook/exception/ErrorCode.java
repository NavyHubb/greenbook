package com.green.greenbook.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다."),
    ALREADY_REGISTERED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 등록된 닉네임입니다."),
    ALREADY_REGISTERED_BOOKTITLE(HttpStatus.BAD_REQUEST, "이미 등록된 도서명입니다."),
    ALREADY_REGISTERED_ISBN(HttpStatus.BAD_REQUEST, "이미 등록된 ISBN입니다."),
    ALREADY_REGISTERED_HEART(HttpStatus.BAD_REQUEST, "이미 등록된 하트입니다."),
    ALREADY_REGISTERED_HEAD(HttpStatus.BAD_REQUEST, "이미 등록된 하트입니다."),

    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    NOT_FOUND_ARCHIVE(HttpStatus.BAD_REQUEST, "아카이브를 찾을 수 없습니다."),
    NOT_FOUND_HEART(HttpStatus.BAD_REQUEST, "하트를 찾을 수 없습니다."),
    NOT_FOUND_REVIEW(HttpStatus.BAD_REQUEST, "리뷰를 찾을 수 없습니다."),

    NO_AUTHORIZATION(HttpStatus.BAD_REQUEST, "인증 정보가 불일치합니다."),

    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}
