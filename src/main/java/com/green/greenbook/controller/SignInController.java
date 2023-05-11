package com.green.greenbook.controller;

import com.green.greenbook.domain.dto.TokenDto;
import com.green.greenbook.domain.form.SignInForm;
import com.green.greenbook.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signIn")
@RequiredArgsConstructor
public class SignInController {

    private final SignInService signInService;

    @PostMapping
    public ResponseEntity<TokenDto> signIn(@RequestBody SignInForm form) {
        return ResponseEntity.ok(TokenDto.from(signInService.signIn(form)));
    }

}