package com.green.greenbook.service;

import com.green.greenbook.domain.dto.chat.ChatMsgRequest;
import com.green.greenbook.domain.dto.chat.ChatMsgResponse;
import com.green.greenbook.domain.model.ChatMsg;
import com.green.greenbook.domain.model.ChatRoom;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.ChatMsgRepository;
import com.green.greenbook.repository.ChatRoomRepository;
import com.green.greenbook.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMsgService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMsgRepository chatMsgRepository;
    private final MemberRepository memberRepository;

    public ChatMsgResponse send(ChatMsgRequest message, Long memberId, Long roomId) {
        Member member = getMember(memberId);
        ChatRoom chatRoom = getChatRoom(roomId);

        ChatMsg chatMsg = ChatMsg.builder()
            .message(message.getMessage())
            .member(member)
            .chatRoom(chatRoom)
            .build();
        chatMsgRepository.save(chatMsg);

        return ChatMsgResponse.builder()
            .roomId(roomId)
            .senderNickname(member.getNickname())
            .message(message.getMessage())
            .sendTime(chatMsg.getCreatedAt())
            .build();
    }

    public List<ChatMsgResponse> getChatMsgList(Long roomId, Long memberId) {
        memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        List<ChatMsg> chatMsgsList = chatMsgRepository.findTop10ByChatRoom_IdOrderByCreatedAtDesc(roomId);

        return chatMsgsList.stream().map(chatMsg -> ChatMsgResponse.builder()
                                                        .roomId(chatMsg.getChatRoom().getId())
                                                        .senderNickname(chatMsg.getMember().getNickname())
                                                        .message(chatMsg.getMessage())
                                                        .sendTime(chatMsg.getCreatedAt())
                                                        .build())
            .collect(Collectors.toList());
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));
    }

}