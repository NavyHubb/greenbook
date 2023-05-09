package com.green.greenbook.service;

import com.green.greenbook.domain.form.SignUpForm;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.green.greenbook.exception.ErrorCode.ALREADY_REGISTERED_MEMBER;
import static com.green.greenbook.exception.ErrorCode.ALREADY_REGISTERED_NICKNAME;

@Service
@RequiredArgsConstructor
@Transactional
public class SignUpService {

    private final MemberRepository memberRepository;

    public String signUp(SignUpForm form) {
        if (isEmailExistManager(form.getEmail())) {
            throw new CustomException(ALREADY_REGISTERED_MEMBER);
        }
        if (isNicknameExistManager(form.getNickname())) {
            throw new CustomException(ALREADY_REGISTERED_NICKNAME);
        }

        memberRepository.save(Member.from(form));

        return "회원가입이 완료되었습니다.";
    }

    private boolean isEmailExistManager(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    private boolean isNicknameExistManager(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

}