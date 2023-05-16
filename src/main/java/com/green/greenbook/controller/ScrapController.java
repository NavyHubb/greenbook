package com.green.greenbook.controller;

import com.green.greenbook.config.JwtAuthenticationProvider;
import com.green.greenbook.domain.dto.MemberDto;
import com.green.greenbook.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
public class ScrapController {

    private final ScrapService scrapService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping("/{reviewId}")
    public ResponseEntity<?> create(@RequestHeader(name = TOKEN_NAME) String token,
                                    @PathVariable Long reviewId) {
        MemberDto dto = provider.getMemberDto(token);
        scrapService.create(dto.getId(),reviewId);

        return ResponseEntity.ok("스크랩이 저장되었습니다.");
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> delete(@RequestHeader(name = TOKEN_NAME) String token,
                                    @PathVariable Long reviewId) {
        MemberDto dto = provider.getMemberDto(token);
        scrapService.delete(dto.getId(),reviewId);

        return ResponseEntity.ok("스크랩이 삭제되었습니다.");
    }

}