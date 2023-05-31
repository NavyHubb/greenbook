package com.green.greenbook.service;

import com.green.greenbook.domain.dto.chat.ChatRoomRequest;
import com.green.greenbook.domain.dto.chat.ChatRoomDto;
import com.green.greenbook.domain.model.ChatMember;
import com.green.greenbook.domain.model.ChatRoom;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.ChatMemberRepository;
import com.green.greenbook.repository.ChatRoomRepository;
import com.green.greenbook.repository.MemberRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final MemberRepository memberRepository;

    public void create(ChatRoomRequest request, Long memberId) {
        Member member = getMember(memberId);
        ChatRoom chatRoom = ChatRoom.builder()
            .name(request.getName())
            .masterMemberId(memberId)
            .build();
        chatRoomRepository.save(chatRoom);

        ChatMember chatMember = ChatMember.builder()
            .chatRoom(chatRoom)
            .member(member)
            .build();

        chatMemberRepository.save(chatMember);
    }

    public void join(Long chatRoomId, Long memberId) {
        Member member = getMember(memberId);
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        List<Long> chatMemberIdList = chatMemberRepository.findChatMemberIdByChatRoom_Id(chatRoomId);

        if (chatMemberIdList.contains(memberId)) {
            throw new CustomException(ErrorCode.ALREADY_JOINED_MEMBER);
        }

        ChatMember chatMember = ChatMember.builder()
            .member(member)
            .chatRoom(chatRoom)
            .build();
        chatMemberRepository.save(chatMember);
    }

    public void delete(Long memberId, Long chatRoomId) {
        ChatRoom room = getChatRoom(chatRoomId);

        if (!Objects.equals(room.getMasterMemberId(), memberId)) {
            throw new CustomException(ErrorCode.NOT_ROOM_MASTER);
        }

        // 영속성 전이를 통해 chatRoom에 연관된 데이터(chatMember, chatMsg)도 함께 삭제된다
        chatRoomRepository.deleteById(chatRoomId);
    }

    public List<ChatRoomDto> findAllRoom(Pageable pageable) {
        Page<ChatRoom> allChatRoom = chatRoomRepository.findAll(pageable);

        return allChatRoom.stream().map(ChatRoomDto::from)
            .collect(Collectors.toList());
    }

    public ChatRoomDto findById(Long chatRoomId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        return ChatRoomDto.from(chatRoom);
    }

    private ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

}