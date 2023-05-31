package com.green.greenbook.domain.dto.chat;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMsgResponse {

    private Long roomId;
    private String senderNickname;
    private String message;
    private LocalDateTime sendTime;

}