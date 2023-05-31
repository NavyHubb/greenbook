package com.green.greenbook.domain.dto.chat;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {

    @NotNull(message = "채팅방 이름은 필수값입니다.")
    private String name;

}