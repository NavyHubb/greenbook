package com.green.greenbook.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.green.greenbook.domain.dto.chat.ChatRoomRequest;
import com.green.greenbook.domain.model.ChatRoom;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.ChatMemberRepository;
import com.green.greenbook.repository.ChatRoomRepository;
import com.green.greenbook.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private ChatMemberRepository chatMemberRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    @DisplayName("채팅방 생성 - 성공")
    void create_success() {
        //given
        ChatRoomRequest request = new ChatRoomRequest("Title");
        Member member = getMember();
        ChatRoom chatRoom = ChatRoom.builder()
            .name(request.getName())
            .masterMemberId(member.getId())
            .build();

        given(memberRepository.findById(anyLong()))
            .willReturn(Optional.of(member));
        given(chatRoomRepository.save(any()))
            .willReturn(chatRoom);

        //when
        chatRoomService.create(request, member.getId());
        ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);

        //then
        verify(chatRoomRepository, times(1)).save(captor.capture());
        assertEquals(request.getName(), captor.getValue().getName());
    }

    @Test
    @DisplayName("채팅방 입장_실패_이미 참여한 채팅방")
    void join_fail() {
        //given
        Member member = getMember();
        ChatRoom chatRoom = getChatRoom();
        List<Long> chatMemberIdList = new ArrayList<>();
        chatMemberIdList.add(member.getId());

        given(memberRepository.findById(anyLong()))
            .willReturn(Optional.of(member));
        given(chatRoomRepository.findById(anyLong()))
            .willReturn(Optional.of(chatRoom));
        given(chatMemberRepository.findChatMemberIdByChatRoom_Id(chatRoom.getId()))
            .willReturn(chatMemberIdList);

        //when
        CustomException exception = assertThrows(CustomException.class,
            () -> chatRoomService.join(1L, 1L));

        //then
        assertEquals(ErrorCode.ALREADY_JOINED_MEMBER, exception.getErrorCode());
    }

    @Test
    @DisplayName("삭제_실패_방장 아님")
    void delete_fail() {
        //given
        ChatRoom chatRoom = getChatRoom();

        given(chatRoomRepository.findById(anyLong()))
            .willReturn(Optional.of(chatRoom));

        //when
        CustomException exception = assertThrows(CustomException.class,
            () -> chatRoomService.delete(222L, 1L));

        //then
        assertEquals(ErrorCode.NOT_ROOM_MASTER, exception.getErrorCode());
    }

    private Member getMember() {
        Member member = new Member();
        member.setId(1L);

        return member;
    }

    private ChatRoom getChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);

        return chatRoom;
    }
}