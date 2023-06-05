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
    ALREADY_REGISTERED_HEAD(HttpStatus.BAD_REQUEST, "이미 등록된 제목입니다."),
    ALREADY_REGISTERED_SCRAP(HttpStatus.BAD_REQUEST, "이미 등록된 스크랩입니다."),
    ALREADY_JOINED_MEMBER(HttpStatus.BAD_REQUEST, "이미 채팅방에 참여한 멤버입니다."),

    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    NOT_FOUND_ARCHIVE(HttpStatus.BAD_REQUEST, "아카이브를 찾을 수 없습니다."),
    NOT_FOUND_HEART(HttpStatus.BAD_REQUEST, "하트를 찾을 수 없습니다."),
    NOT_FOUND_REVIEW(HttpStatus.BAD_REQUEST, "리뷰를 찾을 수 없습니다."),
    NOT_FOUND_SCRAP(HttpStatus.BAD_REQUEST, "스크랩을 찾을 수 없습니다."),
    NOT_FOUND_ROOM(HttpStatus.BAD_REQUEST, "채팅방을 찾을 수 없습니다."),
    NOT_ROOM_MASTER(HttpStatus.BAD_REQUEST, "방장이 아닙니다."),
    NOT_EXIST_CLIENT(HttpStatus.BAD_REQUEST, "해당 채팅방에 클라이언트가 없습니다."),

    CHAT_ERROR(HttpStatus.BAD_REQUEST, "채팅 발송에 에러가 발생했습니다."),

    TRANSACTION_LOCK(HttpStatus.BAD_REQUEST,"해당 객체는 사용 중입니다."),

    NO_AUTHORIZATION(HttpStatus.BAD_REQUEST, "인증 정보가 불일치합니다."),

    EMPTY(HttpStatus.BAD_REQUEST, "삭제할 수량이 없습니다."),

    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}
