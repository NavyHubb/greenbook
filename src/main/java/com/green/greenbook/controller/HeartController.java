package com.green.greenbook.controller;

import com.green.greenbook.config.JwtAuthenticationProvider;
import com.green.greenbook.domain.dto.MemberDto;
import com.green.greenbook.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping("/{archiveId}")
    public ResponseEntity<String> create(@RequestHeader(name = TOKEN_NAME) String token,
                                         @PathVariable Long archiveId) {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");

        MemberDto dto = provider.getMemberDto(token);

        heartService.create(dto.getId(), archiveId);

        return ResponseEntity.ok("create success");
    }

    @DeleteMapping("/{archiveId}")
    public ResponseEntity<String> delete(@RequestHeader(name = TOKEN_NAME) String token,
                                         @PathVariable Long archiveId) {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");

        MemberDto dto = provider.getMemberDto(token);

        heartService.delete(dto.getId(), archiveId);

        return ResponseEntity.ok("delete success");
    }

}