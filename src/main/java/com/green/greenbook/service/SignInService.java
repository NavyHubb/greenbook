package com.green.greenbook.service;

import com.green.greenbook.config.JwtAuthenticationProvider;
import com.green.greenbook.domain.form.SignInForm;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.domain.type.MemberType;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.MemberRepository;
import com.green.greenbook.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final MemberRepository memberRepository;
    private final JwtAuthenticationProvider provider;

    public String signIn(SignInForm form) {
        Member m = memberRepository.findByEmail(form.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        if (!PasswordUtil.checkPassword(form.getPassword(), m.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        return provider.createToken(m.getEmail(), m.getId(), MemberType.USER);
    }

}