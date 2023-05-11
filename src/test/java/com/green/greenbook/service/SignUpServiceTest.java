package com.green.greenbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.green.greenbook.domain.form.SignUpForm;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @InjectMocks
    SignUpService signUpService;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입_성공")
    void signUp_success() {
        // given
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String originPW = "password11!!";
        SignUpForm form = SignUpForm.builder()
            .email("kay@gmail.com")
            .password(originPW)
            .nickname("kayNick")
            .build();

        Member member = Member.builder()
            .id(1L)
            .build();

        // mocking
        given(memberRepository.save(any()))
            .willReturn(member);

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        // when
        signUpService.signUp(form);

        // then
        verify(memberRepository, times(1)).save(captor.capture());
        assertEquals(captor.getValue().getEmail(), form.getEmail());
        assertEquals(captor.getValue().getNickname(), form.getNickname());
        assertTrue(encoder.matches(originPW, captor.getValue().getPassword()));
    }

    @Test
    @DisplayName("회원가입_실패_이미 등록된 이메일")
    void signUp_failure_ALREADY_REGISTERED_MEMBER() {
        //given
        String email = "kay@gmail.com";
        Member member = Member.builder()
            .id(1L)
            .email(email)
            .build();

        SignUpForm form = SignUpForm.builder()
            .email(email)
            .password("password11!!")
            .nickname("kayNick")
            .build();

        given(memberRepository.findByEmail(anyString()))
            .willReturn(Optional.of(member));

        //when
        CustomException exception = assertThrows(CustomException.class,
            () -> signUpService.signUp(form));

        //then
        assertEquals(ErrorCode.ALREADY_REGISTERED_EMAIL, exception.getErrorCode());
    }

    @Test
    @DisplayName("회원가입_실패_이미 등록된 닉네임")
    void signUp_failure_ALREADY_REGISTERED_NICKNAME() {
        //given
        String nickname = "patrick";
        Member member = Member.builder()
            .id(1L)
            .nickname(nickname)
            .build();

        SignUpForm form = SignUpForm.builder()
            .email("kay@gmail.com")
            .password("password11!!")
            .nickname(nickname)
            .build();

        given(memberRepository.findByNickname(anyString()))
            .willReturn(Optional.of(member));

        //when
        CustomException exception = assertThrows(CustomException.class,
            () -> signUpService.signUp(form));

        //then
        assertEquals(ErrorCode.ALREADY_REGISTERED_NICKNAME, exception.getErrorCode());
    }

}