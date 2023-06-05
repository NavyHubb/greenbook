package com.green.greenbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatMsgServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private ChatMsgService chatMsgService;

    @Test
    @DisplayName("채팅 메세지 목록 조회_실패_존재하지 않는 멤버")
    void getChatMsgList_fail() {
        //given
        given(memberRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
            () -> chatMsgService.getChatMsgList(1L, 1L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_MEMBER, exception.getErrorCode());
    }

}