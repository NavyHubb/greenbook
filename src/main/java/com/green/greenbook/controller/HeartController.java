package com.green.greenbook.controller;

import com.green.greenbook.config.JwtAuthenticationProvider;
import com.green.greenbook.domain.dto.HeartRequest;
import com.green.greenbook.domain.dto.MemberDto;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping
    public ResponseEntity<String> create(@RequestHeader(name = TOKEN_NAME) String token,
                                         @RequestBody HeartRequest request) {
        MemberDto dto = provider.getMemberDto(token);
        if (!Objects.equals(dto.getId(), request.getMemberId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        heartService.create(dto.getId(), request.getArchiveId());

        return ResponseEntity.ok("하트 등록이 정상적으로 처리되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestHeader(name = TOKEN_NAME) String token,
                                         @RequestBody HeartRequest request) {
        MemberDto dto = provider.getMemberDto(token);
        if (!Objects.equals(dto.getId(), request.getMemberId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        heartService.delete(request.getMemberId(), request.getArchiveId());

        return ResponseEntity.ok("하트 삭제가 정상적으로 처리되었습니다.");
    }

}