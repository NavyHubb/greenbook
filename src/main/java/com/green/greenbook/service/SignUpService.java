package com.green.greenbook.service;

import com.green.greenbook.domain.dto.MemberResponseDto;
import com.green.greenbook.domain.form.SignUpForm;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.green.greenbook.exception.ErrorCode.ALREADY_REGISTERED_EMAIL;
import static com.green.greenbook.exception.ErrorCode.ALREADY_REGISTERED_NICKNAME;

@Service
@RequiredArgsConstructor
@Transactional
public class SignUpService {

    private final MemberRepository memberRepository;

    public MemberResponseDto signUp(SignUpForm form) {
        if (isEmailExistManager(form.getEmail())) {
            throw new CustomException(ALREADY_REGISTERED_EMAIL);
        }
        if (isNicknameExistManager(form.getNickname())) {
            throw new CustomException(ALREADY_REGISTERED_NICKNAME);
        }

        return Member.from(memberRepository.save(fromForm(form)));
    }

    public Member signUpTest(SignUpForm form) {
        if (isEmailExistManager(form.getEmail())) {
            throw new CustomException(ALREADY_REGISTERED_EMAIL);
        }
        if (isNicknameExistManager(form.getNickname())) {
            throw new CustomException(ALREADY_REGISTERED_NICKNAME);
        }

        return memberRepository.save(fromForm(form));
    }

    private Member fromForm(SignUpForm form) {
        return Member.builder()
            .email(form.getEmail())
            .password(new BCryptPasswordEncoder().encode(form.getPassword()))
            .nickname(form.getNickname())
            .build();
    }

    private boolean isEmailExistManager(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    private boolean isNicknameExistManager(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

}