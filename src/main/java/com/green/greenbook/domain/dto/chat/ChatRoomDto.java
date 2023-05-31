package com.green.greenbook.domain.dto.chat;

import com.green.greenbook.domain.model.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {

    private Long chatRoomId;
    private String name;

    public static ChatRoomDto from(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
            .chatRoomId(chatRoom.getId())
            .name(chatRoom.getName())
            .build();
    }

}